package nadiendev.ThaumaturgeTechCompats.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.registry.CreativeSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * One Thaumonomicon category per creative sub-tab, with the same id, name and order as {@link CreativeSection}.
 * Sections without any research entry (Edible Items) get no category, so no empty tab is shown. Hephaestus Tools
 * (the extra metals) has its own category, like the 26.1.2 branch.
 * Icons are placeholders taken from existing textures.
 */
public final class ResearchCategories {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String OVERLAY = "thaumaturge:textures/gui/gui_research_back_over.png";
    private static final String FEATURE = MOD + ":feature_enabled";
    /** Thaumaturge's own categories use indexes 0..6. */
    private static final int FIRST_INDEX = 7;

    private enum Style {
        AVARITIA("thaumaturge:textures/gui/gui_research_back_6_smooth.png"),
        TECH("thaumaturge:textures/gui/gui_research_back_3_smooth.png"),
        PURITY("thaumaturge:textures/gui/gui_research_back_4_smooth.png");

        private final String background;

        Style(String background) {
            this.background = background;
        }
    }

    public record Category(CreativeSection section, String icon, Style style, String modId, String feature) {
        public String id() {
            return MOD + ":" + section.id();
        }

        public String path() {
            return section.id();
        }

        public String langKey() {
            return "research_category." + MOD + "." + section.id();
        }
    }

    private static final Map<CreativeSection, Category> CATEGORIES = build();

    private ResearchCategories() {
    }

    private static Map<CreativeSection, Category> build() {
        Map<CreativeSection, Category> map = new EnumMap<>(CreativeSection.class);
        put(map, CreativeSection.CORE, "research/cat_avaritia.png", Style.AVARITIA, null, null);
        put(map, CreativeSection.PEARLS, "item/netherite_star_crucifix.png", Style.AVARITIA, null, null);
        put(map, CreativeSection.CREATE, "item/seal_create.png", Style.TECH, "create", null);
        put(map, CreativeSection.ORITECH, "block/oritech_thaumium_speed_addon_on.png", Style.TECH, "oritech", null);
        put(map, CreativeSection.MEKANISM, "item/energized_thaumium_pickaxe.png", Style.TECH, "mekanism", null);
        put(map, CreativeSection.FOREGOING, "item/magical_infinity_drill.png", Style.TECH, "industrialforegoing", null);
        put(map, CreativeSection.FTB, "item/thaumium_brush.png", Style.TECH, "ftbunearthed", null);
        put(map, CreativeSection.SOLAR_FLUX, "research/cat_solarflux.png", Style.TECH, "solarflux", null);
        put(map, CreativeSection.SOPHISTICATED, "item/thaumium_stack_upgrade.png", Style.TECH, "sophisticatedcore", null);
        put(map, CreativeSection.BOTANIA, "item/livingwood_wand_rod.png", Style.TECH, "botania", null);
        put(map, CreativeSection.VOID, "research/cat_tech.png", Style.TECH, "voidminersremastered", null);
        put(map, CreativeSection.NODES, "block/flux_jar_side_0.png", Style.TECH, null, "node_power");
        put(map, CreativeSection.FLUX_NETWORK, "block/flux_jar_top.png", Style.TECH, null, "flux_network_thaumium");
        put(map, CreativeSection.FLUX_GEAR, "research/cat_tech.png", Style.TECH, null, "flux_network_thaumium");
        put(map, CreativeSection.FOOD_TAINTED, "item/tainted_apple.png", Style.TECH, null, "tainted_food");
        put(map, CreativeSection.FOOD_FARMERS, "item/thaumium_knife.png", Style.TECH, "farmersdelight", null);
        put(map, CreativeSection.PURITY, "research/cat_purity.png", Style.PURITY, null, "purification_potion");
        put(map, CreativeSection.ENDERIO, "item/thaumium_capacitor.png", Style.TECH, "enderio", null);
        put(map, CreativeSection.METALS, "item/crimson_ingot.png", Style.TECH, null, null);
        put(map, CreativeSection.EXTRAS, "research/cat_tech.png", Style.TECH, null, null);
        return Collections.unmodifiableMap(map);
    }

    private static void put(Map<CreativeSection, Category> map, CreativeSection section, String icon, Style style, String modId, String feature) {
        map.put(section, new Category(section, MOD + ":textures/" + icon, style, modId, feature));
    }

    public static List<Category> all() {
        return new ArrayList<>(CATEGORIES.values());
    }

    public static Category of(CreativeSection section) {
        Category category = CATEGORIES.get(section);
        if (category == null) {
            throw new IllegalArgumentException("No research category for creative section " + section);
        }
        return category;
    }

    public static String id(CreativeSection section) {
        return of(section).id();
    }

    public static JsonObject json(Category category) {
        JsonObject json = new JsonObject();
        JsonArray conditions = new JsonArray();
        if (category.modId() != null) {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "neoforge:mod_loaded");
            condition.addProperty("modid", category.modId());
            conditions.add(condition);
        }
        if (category.feature() != null) {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", FEATURE);
            condition.addProperty("feature", category.feature());
            conditions.add(condition);
        }
        if (!conditions.isEmpty()) {
            json.add("neoforge:conditions", conditions);
        }
        json.addProperty("background", category.style().background);
        json.add("formula", formula(category.style()));
        json.addProperty("icon", category.icon());
        json.addProperty("index", FIRST_INDEX + category.section().ordinal());
        json.addProperty("overlay_background", OVERLAY);
        return json;
    }

    private static JsonArray formula(Style style) {
        return switch (style) {
            case AVARITIA -> aspects(
                    MOD + ":terminus", 20,
                    "thaumaturge:alienis", 10,
                    "thaumaturge:desiderium", 10,
                    "thaumaturge:praecantatio", 5,
                    "thaumaturge:potentia", 5,
                    "thaumaturge:metallum", 5,
                    "thaumaturge:vitreus", 5,
                    "thaumaturge:permutatio", 5);
            case TECH -> aspects(
                    "thaumaturge:machina", 20,
                    "thaumaturge:potentia", 10,
                    "thaumaturge:motus", 5,
                    "thaumaturge:instrumentum", 5,
                    "thaumaturge:fabrico", 5,
                    "thaumaturge:metallum", 5);
            case PURITY -> aspects(
                    "thaumaturge:ordo", 20,
                    "thaumaturge:victus", 10,
                    "thaumaturge:lux", 10,
                    "thaumaturge:aqua", 5,
                    "thaumaturge:alkimia", 5,
                    "thaumaturge:vitium", 5);
        };
    }

    private static JsonArray aspects(Object... pairs) {
        JsonArray array = new JsonArray();
        for (int i = 0; i < pairs.length; i += 2) {
            JsonObject entry = new JsonObject();
            entry.addProperty("aspect", (String) pairs[i]);
            entry.addProperty("amount", (Integer) pairs[i + 1]);
            array.add(entry);
        }
        return array;
    }
}
