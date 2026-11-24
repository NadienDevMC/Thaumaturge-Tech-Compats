package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.common.CompatJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HephaestusSmelteryContent implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String HEPHAESTUS = HephaestusSmelteryIntegration.MOD_ID;
    private static final int INGOT = 90;
    private static final int NUGGET = 10;
    private static final int BLOCK = 810;
    private static final int PLATE = 90;

    private final PackOutput output;

    public HephaestusSmelteryContent(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (MoltenMetal metal : MoltenMetal.values()) {
            String base = "data/" + MOD + "/recipe/smeltery/";
            String name = metal.id();
            tasks.add(CompatJson.save(output, cache, base + "melting/" + name + "/ingot.json", melting(metal, input(metal, "ingots", metal.ingot()), INGOT, 100)));
            tasks.add(CompatJson.save(output, cache, base + "melting/" + name + "/block.json", melting(metal, input(metal, "storage_blocks", metal.block()), BLOCK, 200)));
            tasks.add(CompatJson.save(output, cache, base + "melting/" + name + "/plate.json", melting(metal, metal.plate(), PLATE, 100)));
            tasks.add(CompatJson.save(output, cache, base + "casting/table/" + name + "/ingot_cast.json",
                    table(metal, "hephaestus:ingot_cast", INGOT, 100, metal.ingot())));
            tasks.add(CompatJson.save(output, cache, base + "casting/table/" + name + "/plate_cast.json",
                    table(metal, "hephaestus:plate_cast", PLATE, 100, metal.plate())));
            tasks.add(CompatJson.save(output, cache, base + "casting/basin/" + name + "/block_cast.json", basin(metal)));
            // Crystal Matrix has no nugget item
            if (metal.nugget() != null) {
                tasks.add(CompatJson.save(output, cache, base + "melting/" + name + "/nugget.json", melting(metal, input(metal, "nuggets", metal.nugget()), NUGGET, 33)));
                tasks.add(CompatJson.save(output, cache, base + "casting/table/" + name + "/nugget_cast.json",
                        table(metal, "hephaestus:nugget_cast", NUGGET, 33, metal.nugget())));
            }
            tasks.add(CompatJson.save(output, cache, "data/c/tags/fluid/" + metal.fluidName() + ".json", fluidTag(metal)));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats hephaestus smeltery";
    }

    private static JsonArray conditions() {
        JsonArray conditions = new JsonArray();
        conditions.add(CompatJson.modLoaded(HEPHAESTUS));
        conditions.add(CompatJson.modLoaded("avaritia"));
        conditions.add(CompatJson.feature(HephaestusSmelteryIntegration.TOGGLE));
        return conditions;
    }

    // Neutronium has c: tags for its ingot, nugget and block; the others use the item id
    private static String input(MoltenMetal metal, String kind, String item) {
        return metal == MoltenMetal.NEUTRONIUM ? "#c:" + kind + "/neutronium" : item;
    }

    private static JsonObject ingredient(String id) {
        JsonObject json = new JsonObject();
        if (id.startsWith("#")) {
            json.addProperty("tag", id.substring(1));
        } else {
            json.addProperty("item", id);
        }
        return json;
    }

    private static JsonObject fluid(MoltenMetal metal, int amount) {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);
        json.addProperty("id", MOD + ":" + metal.fluidName());
        return json;
    }

    private static JsonObject melting(MoltenMetal metal, String input, int amount, int time) {
        JsonObject fuel = new JsonObject();
        fuel.addProperty("amount", 50);
        fuel.addProperty("id", "minecraft:lava");
        JsonObject json = CompatJson.recipe("hephaestus:melting", conditions());
        json.add("fuel", fuel);
        json.add("ingredient", ingredient(input));
        json.add("result", fluid(metal, amount));
        json.addProperty("temperature", metal.temperature());
        json.addProperty("time", time);
        return json;
    }

    // 1.21.1 format: cast is an ingredient object, result is a plain item id
    private static JsonObject table(MoltenMetal metal, String cast, int amount, int cooling, String result) {
        JsonObject json = CompatJson.recipe("hephaestus:casting_table", conditions());
        json.add("cast", ingredient(cast));
        json.addProperty("cooling_time", cooling);
        json.add("fluid", fluid(metal, amount));
        json.addProperty("result", result);
        return json;
    }

    // The basin takes its fluid as "input" and its duration as "time"
    private static JsonObject basin(MoltenMetal metal) {
        JsonObject json = CompatJson.recipe("hephaestus:casting_basin", conditions());
        json.add("input", fluid(metal, BLOCK));
        json.addProperty("result", metal.block());
        json.addProperty("time", 200);
        return json;
    }

    private static JsonObject fluidTag(MoltenMetal metal) {
        JsonArray values = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("id", MOD + ":" + metal.fluidName());
        entry.addProperty("required", false);
        values.add(entry);
        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        json.add("values", values);
        return json;
    }
}
