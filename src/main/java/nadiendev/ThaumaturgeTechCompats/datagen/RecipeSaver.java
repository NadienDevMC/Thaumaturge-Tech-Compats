package nadiendev.ThaumaturgeTechCompats.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.ResearchTabs;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class RecipeSaver {
    private static final String RESEARCH_ENTRY = "/" + ThaumaturgeTechCompats.MOD_ID + "/thaumaturge/research_entry/";

    private RecipeSaver() {
    }

    public static CompletableFuture<?> save(CachedOutput cache, JsonElement json, Path path) {
        String normalized = path.toString().replace('\\', '/');
        JsonElement output = normalized.contains("/recipe/") ? normalize(json) : json;
        int entry = normalized.indexOf(RESEARCH_ENTRY);
        if (entry >= 0 && normalized.endsWith(".json") && output.isJsonObject()) {
            // Every research entry of this mod is moved to its Thaumonomicon tab (one per creative sub-tab) here.
            String name = normalized.substring(entry + RESEARCH_ENTRY.length(), normalized.length() - ".json".length());
            output = ResearchTabs.place(name, output.getAsJsonObject());
        }
        return DataProvider.saveStable(cache, output, path);
    }

    public static JsonElement normalize(JsonElement element) {
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            JsonArray result = new JsonArray();
            for (JsonElement child : array) {
                result.add(normalize(child));
            }
            return result;
        }
        if (!element.isJsonObject()) {
            return element;
        }
        JsonObject object = element.getAsJsonObject();
        if (object.size() == 1) {
            Map.Entry<String, JsonElement> only = object.entrySet().iterator().next();
            if (only.getValue().isJsonPrimitive() && only.getValue().getAsJsonPrimitive().isString()) {
                if (only.getKey().equals("item")) {
                    return new JsonPrimitive(only.getValue().getAsString());
                }
                if (only.getKey().equals("tag")) {
                    return new JsonPrimitive("#" + only.getValue().getAsString());
                }
            }
        }
        JsonObject result = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            result.add(entry.getKey(), normalize(entry.getValue()));
        }
        return result;
    }
}
