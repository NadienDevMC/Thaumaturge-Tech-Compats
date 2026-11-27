package nadiendev.ThaumaturgeTechCompats.metals;

import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Client data of the new metals: item models, block model, blockstate. */
public class ExtraMetalAssets implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String GENERATED = "minecraft:item/generated";
    private static final String HANDHELD = "minecraft:item/handheld";

    private final PackOutput output;

    public ExtraMetalAssets(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (ExtraMetal metal : ExtraMetal.values()) {
            tasks.add(itemModel(cache, metal.ingotName(), GENERATED));
            tasks.add(itemModel(cache, metal.nuggetName(), GENERATED));
            tasks.add(itemModel(cache, metal.clusterName(), GENERATED));
            tasks.add(itemModel(cache, metal.capName(), GENERATED));
            tasks.add(itemModel(cache, metal.inertCapName(), GENERATED));
            tasks.add(itemModel(cache, metal.rodName(), HANDHELD));
            tasks.add(itemModel(cache, metal.inertRodName(), HANDHELD));

            JsonObject blockItem = new JsonObject();
            blockItem.addProperty("parent", MOD + ":block/" + metal.blockName());
            tasks.add(save(cache, "models/item/" + metal.blockName(), blockItem));

            JsonObject textures = new JsonObject();
            textures.addProperty("all", MOD + ":block/" + metal.blockName());
            JsonObject blockModel = new JsonObject();
            blockModel.addProperty("parent", "minecraft:block/cube_all");
            blockModel.add("textures", textures);
            tasks.add(save(cache, "models/block/" + metal.blockName(), blockModel));

            JsonObject variant = new JsonObject();
            variant.addProperty("model", MOD + ":block/" + metal.blockName());
            JsonObject variants = new JsonObject();
            variants.add("", variant);
            JsonObject state = new JsonObject();
            state.add("variants", variants);
            tasks.add(save(cache, "blockstates/" + metal.blockName(), state));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats extra metals assets";
    }

    private CompletableFuture<?> itemModel(CachedOutput cache, String name, String parent) {
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD + ":item/" + name);
        JsonObject json = new JsonObject();
        json.addProperty("parent", parent);
        json.add("textures", textures);
        return save(cache, "models/item/" + name, json);
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, JsonObject json) {
        return CompatJson.save(output, cache, "assets/" + MOD + "/" + path + ".json", json);
    }
}
