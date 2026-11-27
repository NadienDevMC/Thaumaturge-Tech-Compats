package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import nadiendev.ThaumaturgeTechCompats.compats.common.TechCategory;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Research entries, recipes and loot tables of the Solar Generation panels. */
public class SolarGenerationContent implements DataProvider {
    public static final String ASPECT_SOL = ThaumaturgeTechCompats.MOD_ID + ":sol";
    public static final String ROOT = "solargeneration_alchemical";
    public static final String BRASS = "solargeneration_brass";
    public static final String THAUMIUM = "solargeneration_thaumium";
    public static final String VOID = "solargeneration_void";
    public static final String BRASS_ANDESITE = "solargeneration_brass_andesite";
    public static final String THAUMIUM_ZINC = "solargeneration_thaumium_zinc";
    public static final String ALCHEMICAL_COBALT = "solargeneration_alchemical_cobalt";
    public static final String VOID_URANIUM = "solargeneration_void_uranium";

    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String SG = SolarGenerationIntegration.MOD_ID;
    private static final String RECIPES = "compat/solargeneration/";
    private static final String CELL = SG + ":photovoltaic_cell";
    private static final String CORE = SG + ":solar_core_advanced";

    private final PackOutput output;

    public SolarGenerationContent(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        tasks.add(save(cache, "thaumaturge/research_entry/" + ROOT, entry(ROOT,
                List.of(TechCategory.HEADER_ID), 13, 2, panel(SolarPanelType.ALCHEMICAL), RECIPES + "alchemical_solar_panel",
                "thaumaturge:alchemy", 1, 1,
                ContentJson.aspects("thaumaturge:lux", 6, "thaumaturge:alkimia", 5, ASPECT_SOL, 4), 1)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + BRASS, entry(BRASS,
                List.of(MOD + ":" + ROOT, "thaumaturge:metallurgy@1"), 13, 4, panel(SolarPanelType.BRASS), RECIPES + "brass_solar_panel",
                "thaumaturge:alchemy", 1, 2,
                ContentJson.aspects("thaumaturge:lux", 8, "thaumaturge:metallum", 6, ASPECT_SOL, 4), 2)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + THAUMIUM, entry(THAUMIUM,
                List.of(MOD + ":" + BRASS, "thaumaturge:base_artifice", "thaumaturge:metallurgy@2"), 13, 6, panel(SolarPanelType.THAUMIUM), RECIPES + "thaumium_generation_solar_panel",
                "thaumaturge:artifice", 2, 2,
                ContentJson.aspects("thaumaturge:lux", 10, "thaumaturge:potentia", 8, ASPECT_SOL, 6), 2)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + VOID, entry(VOID,
                List.of(MOD + ":" + THAUMIUM, "thaumaturge:base_artifice", "thaumaturge:base_eldritch"), 13, 8, panel(SolarPanelType.VOID_METAL), RECIPES + "void_metal_generation_solar_panel",
                "thaumaturge:eldritch", 2, 3,
                ContentJson.aspects("thaumaturge:lux", 12, "thaumaturge:vacuos", 8, "thaumaturge:potentia", 8, ASPECT_SOL, 8), 3)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + BRASS_ANDESITE, entry(BRASS_ANDESITE,
                List.of(MOD + ":" + BRASS), 14, 4, panel(SolarPanelType.BRASS_ANDESITE), RECIPES + "brass_andesite_solar_panel",
                "thaumaturge:alchemy", 1, 2,
                ContentJson.aspects("thaumaturge:lux", 6, "thaumaturge:machina", 6, ASPECT_SOL, 4), 2)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + THAUMIUM_ZINC, entry(THAUMIUM_ZINC,
                List.of(MOD + ":" + THAUMIUM), 14, 6, panel(SolarPanelType.THAUMIUM_ZINC), RECIPES + "thaumium_zinc_solar_panel",
                "thaumaturge:artifice", 2, 2,
                ContentJson.aspects("thaumaturge:lux", 8, "thaumaturge:metallum", 8, ASPECT_SOL, 5), 2)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + ALCHEMICAL_COBALT, entry(ALCHEMICAL_COBALT,
                List.of(MOD + ":" + ROOT, "thaumaturge:metallurgy@1"), 14, 2, panel(SolarPanelType.ALCHEMICAL_COBALT), RECIPES + "alchemical_cobalt_solar_panel",
                "thaumaturge:alchemy", 1, 2,
                ContentJson.aspects("thaumaturge:lux", 6, "thaumaturge:metallum", 6, ASPECT_SOL, 4), 2)));
        tasks.add(save(cache, "thaumaturge/research_entry/" + VOID_URANIUM, entry(VOID_URANIUM,
                List.of(MOD + ":" + VOID), 14, 8, panel(SolarPanelType.VOID_URANIUM), RECIPES + "void_uranium_solar_panel",
                "thaumaturge:eldritch", 2, 4,
                ContentJson.aspects("thaumaturge:lux", 12, "thaumaturge:vacuos", 10, "thaumaturge:potentia", 10, ASPECT_SOL, 8), 3)));
        tasks.add(save(cache, "recipe/" + RECIPES + "alchemical_solar_panel", alchemicalRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "brass_solar_panel", brassRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "thaumium_generation_solar_panel", thaumiumRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "void_metal_generation_solar_panel", voidRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "brass_andesite_solar_panel", brassAndesiteRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "thaumium_zinc_solar_panel", thaumiumZincRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "alchemical_cobalt_solar_panel", alchemicalCobaltRecipe()));
        tasks.add(save(cache, "recipe/" + RECIPES + "void_uranium_solar_panel", voidUraniumRecipe()));
        for (SolarPanelType type : SolarPanelType.values()) {
            tasks.add(save(cache, "loot_table/blocks/" + type.id(), lootTable(type)));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats solar generation";
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, JsonObject json) {
        return ContentJson.save(output, cache, "data/" + MOD + "/" + path + ".json", json);
    }

    private static String panel(SolarPanelType type) {
        return MOD + ":" + type.id();
    }

    private static JsonObject modLoaded(String modId) {
        return CompatJson.modLoaded(modId);
    }

    private static JsonObject anyOf(String... mods) {
        JsonArray values = new JsonArray();
        for (String mod : mods) {
            values.add(modLoaded(mod));
        }
        JsonObject condition = new JsonObject();
        condition.addProperty("type", "neoforge:or");
        condition.add("values", values);
        return condition;
    }

    private static JsonObject tag(String id) {
        JsonObject json = new JsonObject();
        json.addProperty("tag", id);
        return json;
    }

    private static JsonArray conditions(String... features) {
        JsonArray array = CompatJson.conditions(SG);
        for (String feature : features) {
            array.add(CompatJson.feature(feature));
        }
        return array;
    }

    /** Toggles an entry depends on: its own and those of the panels it is built from. */
    private static String[] featureOf(String entry) {
        return switch (entry) {
            case ROOT -> new String[]{CompatToggles.SOLAR_ALCHEMICAL};
            case BRASS -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_BRASS};
            case THAUMIUM -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_BRASS, CompatToggles.SOLAR_THAUMIUM};
            case VOID -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_BRASS, CompatToggles.SOLAR_THAUMIUM, CompatToggles.SOLAR_VOID_METAL};
            case BRASS_ANDESITE -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_BRASS, CompatToggles.SOLAR_BRASS_ANDESITE};
            case THAUMIUM_ZINC -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_BRASS, CompatToggles.SOLAR_THAUMIUM, CompatToggles.SOLAR_THAUMIUM_ZINC};
            case ALCHEMICAL_COBALT -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_COBALT};
            case VOID_URANIUM -> new String[]{CompatToggles.SOLAR_ALCHEMICAL, CompatToggles.SOLAR_ALCHEMICAL_BRASS, CompatToggles.SOLAR_THAUMIUM, CompatToggles.SOLAR_VOID_METAL, CompatToggles.SOLAR_VOID_URANIUM};
            default -> new String[0];
        };
    }

    /** Feature conditions of the entry plus the mod conditions of its panel. */
    private static JsonArray entryConditions(String entry) {
        JsonArray array = conditions(featureOf(entry));
        switch (entry) {
            case BRASS_ANDESITE -> array.add(modLoaded("create"));
            case THAUMIUM_ZINC -> array.add(anyOf("create", "alltheores"));
            case ALCHEMICAL_COBALT -> array.add(modLoaded("hephaestus"));
            case VOID_URANIUM -> array.add(anyOf("alltheores", "mekanism"));
            default -> {
            }
        }
        return array;
    }

    private static JsonObject entry(String name, List<String> parents, int column, int row, String icon, String recipe, String knowledgeCategory,
                                    int theory, int warp, JsonArray noteAspects, int complexity) {
        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", entryConditions(name));
        json.addProperty("category", TechCategory.CATEGORY_ID);
        json.addProperty("name", "research." + MOD + "." + name + ".title");
        json.add("parents", ContentJson.strings(parents));
        json.addProperty("column", column);
        json.addProperty("row", row);
        json.add("meta", ContentJson.strings("round", "hidden"));
        json.add("icons", ContentJson.strings(icon));

        JsonArray stages = new JsonArray();
        JsonObject first = new JsonObject();
        first.addProperty("text", "research." + MOD + "." + name + ".stage_0");
        JsonObject knowledge = new JsonObject();
        knowledge.addProperty("type", "theory");
        knowledge.addProperty("category", knowledgeCategory);
        knowledge.addProperty("amount", theory);
        JsonArray required = new JsonArray();
        required.add(knowledge);
        first.add("required_knowledge", required);
        first.addProperty("warp", warp - 1);
        stages.add(first);

        JsonObject second = new JsonObject();
        second.addProperty("text", "research." + MOD + "." + name + ".stage_1");
        second.add("recipes", ContentJson.strings(MOD + ":" + recipe));
        stages.add(second);

        json.add("stages", stages);
        json.add("note_aspects", noteAspects);
        json.addProperty("complexity", complexity);
        return json;
    }

    private static JsonObject researchRef(String entry) {
        JsonObject research = new JsonObject();
        research.addProperty("entry", MOD + ":" + entry);
        return research;
    }

    private static JsonObject lootTable(SolarPanelType type) {
        JsonObject copy = new JsonObject();
        copy.addProperty("function", "minecraft:copy_components");
        copy.add("include", ContentJson.strings(SG + ":energy", "minecraft:custom_name"));
        copy.addProperty("source", "block_entity");
        JsonArray functions = new JsonArray();
        functions.add(copy);
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.add("functions", functions);
        entry.addProperty("name", panel(type));
        JsonObject survives = new JsonObject();
        survives.addProperty("condition", "minecraft:survives_explosion");
        JsonArray poolConditions = new JsonArray();
        poolConditions.add(survives);
        JsonArray entries = new JsonArray();
        entries.add(entry);
        JsonObject pool = new JsonObject();
        pool.addProperty("bonus_rolls", 0.0F);
        pool.add("conditions", poolConditions);
        pool.add("entries", entries);
        pool.addProperty("rolls", 1.0F);
        JsonArray pools = new JsonArray();
        pools.add(pool);
        JsonArray conditions = new JsonArray();
        conditions.add(modLoaded(SG));
        // panels that need other mods are not loaded when none of them is present
        if (type.anyOf().length == 1) {
            conditions.add(modLoaded(type.anyOf()[0]));
        } else if (type.anyOf().length > 1) {
            conditions.add(anyOf(type.anyOf()));
        }
        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", conditions);
        json.addProperty("type", "minecraft:block");
        json.add("pools", pools);
        json.addProperty("random_sequence", MOD + ":blocks/" + type.id());
        return json;
    }

    private static JsonObject alchemicalRecipe() {
        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", conditions(featureOf(ROOT)));
        json.addProperty("type", "thaumaturge:crucible");
        json.add("aspects", ContentJson.aspects(ASPECT_SOL, 20, "thaumaturge:machina", 10));
        json.add("catalyst", ContentJson.item(SG + ":solar_panel_advanced"));
        json.add("research", researchRef(ROOT));
        json.add("result", ContentJson.result(panel(SolarPanelType.ALCHEMICAL), 1));
        return json;
    }

    private static JsonObject brassRecipe() {
        JsonObject key = new JsonObject();
        key.add("p", ContentJson.item(CELL));
        key.add("b", ContentJson.item("thaumaturge:plate_brass"));
        key.add("s", ContentJson.item(panel(SolarPanelType.ALCHEMICAL)));
        JsonObject json = ContentJson.arcaneShaped(key, ContentJson.strings("ppp", "bsb", "bbb"), MOD + ":" + BRASS, 20, panel(SolarPanelType.BRASS));
        json.add("neoforge:conditions", entryConditions(BRASS));
        return json;
    }

    private static JsonObject thaumiumRecipe() {
        JsonArray components = new JsonArray();
        for (int i = 0; i < 3; i++) {
            components.add(ContentJson.item("thaumaturge:plate_thaumium"));
            components.add(ContentJson.item(CELL));
        }
        components.add(ContentJson.item("thaumaturge:plate_thaumium"));
        components.add(ContentJson.item("thaumaturge:mechanism_simple"));
        JsonObject json = ContentJson.infusion(
                ContentJson.aspects(ASPECT_SOL, 40, "thaumaturge:potentia", 20),
                ContentJson.item(panel(SolarPanelType.BRASS)),
                components, 3, MOD + ":" + THAUMIUM, panel(SolarPanelType.THAUMIUM));
        json.add("neoforge:conditions", entryConditions(THAUMIUM));
        return json;
    }

    private static JsonObject voidRecipe() {
        JsonArray components = new JsonArray();
        for (int i = 0; i < 3; i++) {
            components.add(ContentJson.item("thaumaturge:plate_void"));
            components.add(ContentJson.item(CORE));
        }
        components.add(ContentJson.item("thaumaturge:plate_void"));
        components.add(ContentJson.item("thaumaturge:mechanism_complex"));
        JsonObject json = ContentJson.infusion(
                ContentJson.aspects(ASPECT_SOL, 60, "thaumaturge:potentia", 40, "thaumaturge:vacuos", 20),
                ContentJson.item(panel(SolarPanelType.THAUMIUM)),
                components, 4, MOD + ":" + VOID, panel(SolarPanelType.VOID_METAL));
        json.add("neoforge:conditions", entryConditions(VOID));
        return json;
    }

    private static JsonObject brassAndesiteRecipe() {
        JsonObject key = new JsonObject();
        key.add("p", ContentJson.item(CELL));
        key.add("a", ContentJson.item("create:andesite_alloy"));
        key.add("b", ContentJson.item("create:brass_ingot"));
        key.add("s", ContentJson.item(panel(SolarPanelType.ALCHEMICAL)));
        JsonObject json = ContentJson.arcaneShaped(key, ContentJson.strings("ppp", "asa", "bbb"), MOD + ":" + BRASS_ANDESITE, 20, panel(SolarPanelType.BRASS_ANDESITE));
        json.add("neoforge:conditions", entryConditions(BRASS_ANDESITE));
        return json;
    }

    private static JsonObject thaumiumZincRecipe() {
        JsonObject key = new JsonObject();
        key.add("p", ContentJson.item(CELL));
        key.add("t", ContentJson.item("thaumaturge:plate_thaumium"));
        key.add("z", tag("c:ingots/zinc"));
        key.add("s", ContentJson.item(panel(SolarPanelType.ALCHEMICAL)));
        JsonObject json = ContentJson.arcaneShaped(key, ContentJson.strings("ptp", "zsz", "ttt"), MOD + ":" + THAUMIUM_ZINC, 30, panel(SolarPanelType.THAUMIUM_ZINC));
        json.add("neoforge:conditions", entryConditions(THAUMIUM_ZINC));
        return json;
    }

    private static JsonObject alchemicalCobaltRecipe() {
        JsonObject key = new JsonObject();
        key.add("p", ContentJson.item(CELL));
        key.add("c", tag("c:ingots/cobalt"));
        key.add("s", ContentJson.item(panel(SolarPanelType.ALCHEMICAL)));
        JsonObject json = ContentJson.arcaneShaped(key, ContentJson.strings("ppp", "csc", "ccc"), MOD + ":" + ALCHEMICAL_COBALT, 20, panel(SolarPanelType.ALCHEMICAL_COBALT));
        json.add("neoforge:conditions", entryConditions(ALCHEMICAL_COBALT));
        return json;
    }

    private static JsonObject voidUraniumRecipe() {
        JsonArray components = new JsonArray();
        for (int i = 0; i < 3; i++) {
            components.add(ContentJson.item("thaumaturge:plate_void"));
            components.add(tag("c:ingots/uranium"));
        }
        components.add(ContentJson.item(CORE));
        components.add(ContentJson.item("thaumaturge:mechanism_complex"));
        JsonObject json = ContentJson.infusion(
                ContentJson.aspects(ASPECT_SOL, 80, "thaumaturge:potentia", 60, "thaumaturge:vacuos", 40),
                ContentJson.item(panel(SolarPanelType.VOID_METAL)),
                components, 5, MOD + ":" + VOID_URANIUM, panel(SolarPanelType.VOID_URANIUM));
        json.add("neoforge:conditions", entryConditions(VOID_URANIUM));
        return json;
    }
}
