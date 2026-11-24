package nadiendev.ThaumaturgeTechCompats.edible;

import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Item models whose parent is the model of the original item; no new textures. */
public class EdibleAssets implements DataProvider {
    private final PackOutput output;

    public EdibleAssets(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (EdibleEntry entry : EdibleCatalog.entries()) {
            tasks.add(ContentJson.save(output, cache,
                    "assets/" + ThaumaturgeTechCompats.MOD_ID + "/models/item/" + entry.id() + ".json", model(entry)));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats edible item models";
    }

    static JsonObject model(EdibleEntry entry) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", entry.original().getNamespace() + ":item/" + entry.original().getPath());
        return json;
    }
}
