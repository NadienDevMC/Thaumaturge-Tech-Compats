package nadiendev.ThaumaturgeTechCompats.materials;

import com.google.gson.JsonElement;
import nadiendev.ThaumaturgeTechCompats.datagen.RecipeSaver;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** Writes the JSON of {@link ExtraMaterialJson}: either the assets or the data half. */
public class ExtraMaterialFiles implements DataProvider {
    private final PackOutput output;
    private final String prefix;

    public ExtraMaterialFiles(PackOutput output, boolean assets) {
        this.output = output;
        this.prefix = assets ? "assets/" : "data/";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : ExtraMaterialJson.files().entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                Path path = output.getOutputFolder().resolve(entry.getKey());
                tasks.add(RecipeSaver.save(cache, entry.getValue(), path));
            }
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats extra metals " + (prefix.startsWith("assets") ? "assets" : "data");
    }
}
