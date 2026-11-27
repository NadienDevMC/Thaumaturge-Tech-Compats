package nadiendev.ThaumaturgeTechCompats.metals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import nadiendev.ThaumaturgeTechCompats.compats.common.TechCategory;
import nadiendev.ThaumaturgeTechCompats.compats.enderio.EnderIOJson;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Server data of the new metals: recipes, tags, loot tables. */
public class ExtraMetalContent implements DataProvider {
    /** Research of the four metals (crucible, ingot infusions and wand parts), same entry as the 26.1.2 branch. */
    public static final String RESEARCH = ThaumaturgeTechCompats.MOD_ID + ":" + ExtraMetal.RESEARCH;
    public static final String ASPECT_SOL = ThaumaturgeTechCompats.MOD_ID + ":sol";

    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String RECIPES = "data/" + MOD + "/recipe/metals/";
    private static final String VOID_INGOT = "thaumaturge:ingot_void";
    private static final String SALIS_MUNDUS = "thaumaturge:salis_mundus";
    private static final int SMELTING_TIME = 200;
    private static final int BLASTING_TIME = 100;
    private static final int INGOTS_PER_CLUSTER = 2;
    private static final float EXPERIENCE = 1.0F;
    /** Order of the research recipe list, like the 26.1.2 branch: Rifting, Fibric, Salis, Crimson. */
    private static final List<ExtraMetal> DISPLAY = List.of(ExtraMetal.RIFTING, ExtraMetal.FIBRIC, ExtraMetal.SALIS, ExtraMetal.CRIMSON);

    private final PackOutput output;

    public ExtraMetalContent(PackOutput output) {
        this.output = output;
    }

    public static String item(String name) {
        return MOD + ":" + name;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (ExtraMetal metal : ExtraMetal.values()) {
            String id = metal.id();
            tasks.add(save(cache, RECIPES + id + "_ingot_from_nuggets", ninePack(metal.nuggetName(), metal.ingotName())));
            tasks.add(save(cache, RECIPES + id + "_nuggets_from_ingot", unpack(metal.ingotName(), metal.nuggetName())));
            tasks.add(save(cache, RECIPES + id + "_block", ninePack(metal.ingotName(), metal.blockName())));
            tasks.add(save(cache, RECIPES + id + "_ingots_from_block", unpack(metal.blockName(), metal.ingotName())));
            tasks.add(save(cache, RECIPES + "smelting/" + id + "_ingot_from_cluster", cooking("minecraft:smelting", metal, SMELTING_TIME)));
            tasks.add(save(cache, RECIPES + "blasting/" + id + "_ingot_from_cluster", cooking("minecraft:blasting", metal, BLASTING_TIME)));
            tasks.add(save(cache, RECIPES + "wand/" + metal.inertCapName(), inertCap(metal)));
            tasks.add(save(cache, RECIPES + "wand/" + metal.inertRodName(), inertRod(metal)));
            tasks.add(save(cache, RECIPES + "wand/" + metal.capName(), cap(metal)));
            tasks.add(save(cache, RECIPES + "wand/" + metal.rodName(), rod(metal)));
            tasks.add(save(cache, "data/c/tags/item/ingots/" + id, tag(item(metal.ingotName()))));
            tasks.add(save(cache, "data/c/tags/item/nuggets/" + id, tag(item(metal.nuggetName()))));
            tasks.add(save(cache, "data/c/tags/item/storage_blocks/" + id, tag(item(metal.blockName()))));
            tasks.add(save(cache, "data/c/tags/block/storage_blocks/" + id, tag(item(metal.blockName()))));
            tasks.add(save(cache, "data/" + MOD + "/loot_table/blocks/" + metal.blockName(), blockLoot(metal)));
        }
        tasks.add(save(cache, RECIPES + "crucible/" + ExtraMetal.RIFTING.ingotName(), riftingIngot()));
        for (ExtraMetal metal : ExtraMetal.values()) {
            if (metal != ExtraMetal.RIFTING) {
                tasks.add(save(cache, RECIPES + "infusion/" + metal.ingotName(), ingotInfusion(metal)));
            }
        }
        tasks.add(save(cache, "data/c/tags/item/ingots", tag(ids(ExtraMetal::ingotName))));
        tasks.add(save(cache, "data/c/tags/item/nuggets", tag(ids(ExtraMetal::nuggetName))));
        tasks.add(save(cache, "data/c/tags/item/storage_blocks", tag(ids(ExtraMetal::blockName))));
        tasks.add(save(cache, "data/c/tags/block/storage_blocks", tag(ids(ExtraMetal::blockName))));
        tasks.add(save(cache, "data/" + MOD + "/thaumaturge/research_entry/" + ExtraMetal.RESEARCH, research()));
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats extra metals content";
    }

    /** Block ids that must be added to the pickaxe tag, and cluster ids for the Thaumaturge clusters tag. */
    public static List<String> blockIds() {
        return ids(ExtraMetal::blockName);
    }

    public static List<String> clusterIds() {
        return ids(ExtraMetal::clusterName);
    }

    private static List<String> ids(java.util.function.Function<ExtraMetal, String> name) {
        List<String> ids = new ArrayList<>();
        for (ExtraMetal metal : ExtraMetal.values()) {
            ids.add(item(name.apply(metal)));
        }
        return ids;
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, com.google.gson.JsonElement json) {
        String full = path.endsWith(".json") ? path : path + ".json";
        return CompatJson.save(output, cache, full, json);
    }

    private static JsonObject tag(String... values) {
        return tag(List.of(values));
    }

    private static JsonObject tag(List<String> values) {
        JsonArray array = new JsonArray();
        for (String value : values) {
            JsonObject entry = new JsonObject();
            entry.addProperty("id", value);
            entry.addProperty("required", false);
            array.add(entry);
        }
        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        json.add("values", array);
        return json;
    }

    private static JsonObject ninePack(String from, String to) {
        JsonObject key = new JsonObject();
        key.add("X", ContentJson.item(item(from)));
        JsonObject json = ContentJson.shaped("misc", key, ContentJson.strings("XXX", "XXX", "XXX"), item(to), 1);
        return json;
    }

    private static JsonObject unpack(String from, String to) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");
        json.addProperty("category", "misc");
        JsonArray ingredients = new JsonArray();
        ingredients.add(ContentJson.item(item(from)));
        json.add("ingredients", ingredients);
        json.add("result", ContentJson.result(item(to), 9));
        return json;
    }

    private static JsonObject cooking(String type, ExtraMetal metal, int time) {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("category", "misc");
        json.addProperty("cookingtime", time);
        json.addProperty("experience", EXPERIENCE);
        json.addProperty("group", metal.id() + "_ingot");
        json.add("ingredient", ContentJson.item(item(metal.clusterName())));
        json.add("result", ContentJson.result(item(metal.ingotName()), INGOTS_PER_CLUSTER));
        return json;
    }

    private static JsonObject inertCap(ExtraMetal metal) {
        JsonObject key = new JsonObject();
        key.add("N", tagIngredient("c:nuggets/" + metal.id()));
        return ContentJson.arcaneShaped(key, ContentJson.strings("NNN", "N N"), RESEARCH, 90, item(metal.inertCapName()));
    }

    private static JsonObject inertRod(ExtraMetal metal) {
        JsonObject key = new JsonObject();
        key.add("I", tagIngredient("c:ingots/" + metal.id()));
        return ContentJson.arcaneShaped(key, ContentJson.strings("  I", " I ", "I  "), RESEARCH, 40, item(metal.inertRodName()));
    }

    private static JsonObject cap(ExtraMetal metal) {
        return awaken(metal, metal.inertCapName(), metal.capName());
    }

    private static JsonObject rod(ExtraMetal metal) {
        return awaken(metal, metal.inertRodName(), metal.rodName());
    }

    /** Awakened part: infusion with the inert part as catalyst, four salis mundus, potentia, auram and the cap aspects. */
    private static JsonObject awaken(ExtraMetal metal, String catalyst, String result) {
        JsonArray aspects = ContentJson.aspects("thaumaturge:potentia", metal.infusionAspect(), "thaumaturge:auram", metal.infusionAspect());
        for (ResourceKey<IAspect> aspect : metal.capAspects()) {
            aspects.addAll(ContentJson.aspects(aspect.location().toString(), metal.infusionAspect()));
        }
        return ContentJson.infusion(aspects, ContentJson.item(item(catalyst)), repeat(SALIS_MUNDUS, 4),
                metal.instability(), RESEARCH, item(result));
    }

    private static JsonObject tagIngredient(String tag) {
        JsonObject json = new JsonObject();
        json.addProperty("tag", tag);
        return json;
    }

    private static JsonArray repeat(String id, int count) {
        JsonArray array = new JsonArray();
        for (int i = 0; i < count; i++) {
            array.add(ContentJson.item(id));
        }
        return array;
    }

    /**
     * Void metal ingot dissolved in a crucible with water, 20 praecantatio and 20 sol. The trapped rift condition
     * (a charged Rift Jar touching the crucible) is checked by the crucible mixin, since a recipe cannot express it.
     */
    private static JsonObject riftingIngot() {
        JsonArray conditions = new JsonArray();
        conditions.add(CompatJson.feature(CompatToggles.NODE_POWER));
        JsonObject json = CompatJson.recipe("thaumaturge:crucible", conditions);
        json.add("aspects", ContentJson.aspects("thaumaturge:praecantatio", 20, ASPECT_SOL, 20));
        json.add("catalyst", ContentJson.item(VOID_INGOT));
        JsonObject research = new JsonObject();
        research.addProperty("entry", RESEARCH);
        json.add("research", research);
        json.add("result", ContentJson.result(item(ExtraMetal.RIFTING.ingotName()), 1));
        return json;
    }

    /** Void metal ingot in the centre, the themed material around it. Crimson uses the Crimson Blade, which has no recipe. */
    private static JsonObject ingotInfusion(ExtraMetal metal) {
        int amount = metal.infusionAspect();
        JsonArray aspects;
        JsonArray components;
        switch (metal) {
            case FIBRIC -> {
                aspects = ContentJson.aspects("thaumaturge:fabrico", amount, "thaumaturge:vitium", amount);
                components = repeat("thaumaturge:taint_fibre", 4);
            }
            case SALIS -> {
                aspects = ContentJson.aspects("thaumaturge:praecantatio", amount, "thaumaturge:potentia", amount);
                components = repeat(SALIS_MUNDUS, 4);
            }
            default -> {
                aspects = ContentJson.aspects("thaumaturge:perditio", amount, "thaumaturge:humanus", amount);
                components = repeat("thaumaturge:crimson_blade", 2);
            }
        }
        return ContentJson.infusion(aspects, ContentJson.item(VOID_INGOT), components, metal.instability(), RESEARCH, item(metal.ingotName()));
    }

    /** Recipe id (namespace:path) of a recipe file written by this provider. */
    private static String recipeId(String path) {
        return MOD + ":metals/" + path;
    }

    /**
     * Research entry of the four metals: the Rifting crucible, the three ingot infusions, the inert and awakened wand
     * parts and the wand assemblies. ResearchLayout moves it to the Hephaestus Tools tab.
     */
    private static JsonObject research() {
        List<String> recipes = new ArrayList<>();
        recipes.add(recipeId("crucible/" + ExtraMetal.RIFTING.ingotName()));
        for (ExtraMetal metal : DISPLAY) {
            if (metal != ExtraMetal.RIFTING) {
                recipes.add(recipeId("infusion/" + metal.ingotName()));
            }
        }
        for (ExtraMetal metal : DISPLAY) {
            recipes.add(recipeId("wand/" + metal.inertRodName()));
            recipes.add(recipeId("wand/" + metal.inertCapName()));
            recipes.add(recipeId("wand/" + metal.rodName()));
            recipes.add(recipeId("wand/" + metal.capName()));
        }
        for (ExtraMetal metal : DISPLAY) {
            recipes.add("thaumaturge:wand/assembly/" + MOD + "_" + metal.id() + "_" + MOD + "_" + metal.id());
        }
        JsonObject json = EnderIOJson.entry(
                ExtraMetal.RESEARCH,
                new JsonArray(),
                List.of(TechCategory.HEADER_ID, "thaumaturge:cap_void"),
                0,
                item(ExtraMetal.RIFTING.ingotName()),
                recipes,
                EnderIOJson.CATEGORY_AUROMANCY, 2, 1,
                ContentJson.aspects("thaumaturge:praecantatio", 6, "thaumaturge:metallum", 5, "thaumaturge:alienis", 4),
                3);
        // No conditions: the metals exist without any other mod
        json.remove("neoforge:conditions");
        return json;
    }

    private static JsonObject blockLoot(ExtraMetal metal) {
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", item(metal.blockName()));
        JsonArray entries = new JsonArray();
        entries.add(entry);

        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:survives_explosion");
        JsonArray conditions = new JsonArray();
        conditions.add(condition);

        JsonObject pool = new JsonObject();
        pool.addProperty("bonus_rolls", 0.0);
        pool.add("conditions", conditions);
        pool.add("entries", entries);
        pool.addProperty("rolls", 1.0);
        JsonArray pools = new JsonArray();
        pools.add(pool);

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:block");
        json.add("pools", pools);
        json.addProperty("random_sequence", MOD + ":blocks/" + metal.blockName());
        return json;
    }
}
