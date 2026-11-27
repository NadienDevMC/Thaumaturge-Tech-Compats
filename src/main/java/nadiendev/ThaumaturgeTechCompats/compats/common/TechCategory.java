package nadiendev.ThaumaturgeTechCompats.compats.common;

import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.registry.CreativeSection;
import nadiendev.ThaumaturgeTechCompats.research.ResearchCategories;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Writes every Thaumonomicon category of the mod (one per creative sub-tab, see {@link ResearchCategories})
 * and the Tech header entry, which now opens the Extras category.
 */
public class TechCategory implements DataProvider {
    public static final String HEADER = "tech";
    /** Fallback category of the compat entries; the real one comes from ResearchLayout. */
    public static final String CATEGORY_ID = ResearchCategories.id(CreativeSection.EXTRAS);
    public static final String HEADER_ID = ThaumaturgeTechCompats.MOD_ID + ":" + HEADER;

    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;

    private final PackOutput output;

    public TechCategory(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (ResearchCategories.Category category : ResearchCategories.all()) {
            tasks.add(save(cache, "thaumaturge/research_category/" + category.path(), ResearchCategories.json(category)));
        }
        tasks.add(save(cache, "thaumaturge/research_entry/" + HEADER, header()));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats research categories";
    }

    public static void english(LanguageProvider lang) {
        for (ResearchCategories.Category category : ResearchCategories.all()) {
            lang.add(category.langKey(), category.section().english());
        }
        lang.add("research." + MOD + "." + HEADER + ".title", "Technology");
        lang.add("research." + MOD + "." + HEADER + ".stage_0",
                "Machines and magic do not have to be enemies. Wherever a tool of iron and gears meets the aura, I should be able to bend one to the other.");
        lang.add("research." + MOD + "." + HEADER + ".stage_1",
                "Everything that joins my thaumaturgy to the technology of other mods is gathered in the tabs of this book, one for each mod or family of devices.");
    }

    public static void spanish(LanguageProvider lang) {
        for (ResearchCategories.Category category : ResearchCategories.all()) {
            lang.add(category.langKey(), category.section().spanish());
        }
        lang.add("research." + MOD + "." + HEADER + ".title", "Tecnología");
        lang.add("research." + MOD + "." + HEADER + ".stage_0",
                "Las máquinas y la magia no tienen por qué ser enemigas. Allí donde una herramienta de hierro y engranajes se encuentre con el aura, debería poder doblegar una a la otra.");
        lang.add("research." + MOD + "." + HEADER + ".stage_1",
                "Todo lo que une mi taumaturgia con la tecnología de otros mods está reunido en las pestañas de este libro, una por cada mod o familia de aparatos.");
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, JsonObject json) {
        return ContentJson.save(output, cache, "data/" + MOD + "/" + path + ".json", json);
    }

    private static JsonObject header() {
        JsonObject json = ContentJson.research(
                HEADER,
                List.of("thaumaturge:first_steps"),
                0, 0,
                "thaumaturge:mechanism_complex",
                List.of(),
                1,
                ContentJson.aspects("thaumaturge:machina", 4, "thaumaturge:potentia", 3),
                1);
        json.addProperty("category", CATEGORY_ID);
        return json;
    }
}
