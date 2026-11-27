package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Blockstates, models and item definitions of the panels. The models use the Solar Generation panel
 * model and, for now, one of its textures; the final textures go in textures/block/solar_panel/.
 */
public class SolarGenerationAssets implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String SG = SolarGenerationIntegration.MOD_ID;

    private final PackOutput output;

    public SolarGenerationAssets(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (SolarPanelType type : SolarPanelType.values()) {
            tasks.add(save(cache, "blockstates/" + type.id(), blockState(type)));
            tasks.add(save(cache, "models/block/" + type.id(), model(type)));
            tasks.add(save(cache, "items/" + type.id(), itemDefinition(type)));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats solar generation assets";
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, JsonObject json) {
        return ContentJson.save(output, cache, "assets/" + MOD + "/" + path + ".json", json);
    }

    private static JsonObject blockState(SolarPanelType type) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", MOD + ":block/" + type.id());
        JsonObject variants = new JsonObject();
        variants.add("", variant);
        JsonObject json = new JsonObject();
        json.add("variants", variants);
        return json;
    }

    private static JsonObject model(SolarPanelType type) {
        JsonObject textures = new JsonObject();
        textures.addProperty("main", SG + ":block/" + type.placeholder());
        textures.addProperty("particle", SG + ":block/particle_" + type.placeholder());
        JsonObject json = new JsonObject();
        json.addProperty("parent", SG + ":block/solar_panel");
        json.add("textures", textures);
        return json;
    }

    private static JsonObject itemDefinition(SolarPanelType type) {
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", MOD + ":block/" + type.id());
        JsonObject json = new JsonObject();
        json.add("model", model);
        return json;
    }
}
