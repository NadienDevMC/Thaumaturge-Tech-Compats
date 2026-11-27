package nadiendev.ThaumaturgeTechCompats.metals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Base aspects of the four new metals (ingot, nugget, block, native cluster, wand parts, molten bucket) and of the
 * Avaritia molten buckets and jars of this mod. Written to {@code thaumaturge:data_maps/item/base_aspects.json}.
 * Scale: ingot = 1 unit, nugget = 1/9 (rounded up to 1 for the themed aspects), block = 9, cluster = 2 ingots,
 * inert cap = 5 nuggets, inert rod = 3 ingots, bucket = 10 ingots (1000 mB at 90 mB per ingot, minus a loss).
 * Only uses Gson so a tool can run it outside Minecraft.
 */
public final class ExtraMetalAspects {
    private static final String MOD = "thaumaturgetechcompats";
    private static final String TERMINUS = MOD + ":terminus";
    private static final int INGOT_METALLUM = 15;
    private static final int INGOT_THEME = 8;
    private static final int NUGGET_METALLUM = 2;
    private static final int NUGGET_THEME = 1;
    private static final int BUCKET_INGOTS = 10;
    private static final int ACTIVE_PART_BONUS = 10;

    /** Metal id, then its two theme aspects (without namespace). */
    private static final String[][] METALS = {
            {"rifting", "vacuos", "alienis"},
            {"fibric", "fabrico", "vitium"},
            {"salis", "praecantatio", "potentia"},
            {"crimson", "perditio", "humanus"}
    };

    /** Avaritia ingot aspects (same as in the Avaritia entries) for the molten buckets. */
    private static final Map<String, Object[]> AVARITIA_INGOTS = Map.of(
            "crystal_matrix", new Object[]{TERMINUS, 1, "thaumaturge:potentia", 8, "thaumaturge:vitreus", 32},
            "neutronium", new Object[]{TERMINUS, 2, "thaumaturge:metallum", 12, "thaumaturge:potentia", 12},
            "infinity", new Object[]{TERMINUS, 16, "thaumaturge:alienis", 64});

    private ExtraMetalAspects() {
    }

    public static void addTo(JsonObject values) {
        for (String[] metal : METALS) {
            String id = metal[0];
            String a = "thaumaturge:" + metal[1];
            String b = "thaumaturge:" + metal[2];
            String metallum = "thaumaturge:metallum";
            put(values, id + "_ingot", metallum, INGOT_METALLUM, a, INGOT_THEME, b, INGOT_THEME);
            put(values, id + "_nugget", metallum, NUGGET_METALLUM, a, NUGGET_THEME, b, NUGGET_THEME);
            put(values, id + "_block", metallum, INGOT_METALLUM * 9, a, INGOT_THEME * 9, b, INGOT_THEME * 9);
            put(values, id + "_native_cluster", metallum, INGOT_METALLUM * 2, a, INGOT_THEME * 2, b, INGOT_THEME * 2);
            put(values, id + "_wand_cap_inert", metallum, NUGGET_METALLUM * 5, a, NUGGET_THEME * 5, b, NUGGET_THEME * 5);
            put(values, id + "_wand_rod_inert", metallum, INGOT_METALLUM * 3, a, INGOT_THEME * 3, b, INGOT_THEME * 3);
            put(values, id + "_wand_cap", metallum, NUGGET_METALLUM * 5, a, NUGGET_THEME * 5, b, NUGGET_THEME * 5,
                    "thaumaturge:praecantatio", ACTIVE_PART_BONUS);
            put(values, id + "_wand_rod", metallum, INGOT_METALLUM * 3 + INGOT_METALLUM * 2, a, INGOT_THEME * 5, b, INGOT_THEME * 5,
                    "thaumaturge:praecantatio", ACTIVE_PART_BONUS);
            put(values, "molten_" + id + "_bucket", metallum, INGOT_METALLUM * BUCKET_INGOTS,
                    a, INGOT_THEME * BUCKET_INGOTS, b, INGOT_THEME * BUCKET_INGOTS);
        }
        for (Map.Entry<String, Object[]> ingot : AVARITIA_INGOTS.entrySet()) {
            Object[] pairs = ingot.getValue().clone();
            for (int i = 1; i < pairs.length; i += 2) {
                pairs[i] = (Integer) pairs[i] * BUCKET_INGOTS;
            }
            put(values, "molten_" + ingot.getKey() + "_bucket", pairs);
        }
        // Jars: the ingots of the infusion (x3, the infusion keeps 75%), cumulative with the jar used as catalyst.
        put(values, "matrix_jar", TERMINUS, 3, "thaumaturge:potentia", 24, "thaumaturge:vitreus", 96);
        put(values, "neutronium_jar", TERMINUS, 9, "thaumaturge:metallum", 36, "thaumaturge:potentia", 60,
                "thaumaturge:vitreus", 96);
        put(values, "infinity_jar", TERMINUS, 57, "thaumaturge:metallum", 36, "thaumaturge:potentia", 60,
                "thaumaturge:vitreus", 96, "thaumaturge:alienis", 192);
    }

    private static void put(JsonObject values, String item, Object... pairs) {
        JsonArray array = new JsonArray();
        for (int i = 0; i < pairs.length; i += 2) {
            JsonObject entry = new JsonObject();
            entry.addProperty("amount", (Integer) pairs[i + 1]);
            entry.addProperty("aspect", (String) pairs[i]);
            array.add(entry);
        }
        values.add(MOD + ":" + item, array);
    }
}
