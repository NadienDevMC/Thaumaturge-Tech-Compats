package nadiendev.ThaumaturgeTechCompats.edible;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.datagen.RecipeSaver;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Item definitions that point at the model of the original item; no new models or textures. */
public class EdibleAssets implements DataProvider {
    private final PackOutput output;

    public EdibleAssets(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (EdibleEntry entry : EdibleCatalog.entries()) {
            tasks.add(RecipeSaver.save(cache, definition(entry),
                    output.getOutputFolder().resolve("assets/" + ThaumaturgeTechCompats.MOD_ID + "/items/" + entry.id() + ".json")));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats edible item definitions";
    }

    private static JsonObject model(String model) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:model");
        json.addProperty("model", model);
        return json;
    }

    static JsonObject definition(EdibleEntry entry) {
        String base = entry.original().getNamespace() + ":item/" + entry.original().getPath();
        JsonObject json = new JsonObject();
        if (!entry.spear()) {
            json.add("model", model(base));
            return json;
        }
        JsonObject select = new JsonObject();
        select.addProperty("type", "minecraft:select");
        JsonArray cases = new JsonArray();
        JsonObject inventory = new JsonObject();
        inventory.add("model", model(base));
        JsonArray when = new JsonArray();
        for (String context : new String[]{"gui", "ground", "fixed", "on_shelf"}) {
            when.add(context);
        }
        inventory.add("when", when);
        cases.add(inventory);
        select.add("cases", cases);
        select.add("fallback", model(base + "_in_hand"));
        select.addProperty("property", "minecraft:display_context");
        json.add("model", select);
        json.addProperty("swap_animation_scale", 1.95F);
        return json;
    }
}
