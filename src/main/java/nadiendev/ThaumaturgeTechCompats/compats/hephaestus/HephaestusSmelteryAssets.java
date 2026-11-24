package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HephaestusSmelteryAssets implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;

    private final PackOutput output;

    public HephaestusSmelteryAssets(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (MoltenMetal metal : MoltenMetal.values()) {
            String block = metal.fluidName();
            String bucket = metal.bucketName();
            tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/blockstates/" + block + ".json", blockState(block)));
            tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/models/block/" + block + ".json", blockModel()));
            tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/models/item/" + bucket + ".json", bucketModel()));
            tasks.add(CompatJson.save(output, cache, "assets/" + MOD + "/items/" + bucket + ".json", bucketDefinition(bucket, metal.color())));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats hephaestus smeltery assets";
    }

    private static JsonObject blockState(String block) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", MOD + ":block/" + block);
        JsonObject variants = new JsonObject();
        variants.add("", variant);
        JsonObject json = new JsonObject();
        json.add("variants", variants);
        return json;
    }

    // Particle-only model: the fluid itself is rendered by the fluid renderer
    private static JsonObject blockModel() {
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", "hephaestus:fluid/liquid/molten_metal");
        JsonObject json = new JsonObject();
        json.add("textures", textures);
        return json;
    }

    private static JsonObject bucketModel() {
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "minecraft:item/bucket");
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        json.add("textures", textures);
        return json;
    }

    private static JsonObject bucketDefinition(String bucket, int argb) {
        JsonObject tint = new JsonObject();
        tint.addProperty("type", "minecraft:constant");
        tint.addProperty("value", argb & 0xFFFFFF);
        JsonArray tints = new JsonArray();
        tints.add(tint);
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", MOD + ":item/" + bucket);
        model.add("tints", tints);
        JsonObject json = new JsonObject();
        json.add("model", model);
        return json;
    }
}
