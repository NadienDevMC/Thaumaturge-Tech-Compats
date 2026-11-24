package nadiendev.ThaumaturgeTechCompats.edible;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import nadiendev.ThaumaturgeTechCompats.datagen.RecipeSaver;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Shapeless recipes: original item + sugar gives the edible copy. */
public class EdibleContent implements DataProvider {
    private final PackOutput output;

    public EdibleContent(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (EdibleEntry entry : EdibleCatalog.entries()) {
            tasks.add(RecipeSaver.save(cache, recipe(entry),
                    output.getOutputFolder().resolve("data/" + ThaumaturgeTechCompats.MOD_ID + "/recipe/edible/" + entry.id() + ".json")));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats edible recipes";
    }

    static JsonObject recipe(EdibleEntry entry) {
        JsonArray conditions = new JsonArray();
        if (!entry.alwaysLoaded()) {
            conditions.add(CompatJson.modLoaded(entry.original().getNamespace()));
        }
        conditions.add(CompatJson.feature(EdibleIntegration.TOGGLE));
        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", conditions);
        json.addProperty("type", "minecraft:crafting_shapeless");
        json.addProperty("category", "misc");
        json.add("ingredients", ContentJson.strings(entry.original().toString(), "minecraft:sugar"));
        json.add("result", ContentJson.result(entry.itemId().toString(), 1));
        return json;
    }
}
