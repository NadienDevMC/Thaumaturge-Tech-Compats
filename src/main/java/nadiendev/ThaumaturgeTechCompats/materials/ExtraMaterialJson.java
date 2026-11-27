package nadiendev.ThaumaturgeTechCompats.materials;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Every JSON file of the extra metals (assets and data), as relative output paths. Built with Gson only, so the
 * data providers and a standalone run write exactly the same files. Recipe ingredients are already in the 26.1
 * string form ("item:id" or "#tag").
 */
public final class ExtraMaterialJson {
    public static final String MOD = ExtraMaterial.MOD;
    public static final String HEPHAESTUS = "hephaestus";
    public static final String HEPHAESTUS_TOOLS = "hephaestus_tools";
    public static final String SMELTERY_TOGGLE = "hephaestus_smeltery";
    public static final String THAUMATURGE = "thaumaturge";
    public static final int COLUMN = 80;

    public static final int INGOT = 90;
    public static final int NUGGET = 10;
    public static final int BLOCK = 810;

    /** Native cluster to ingots in a furnace or blast furnace, the same numbers as the clusters of 1.21.1. */
    private static final int SMELTING_TIME = 200;
    private static final int BLASTING_TIME = 100;
    private static final int INGOTS_PER_CLUSTER = 2;
    private static final float CLUSTER_EXPERIENCE = 1.0F;

    /** Hephaestus Tools tool part, fluid amount and cooling time (the same numbers its own metals use). */
    private static final List<Part> PARTS = List.of(
            new Part("adze_head", 180, 120),
            new Part("bow_limb", 270, 140),
            new Part("bowstring", 90, 100),
            new Part("broad_axe_head", 720, 240),
            new Part("hammer_head", 720, 240),
            new Part("large_blade", 720, 240),
            new Part("large_plate", 360, 160),
            new Part("mace_head", 720, 240),
            new Part("pick_head", 180, 120),
            new Part("small_axe_head", 180, 120),
            new Part("small_blade", 180, 120),
            new Part("spear_head", 180, 120),
            new Part("tool_binding", 90, 100),
            new Part("tool_handle", 90, 100),
            new Part("tough_binding", 270, 140),
            new Part("tough_handle", 270, 140));

    private record Part(String name, int amount, int time) {
    }

    private ExtraMaterialJson() {
    }

    public static String item(String name) {
        return MOD + ":" + name;
    }

    public static Map<String, JsonElement> files() {
        Map<String, JsonElement> files = new LinkedHashMap<>();
        for (ExtraMaterial material : ExtraMaterial.values()) {
            assets(files, material);
            data(files, material);
        }
        parentTags(files);
        researchEntry(files);
        taintFibreShears(files);
        return files;
    }

    // ---------------------------------------------------------------- assets

    private static void assets(Map<String, JsonElement> files, ExtraMaterial m) {
        for (String name : List.of(m.ingotName(), m.nuggetName(), m.clusterName())) {
            flatItem(files, name, "minecraft:item/generated");
        }
        for (String name : List.of(m.capName(), m.inertCapName())) {
            flatItem(files, name, "minecraft:item/generated");
        }
        for (String name : List.of(m.rodName(), m.inertRodName())) {
            flatItem(files, name, "minecraft:item/handheld");
        }

        String block = m.blockName();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", MOD + ":block/" + block);
        JsonObject variants = new JsonObject();
        variants.add("", variant);
        JsonObject state = new JsonObject();
        state.add("variants", variants);
        files.put("assets/" + MOD + "/blockstates/" + block + ".json", state);
        files.put("assets/" + MOD + "/models/block/" + block + ".json",
                model("minecraft:block/cube_all", "all", MOD + ":block/" + block));
        JsonObject blockItem = new JsonObject();
        blockItem.addProperty("parent", MOD + ":block/" + block);
        files.put("assets/" + MOD + "/models/item/" + block + ".json", blockItem);
        files.put("assets/" + MOD + "/items/" + block + ".json", definition(MOD + ":item/" + block, null));

        // Molten fluid of the Hephaestus Forge: particle-only block model, tinted bucket
        String fluid = m.moltenName();
        String bucket = fluid + "_bucket";
        JsonObject fluidVariant = new JsonObject();
        fluidVariant.addProperty("model", MOD + ":block/" + fluid);
        JsonObject fluidVariants = new JsonObject();
        fluidVariants.add("", fluidVariant);
        JsonObject fluidState = new JsonObject();
        fluidState.add("variants", fluidVariants);
        files.put("assets/" + MOD + "/blockstates/" + fluid + ".json", fluidState);
        JsonObject fluidModel = new JsonObject();
        JsonObject particle = new JsonObject();
        particle.addProperty("particle", "hephaestus:fluid/liquid/molten_metal");
        fluidModel.add("textures", particle);
        files.put("assets/" + MOD + "/models/block/" + fluid + ".json", fluidModel);
        files.put("assets/" + MOD + "/models/item/" + bucket + ".json", model("minecraft:item/generated", "layer0", "minecraft:item/bucket"));
        files.put("assets/" + MOD + "/items/" + bucket + ".json", definition(MOD + ":item/" + bucket, m.rgb()));
    }

    private static void flatItem(Map<String, JsonElement> files, String name, String parent) {
        files.put("assets/" + MOD + "/models/item/" + name + ".json", model(parent, "layer0", MOD + ":item/" + name));
        files.put("assets/" + MOD + "/items/" + name + ".json", definition(MOD + ":item/" + name, null));
    }

    private static JsonObject model(String parent, String key, String texture) {
        JsonObject textures = new JsonObject();
        textures.addProperty(key, texture);
        JsonObject json = new JsonObject();
        json.addProperty("parent", parent);
        json.add("textures", textures);
        return json;
    }

    private static JsonObject definition(String model, Integer tint) {
        JsonObject inner = new JsonObject();
        inner.addProperty("type", "minecraft:model");
        inner.addProperty("model", model);
        if (tint != null) {
            JsonObject constant = new JsonObject();
            constant.addProperty("type", "minecraft:constant");
            constant.addProperty("value", tint);
            JsonArray tints = new JsonArray();
            tints.add(constant);
            inner.add("tints", tints);
        }
        JsonObject json = new JsonObject();
        json.add("model", inner);
        return json;
    }

    // ------------------------------------------------------------------ data

    private static void data(Map<String, JsonElement> files, ExtraMaterial m) {
        String id = m.id();
        String data = "data/" + MOD + "/";

        files.put(data + "loot_table/blocks/" + m.blockName() + ".json", blockLoot(m));

        files.put(data + "recipe/compat/extra_metals/" + m.ingotName() + "_from_nuggets.json",
                shaped(List.of("NNN", "NNN", "NNN"), "N", "#" + m.nuggetTag(), item(m.ingotName()), 1));
        files.put(data + "recipe/compat/extra_metals/" + m.nuggetName() + "_from_ingot.json",
                shapeless("#" + m.ingotTag(), item(m.nuggetName()), 9));
        files.put(data + "recipe/compat/extra_metals/" + m.blockName() + "_from_ingots.json",
                shaped(List.of("III", "III", "III"), "I", "#" + m.ingotTag(), item(m.blockName()), 1));
        files.put(data + "recipe/compat/extra_metals/" + m.ingotName() + "_from_block.json",
                shapeless("#" + m.blockTag(), item(m.ingotName()), 9));
        // The native cluster smelts into two ingots, like the clusters of Thaumaturge
        files.put(data + "recipe/compat/extra_metals/smelting/" + m.ingotName() + "_from_cluster.json",
                cooking("minecraft:smelting", m, SMELTING_TIME));
        files.put(data + "recipe/compat/extra_metals/blasting/" + m.ingotName() + "_from_cluster.json",
                cooking("minecraft:blasting", m, BLASTING_TIME));

        files.put("data/c/tags/item/ingots/" + id + ".json", tag(item(m.ingotName())));
        files.put("data/c/tags/item/nuggets/" + id + ".json", tag(item(m.nuggetName())));
        files.put("data/c/tags/item/storage_blocks/" + id + ".json", tag(item(m.blockName())));
        files.put("data/c/tags/block/storage_blocks/" + id + ".json", tag(item(m.blockName())));
        files.put("data/c/tags/fluid/" + m.moltenName() + ".json", optionalTag(item(m.moltenName())));

        smeltery(files, m);
        toolMaterial(files, m);
        toolParts(files, m);
        wandRecipes(files, m);
    }

    private static JsonObject blockLoot(ExtraMaterial m) {
        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:survives_explosion");
        JsonArray conditions = new JsonArray();
        conditions.add(condition);
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", item(m.blockName()));
        JsonArray entries = new JsonArray();
        entries.add(entry);
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
        json.addProperty("random_sequence", MOD + ":blocks/" + m.blockName());
        return json;
    }

    private static JsonObject shaped(List<String> pattern, String symbol, String ingredient, String result, int count) {
        JsonObject key = new JsonObject();
        key.addProperty(symbol, ingredient);
        JsonArray rows = new JsonArray();
        pattern.forEach(rows::add);
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.addProperty("category", "misc");
        json.add("key", key);
        json.add("pattern", rows);
        json.add("result", stack(result, count));
        return json;
    }

    private static JsonObject shapeless(String ingredient, String result, int count) {
        JsonArray ingredients = new JsonArray();
        ingredients.add(ingredient);
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");
        json.addProperty("category", "misc");
        json.add("ingredients", ingredients);
        json.add("result", stack(result, count));
        return json;
    }

    private static JsonObject cooking(String type, ExtraMaterial m, int time) {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("category", "misc");
        json.addProperty("cookingtime", time);
        json.addProperty("experience", CLUSTER_EXPERIENCE);
        json.addProperty("group", m.ingotName());
        json.addProperty("ingredient", item(m.clusterName()));
        json.add("result", stack(item(m.ingotName()), INGOTS_PER_CLUSTER));
        return json;
    }

    private static JsonObject stack(String id, int count) {
        JsonObject json = new JsonObject();
        json.addProperty("count", count);
        json.addProperty("id", id);
        return json;
    }

    private static JsonObject tag(String... values) {
        JsonArray array = new JsonArray();
        for (String value : values) {
            array.add(value);
        }
        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        json.add("values", array);
        return json;
    }

    private static JsonObject optionalTag(String id) {
        JsonObject entry = new JsonObject();
        entry.addProperty("id", id);
        entry.addProperty("required", false);
        JsonArray values = new JsonArray();
        values.add(entry);
        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        json.add("values", values);
        return json;
    }

    private static void parentTags(Map<String, JsonElement> files) {
        String[] ingots = new String[ExtraMaterial.values().length];
        String[] nuggets = new String[ingots.length];
        String[] blocks = new String[ingots.length];
        for (ExtraMaterial m : ExtraMaterial.values()) {
            ingots[m.ordinal()] = "#" + m.ingotTag();
            nuggets[m.ordinal()] = "#" + m.nuggetTag();
            blocks[m.ordinal()] = "#" + m.blockTag();
        }
        files.put("data/c/tags/item/ingots.json", tag(ingots));
        files.put("data/c/tags/item/nuggets.json", tag(nuggets));
        files.put("data/c/tags/item/storage_blocks.json", tag(blocks));
        files.put("data/c/tags/block/storage_blocks.json", tag(blocks));
    }

    // ------------------------------------------------------------- smeltery

    private static JsonArray smelteryConditions() {
        JsonArray conditions = new JsonArray();
        conditions.add(modLoaded(HEPHAESTUS));
        conditions.add(feature(SMELTERY_TOGGLE));
        return conditions;
    }

    private static JsonArray toolConditions() {
        JsonArray conditions = smelteryConditions();
        conditions.add(modLoaded(HEPHAESTUS_TOOLS));
        return conditions;
    }

    private static JsonObject modLoaded(String modId) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "neoforge:mod_loaded");
        json.addProperty("modid", modId);
        return json;
    }

    private static JsonObject feature(String id) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "thaumaturgetechcompats:feature_enabled");
        json.addProperty("feature", id);
        return json;
    }

    private static JsonObject recipe(String type, JsonArray conditions) {
        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", conditions);
        json.addProperty("type", type);
        return json;
    }

    private static JsonObject fluid(ExtraMaterial m, int amount) {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);
        json.addProperty("id", item(m.moltenName()));
        return json;
    }

    private static JsonObject melting(ExtraMaterial m, JsonArray conditions, JsonElement ingredient, String fuel, int amount, int time) {
        JsonObject fuelJson = new JsonObject();
        fuelJson.addProperty("amount", 50);
        fuelJson.addProperty("id", fuel);
        JsonObject json = recipe("hephaestus:melting", conditions);
        json.add("fuel", fuelJson);
        json.add("ingredient", ingredient);
        json.add("result", fluid(m, amount));
        json.addProperty("temperature", m.temperature());
        json.addProperty("time", time);
        return json;
    }

    private static JsonObject idOnly(String id) {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        return json;
    }

    private static void smeltery(Map<String, JsonElement> files, ExtraMaterial m) {
        String base = "data/" + MOD + "/recipe/smeltery/";
        String id = m.id();
        files.put(base + "melting/" + id + "/ingot.json",
                melting(m, smelteryConditions(), new com.google.gson.JsonPrimitive("#" + m.ingotTag()), "minecraft:lava", INGOT, 100));
        files.put(base + "melting/" + id + "/nugget.json",
                melting(m, smelteryConditions(), new com.google.gson.JsonPrimitive("#" + m.nuggetTag()), "minecraft:lava", NUGGET, 33));
        files.put(base + "melting/" + id + "/block.json",
                melting(m, smelteryConditions(), new com.google.gson.JsonPrimitive("#" + m.blockTag()), "minecraft:lava", BLOCK, 200));
        files.put(base + "casting/table/" + id + "/ingot_cast.json", table(m, "hephaestus:ingot_cast", INGOT, 100, item(m.ingotName())));
        files.put(base + "casting/table/" + id + "/nugget_cast.json", table(m, "hephaestus:nugget_cast", NUGGET, 33, item(m.nuggetName())));
        JsonObject basin = recipe("hephaestus:casting_basin", smelteryConditions());
        basin.addProperty("cooling_time", 200);
        basin.add("fluid", fluid(m, BLOCK));
        basin.add("result", idOnly(item(m.blockName())));
        files.put(base + "casting/basin/" + id + "/block_cast.json", basin);
    }

    private static JsonObject table(ExtraMaterial m, String cast, int amount, int cooling, String result) {
        JsonObject json = recipe("hephaestus:casting_table", smelteryConditions());
        json.addProperty("cast", cast);
        json.addProperty("cooling_time", cooling);
        json.add("fluid", fluid(m, amount));
        json.add("result", idOnly(result));
        return json;
    }

    // ------------------------------------------------------ hephaestus tools

    /**
     * Material file in the hephaestus_tools namespace on purpose: its casting table recipes name the material after
     * the molten fluid ("molten_x" gives hephaestus_tools:x), whatever the namespace of the fluid.
     */
    private static void toolMaterial(Map<String, JsonElement> files, ExtraMaterial m) {
        JsonArray conditions = new JsonArray();
        conditions.add(modLoaded(HEPHAESTUS_TOOLS));

        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", m.ingotTag());

        JsonObject head = new JsonObject();
        head.addProperty("durability", m.durability());
        head.addProperty("mining_speed", m.miningSpeed());
        head.addProperty("attack_damage", m.attackDamage());
        head.addProperty("attack_speed", 1.0F);
        head.addProperty("tier", m.tier());
        head.addProperty("enchantability", m.enchantability());
        JsonArray traits = new JsonArray();
        for (String trait : m.traits()) {
            JsonObject entry = new JsonObject();
            entry.addProperty("id", trait);
            traits.add(entry);
        }
        if (traits.size() > 0) {
            head.add("traits", traits);
        }

        JsonObject handle = new JsonObject();
        handle.addProperty("durability_mult", m.handleDurability());
        handle.addProperty("speed_mult", m.handleSpeed());
        handle.addProperty("damage_mult", m.handleDamage());
        handle.addProperty("attack_speed_mult", m.handleAttackSpeed());

        JsonObject binding = new JsonObject();
        binding.addProperty("durability", m.bindingDurability());

        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", conditions);
        json.addProperty("id", HEPHAESTUS_TOOLS + ":" + m.id());
        json.add("ingredient", ingredient);
        json.addProperty("color", m.rgb());
        json.add("head_stats", head);
        json.add("handle_stats", handle);
        json.add("binding_stats", binding);
        files.put("data/" + HEPHAESTUS_TOOLS + "/" + HEPHAESTUS_TOOLS + "/materials/" + m.id() + ".json", json);
    }

    private static void toolParts(Map<String, JsonElement> files, ExtraMaterial m) {
        String base = "data/" + MOD + "/recipe/hephaestus_tools/";
        for (Part part : PARTS) {
            JsonObject casting = recipe("hephaestus:casting_table", toolConditions());
            casting.addProperty("cast", HEPHAESTUS_TOOLS + ":" + part.name() + "_cast");
            casting.addProperty("cast_consumed", false);
            casting.addProperty("cooling_time", part.time());
            casting.add("fluid", fluid(m, part.amount()));
            casting.add("result", stack(HEPHAESTUS_TOOLS + ":" + part.name(), 1));
            files.put(base + "casting/" + m.id() + "/" + part.name() + ".json", casting);

            JsonObject components = new JsonObject();
            components.addProperty(HEPHAESTUS_TOOLS + ":part_material", HEPHAESTUS_TOOLS + ":" + m.id());
            JsonObject ingredient = new JsonObject();
            ingredient.addProperty("neoforge:ingredient_type", "neoforge:components");
            ingredient.add("components", components);
            ingredient.addProperty("items", HEPHAESTUS_TOOLS + ":" + part.name());
            ingredient.addProperty("strict", false);
            files.put(base + "melting/" + m.id() + "/" + part.name() + ".json",
                    melting(m, toolConditions(), ingredient, "hephaestus:molten_blaze", part.amount(), part.time()));
        }
    }

    // ----------------------------------------------------------------- wands

    private static String partRecipe(String folder, String name) {
        return MOD + ":" + folder + "/" + name;
    }

    private static JsonObject research() {
        JsonObject research = new JsonObject();
        research.addProperty("entry", MOD + ":" + ExtraMaterial.RESEARCH);
        return research;
    }

    private static JsonObject aspect(String id, int amount) {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);
        json.addProperty("aspect", id);
        return json;
    }

    private static void wandRecipes(Map<String, JsonElement> files, ExtraMaterial m) {
        String data = "data/" + MOD + "/recipe/";
        // Inert parts at the arcane workbench, like the inert caps of Thaumaturge
        files.put(data + "wand/part/" + m.inertRodName() + ".json",
                arcane(List.of("  I", " I ", "I  "), "I", "#" + m.ingotTag(), 40, item(m.inertRodName())));
        files.put(data + "wand/part/" + m.inertCapName() + ".json",
                arcane(List.of("NNN", "N N"), "N", "#" + m.nuggetTag(), 90, item(m.inertCapName())));
        // Awakened parts: infusion with the inert part as catalyst and four salis mundus
        files.put(data + "infusion/" + m.rodName() + ".json", awaken(m, m.inertRodName(), m.rodName()));
        files.put(data + "infusion/" + m.capName() + ".json", awaken(m, m.inertCapName(), m.capName()));
        if (m != ExtraMaterial.RIFTING) {
            files.put(data + "infusion/" + m.ingotName() + ".json", ingotInfusion(m));
        }
    }

    private static JsonObject arcane(List<String> pattern, String symbol, String ingredient, int vis, String result) {
        JsonObject key = new JsonObject();
        key.addProperty(symbol, ingredient);
        JsonArray rows = new JsonArray();
        pattern.forEach(rows::add);
        JsonObject json = new JsonObject();
        json.addProperty("type", "thaumaturge:arcane_workbench_shaped");
        json.add("crystals", new JsonArray());
        json.add("key", key);
        json.add("pattern", rows);
        json.add("research", research());
        json.add("result", stack(result, 1));
        json.addProperty("vis", vis);
        return json;
    }

    /** Fibric, Salis and Crimson ingots: infusion over a void metal ingot with the thematic items around it. */
    private static JsonObject ingotInfusion(ExtraMaterial m) {
        String around;
        int count;
        List<String> aspectIds;
        switch (m) {
            case FIBRIC -> { around = "thaumaturge:taint_fibre"; count = 4; aspectIds = List.of("fabrico", "vitium"); }
            case SALIS -> { around = "thaumaturge:salis_mundus"; count = 4; aspectIds = List.of("praecantatio", "potentia"); }
            default -> { around = "thaumaturge:crimson_blade"; count = 2; aspectIds = List.of("perditio", "humanus"); }
        }
        JsonArray aspects = new JsonArray();
        for (String id : aspectIds) {
            aspects.add(aspect("thaumaturge:" + id, m.infusionAspect()));
        }
        JsonArray components = new JsonArray();
        for (int i = 0; i < count; i++) {
            components.add(around);
        }
        JsonObject json = new JsonObject();
        json.addProperty("type", "thaumaturge:infusion");
        json.add("aspects", aspects);
        json.addProperty("catalyst", "thaumaturge:ingot_void");
        json.add("components", components);
        json.addProperty("instability", 2 * m.tier() - 2);
        json.add("research", research());
        json.add("result", stack(item(m.ingotName()), 1));
        return json;
    }

    private static JsonObject awaken(ExtraMaterial m, String catalyst, String result) {
        JsonArray aspects = new JsonArray();
        aspects.add(aspect("thaumaturge:potentia", m.infusionAspect()));
        aspects.add(aspect("thaumaturge:auram", m.infusionAspect()));
        for (String special : m.capAspects()) {
            aspects.add(aspect("thaumaturge:" + special, m.infusionAspect()));
        }
        JsonArray components = new JsonArray();
        for (int i = 0; i < 4; i++) {
            components.add("thaumaturge:salis_mundus");
        }
        JsonObject json = new JsonObject();
        json.addProperty("type", "thaumaturge:infusion");
        json.add("aspects", aspects);
        json.addProperty("catalyst", item(catalyst));
        json.add("components", components);
        json.addProperty("instability", 2 * m.tier() - 2);
        json.add("research", research());
        json.add("result", stack(item(result), 1));
        return json;
    }

    // -------------------------------------------------------------- research

    private static void researchEntry(Map<String, JsonElement> files) {
        JsonArray recipes = new JsonArray();
        recipes.add(partRecipe("crucible", "rifting_ingot"));
        for (ExtraMaterial m : ExtraMaterial.values()) {
            if (m != ExtraMaterial.RIFTING) {
                recipes.add(partRecipe("infusion", m.ingotName()));
            }
        }
        for (ExtraMaterial m : ExtraMaterial.values()) {
            recipes.add(partRecipe("wand/part", m.inertRodName()));
            recipes.add(partRecipe("wand/part", m.inertCapName()));
            recipes.add(partRecipe("infusion", m.rodName()));
            recipes.add(partRecipe("infusion", m.capName()));
        }
        for (ExtraMaterial m : ExtraMaterial.values()) {
            recipes.add("thaumaturge:wand/assembly/" + MOD + "_" + m.id() + "_" + MOD + "_" + m.id());
        }

        JsonArray parents = new JsonArray();
        parents.add(MOD + ":tech");
        parents.add("thaumaturge:cap_void");

        JsonArray meta = new JsonArray();
        meta.add("round");
        meta.add("hidden");
        JsonArray icons = new JsonArray();
        icons.add(item(ExtraMaterial.RIFTING.ingotName()));

        JsonObject knowledge = new JsonObject();
        knowledge.addProperty("type", "theory");
        knowledge.addProperty("category", "thaumaturge:auromancy");
        knowledge.addProperty("amount", 2);
        JsonArray required = new JsonArray();
        required.add(knowledge);

        String name = ExtraMaterial.RESEARCH;
        JsonObject first = new JsonObject();
        first.addProperty("text", "research." + MOD + "." + name + ".stage_0");
        first.add("required_knowledge", required);
        first.addProperty("warp", 1);
        JsonObject second = new JsonObject();
        second.addProperty("text", "research." + MOD + "." + name + ".stage_1");
        second.add("recipes", recipes);
        JsonArray stages = new JsonArray();
        stages.add(first);
        stages.add(second);

        JsonArray notes = new JsonArray();
        notes.add(aspect("thaumaturge:praecantatio", 6));
        notes.add(aspect("thaumaturge:metallum", 5));
        notes.add(aspect("thaumaturge:alienis", 4));

        JsonObject json = new JsonObject();
        json.addProperty("category", MOD + ":tech");
        json.addProperty("name", "research." + MOD + "." + name + ".title");
        json.add("parents", parents);
        json.addProperty("column", COLUMN);
        json.addProperty("row", 2);
        json.add("meta", meta);
        json.add("icons", icons);
        json.add("stages", stages);
        json.add("note_aspects", notes);
        json.addProperty("complexity", 3);
        files.put("data/" + MOD + "/thaumaturge/research_entry/" + name + ".json", json);

        // Rifting ingot: crucible with water, a void metal ingot as catalyst and 20 praecantatio plus 20 sol.
        // The trapped rift in an adjacent Rift Jar is checked by the crucible mixin.
        JsonArray aspects = new JsonArray();
        aspects.add(aspect("thaumaturge:praecantatio", 20));
        aspects.add(aspect(MOD + ":sol", 20));
        // Needs the Rift Jar, so it only exists while node power is enabled (as in 1.21.1)
        JsonArray riftConditions = new JsonArray();
        riftConditions.add(feature("node_power"));
        JsonObject crucible = recipe("thaumaturge:crucible", riftConditions);
        crucible.add("aspects", aspects);
        crucible.addProperty("catalyst", "thaumaturge:ingot_void");
        crucible.add("research", research());
        crucible.add("result", stack(item(ExtraMaterial.RIFTING.ingotName()), 1));
        files.put("data/" + MOD + "/recipe/crucible/rifting_ingot.json", crucible);
    }

    // ------------------------------------------------------ taint fibre shears

    private static void taintFibreShears(Map<String, JsonElement> files) {
        JsonObject stateCondition = new JsonObject();
        stateCondition.addProperty("condition", "minecraft:block_state_property");
        stateCondition.addProperty("block", "thaumaturge:taint_fibre");

        JsonObject toolPredicate = new JsonObject();
        toolPredicate.addProperty("items", "minecraft:shears");
        JsonObject toolCondition = new JsonObject();
        toolCondition.addProperty("condition", "minecraft:match_tool");
        toolCondition.add("predicate", toolPredicate);

        JsonArray conditions = new JsonArray();
        conditions.add(stateCondition);
        conditions.add(toolCondition);

        JsonArray loaded = new JsonArray();
        loaded.add(modLoaded(THAUMATURGE));

        JsonObject modifier = new JsonObject();
        modifier.add("neoforge:conditions", loaded);
        modifier.addProperty("type", "neoforge:add_table");
        modifier.add("conditions", conditions);
        modifier.addProperty("table", MOD + ":gameplay/taint_fibre_shears");
        files.put("data/" + MOD + "/loot_modifiers/taint_fibre_shears.json", modifier);

        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", "thaumaturge:taint_fibre");
        JsonArray poolEntries = new JsonArray();
        poolEntries.add(entry);
        JsonObject pool = new JsonObject();
        pool.addProperty("bonus_rolls", 0.0);
        pool.add("entries", poolEntries);
        pool.addProperty("rolls", 1.0);
        JsonArray pools = new JsonArray();
        pools.add(pool);
        JsonObject table = new JsonObject();
        table.addProperty("type", "minecraft:block");
        table.add("pools", pools);
        files.put("data/" + MOD + "/loot_table/gameplay/taint_fibre_shears.json", table);
    }
}
