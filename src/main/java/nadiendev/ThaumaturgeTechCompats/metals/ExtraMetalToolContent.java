package nadiendev.ThaumaturgeTechCompats.metals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import nadiendev.ThaumaturgeTechCompats.compats.hephaestus.HephaestusSmelteryIntegration;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Hephaestus Tools data of the new metals: the tool material and the casting and melting of its parts with the
 * molten metal of Hephaestus Forge. Everything is data, so both mods stay optional.
 */
public class ExtraMetalToolContent implements DataProvider {
    public static final String TOOLS = "hephaestus_tools";

    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;

    /** Part name, mB of molten metal, casting and melting time. */
    private static final Object[][] PARTS = {
            {"adze_head", 180, 120}, {"broad_axe_head", 720, 240}, {"hammer_head", 720, 240},
            {"large_blade", 720, 240}, {"large_plate", 360, 160}, {"pick_head", 180, 120},
            {"small_axe_head", 180, 120}, {"small_blade", 180, 120}, {"tool_binding", 90, 100},
            {"tool_handle", 90, 100}, {"tough_binding", 270, 140}, {"tough_handle", 270, 140}
    };

    private final PackOutput output;

    public ExtraMetalToolContent(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (ExtraMetal metal : ExtraMetal.values()) {
            String id = metal.id();
            tasks.add(save(cache, "data/" + MOD + "/" + TOOLS + "/materials/" + id, material(metal)));
            for (Object[] part : PARTS) {
                String name = (String) part[0];
                int amount = (Integer) part[1];
                int time = (Integer) part[2];
                tasks.add(save(cache, "data/" + MOD + "/recipe/tools/casting/" + id + "/" + name, casting(metal, name, amount, time)));
                tasks.add(save(cache, "data/" + MOD + "/recipe/tools/melting/" + id + "/" + name, melting(metal, name, amount, time)));
            }
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats extra metals tool materials";
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, JsonObject json) {
        return CompatJson.save(output, cache, path + ".json", json);
    }

    private static JsonArray toolsLoaded() {
        JsonArray conditions = new JsonArray();
        conditions.add(CompatJson.modLoaded(TOOLS));
        return conditions;
    }

    private static JsonArray smelteryConditions() {
        JsonArray conditions = new JsonArray();
        conditions.add(CompatJson.modLoaded(HephaestusSmelteryIntegration.MOD_ID));
        conditions.add(CompatJson.modLoaded(TOOLS));
        conditions.add(CompatJson.feature(HephaestusSmelteryIntegration.TOGGLE));
        return conditions;
    }

    /** Head: durability, speed, damage, tier, enchantability. Handle multipliers. Binding durability. */
    private static JsonObject material(ExtraMetal metal) {
        double[] head;
        double[] handle;
        int binding;
        switch (metal) {
            case RIFTING -> { head = new double[]{8500, 6.0, 2.5, 5, 14}; handle = new double[]{0.08, 0.02, 0.04}; binding = 100; }
            case CRIMSON -> { head = new double[]{12500, 7.0, 3.0, 5, 15}; handle = new double[]{0.10, 0.03, 0.06}; binding = 160; }
            case FIBRIC -> { head = new double[]{45500, 8.0, 3.5, 3, 18}; handle = new double[]{0.12, 0.04, 0.08}; binding = 240; }
            default -> { head = new double[]{75900, 11.0, 5.0, 3, 25}; handle = new double[]{0.20, 0.08, 0.15}; binding = 550; }
        }
        JsonObject json = new JsonObject();
        json.add("neoforge:conditions", toolsLoaded());
        json.addProperty("id", MOD + ":" + metal.id());
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", MOD + ":" + metal.ingotName());
        json.add("ingredient", ingredient);
        json.addProperty("color", metal.color() & 0xFFFFFF);

        JsonObject headStats = new JsonObject();
        headStats.addProperty("durability", (int) head[0]);
        headStats.addProperty("mining_speed", head[1]);
        headStats.addProperty("attack_damage", head[2]);
        headStats.addProperty("attack_speed", 1.0);
        headStats.addProperty("tier", (int) head[3]);
        headStats.addProperty("enchantability", (int) head[4]);
        json.add("head_stats", headStats);

        JsonObject handleStats = new JsonObject();
        handleStats.addProperty("durability_mult", handle[0]);
        handleStats.addProperty("speed_mult", handle[1]);
        handleStats.addProperty("damage_mult", handle[2]);
        handleStats.addProperty("attack_speed_mult", 0.0);
        json.add("handle_stats", handleStats);

        JsonObject bindingStats = new JsonObject();
        bindingStats.addProperty("durability", binding);
        json.add("binding_stats", bindingStats);
        return json;
    }

    private static JsonObject fluid(ExtraMetal metal, int amount) {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);
        json.addProperty("id", MOD + ":molten_" + metal.id());
        return json;
    }

    private static JsonObject casting(ExtraMetal metal, String part, int amount, int time) {
        JsonObject cast = new JsonObject();
        cast.addProperty("item", TOOLS + ":" + part + "_cast");
        JsonObject json = CompatJson.recipe("hephaestus:casting_table", smelteryConditions());
        json.add("cast", cast);
        json.addProperty("cast_consumed", false);
        json.addProperty("cooling_time", time);
        json.addProperty("count", 1);
        json.add("fluid", fluid(metal, amount));
        json.addProperty("result", TOOLS + ":" + part);
        return json;
    }

    private static JsonObject melting(ExtraMetal metal, String part, int amount, int time) {
        JsonObject fuel = new JsonObject();
        fuel.addProperty("amount", 50);
        fuel.addProperty("id", "hephaestus:molten_blaze");
        JsonObject components = new JsonObject();
        components.addProperty(TOOLS + ":part_material", MOD + ":" + metal.id());
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("type", "neoforge:components");
        ingredient.add("components", components);
        ingredient.addProperty("items", TOOLS + ":" + part);
        ingredient.addProperty("strict", false);
        JsonObject json = CompatJson.recipe("hephaestus:melting", smelteryConditions());
        json.add("fuel", fuel);
        json.add("ingredient", ingredient);
        json.add("result", fluid(metal, amount));
        json.addProperty("temperature", metal.temperature());
        json.addProperty("time", time);
        return json;
    }
}
