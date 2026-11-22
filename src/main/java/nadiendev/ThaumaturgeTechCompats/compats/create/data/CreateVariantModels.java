package nadiendev.ThaumaturgeTechCompats.compats.create.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import nadiendev.ThaumaturgeTechCompats.compats.create.CreateIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.create.casing.CasingTier;
import nadiendev.ThaumaturgeTechCompats.compats.create.variant.VariantKind;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CreateVariantModels implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String CREATE = CreateIntegration.MOD_ID;
    private static final Set<VariantKind> CUTOUT = EnumSet.of(VariantKind.MIXER, VariantKind.SAW, VariantKind.SPOUT, VariantKind.ITEM_DRAIN, VariantKind.BASIN);

    private final PackOutput output;

    public CreateVariantModels(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        try {
            for (VariantKind kind : VariantKind.values()) {
                for (CasingTier tier : CasingTier.values()) {
                    generate(cache, kind, tier, tasks);
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Create Variant Models: " + MOD;
    }

    private void generate(CachedOutput cache, VariantKind kind, CasingTier tier, List<CompletableFuture<?>> tasks) throws IOException {
        String name = kind.blockName(tier);
        Set<Identifier> models = new LinkedHashSet<>();

        JsonElement state = read(Identifier.fromNamespaceAndPath(CREATE, kind.id()), "blockstates");
        remap(state, name, models);
        tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/blockstates/" + name + ".json", state));

        Identifier itemParent = itemModelOf(kind);
        models.add(itemParent);
        JsonObject itemModel = new JsonObject();
        itemModel.addProperty("parent", target(itemParent, name).toString());
        tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/models/item/" + name + ".json", itemModel));
        JsonObject itemInner = new JsonObject();
        itemInner.addProperty("type", "minecraft:model");
        itemInner.addProperty("model", MOD + ":item/" + name);
        JsonObject itemDefinition = new JsonObject();
        itemDefinition.add("model", itemInner);
        tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/items/" + name + ".json", itemDefinition));

        for (Identifier model : models) {
            tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/models/" + target(model, name).getPath() + ".json", retextured(model, kind, tier)));
        }
    }

    private Identifier itemModelOf(VariantKind kind) throws IOException {
        try {
            JsonObject item = read(Identifier.fromNamespaceAndPath(CREATE, "item/" + kind.id()), "models").getAsJsonObject();
            return Identifier.parse(item.get("parent").getAsString());
        } catch (IOException missing) {
            JsonObject definition = read(Identifier.fromNamespaceAndPath(CREATE, kind.id()), "items").getAsJsonObject();
            return Identifier.parse(definition.getAsJsonObject("model").get("model").getAsString());
        }
    }

    private JsonObject retextured(Identifier model, VariantKind kind, CasingTier tier) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("parent", model.toString());
        if (CUTOUT.contains(kind)) {
            json.addProperty("render_type", "minecraft:cutout_mipped");
        }
        JsonObject textures = new JsonObject();
        for (Map.Entry<String, String> entry : textures(model).entrySet()) {
            if (entry.getValue().startsWith("#")) {
                continue;
            }
            Identifier texture = Identifier.parse(entry.getValue());
            if (!CREATE.equals(texture.getNamespace())) {
                continue;
            }
            Identifier mapped = texture(texture, tier);
            if (CreateVariantModels.class.getClassLoader().getResource("assets/" + mapped.getNamespace() + "/textures/" + mapped.getPath() + ".png") == null) {
                throw new IllegalStateException("Missing recolored texture " + mapped);
            }
            textures.addProperty(entry.getKey(), mapped.toString());
        }
        json.add("textures", textures);
        return json;
    }

    private Map<String, String> textures(Identifier model) throws IOException {
        if (!CREATE.equals(model.getNamespace())) {
            return new LinkedHashMap<>();
        }
        JsonObject json = read(model, "models").getAsJsonObject();
        Map<String, String> result = json.has("parent") ? textures(Identifier.parse(json.get("parent").getAsString())) : new LinkedHashMap<>();
        if (json.has("textures")) {
            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("textures").entrySet()) {
                result.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return result;
    }

    private void remap(JsonElement element, String name, Set<Identifier> models) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            for (String key : new ArrayList<>(object.keySet())) {
                JsonElement value = object.get(key);
                if ("model".equals(key) && value.isJsonPrimitive()) {
                    Identifier model = Identifier.parse(value.getAsString());
                    models.add(model);
                    object.add(key, new JsonPrimitive(target(model, name).toString()));
                } else {
                    remap(value, name, models);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (JsonElement child : array) {
                remap(child, name, models);
            }
        }
    }

    private static Identifier target(Identifier model, String name) {
        String path = model.getPath();
        if (path.startsWith("block/")) {
            path = path.substring("block/".length());
        }
        return Identifier.fromNamespaceAndPath(MOD, "block/create_variant/" + name + "/" + path.replace('/', '_'));
    }

    private static Identifier texture(Identifier texture, CasingTier tier) {
        String path = texture.getPath();
        if (path.startsWith("block/")) {
            path = path.substring("block/".length());
        }
        return Identifier.fromNamespaceAndPath(MOD, "block/create_variant/" + tier.material() + "/" + path.replace('/', '_'));
    }

    private JsonElement read(Identifier location, String prefix) throws IOException {
        String resource = "assets/" + location.getNamespace() + "/" + prefix + "/" + location.getPath() + ".json";
        InputStream stream = CreateVariantModels.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            throw new IOException("Missing resource " + resource);
        }
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader);
        }
    }
}
