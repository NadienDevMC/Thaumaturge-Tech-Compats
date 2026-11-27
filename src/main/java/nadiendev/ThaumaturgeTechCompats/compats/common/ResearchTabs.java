package nadiendev.ThaumaturgeTechCompats.compats.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.registry.CreativeSection;
import nadiendev.ThaumaturgeTechCompats.tiercontent.ContentJson;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * One Thaumonomicon tab per creative sub-tab, with the same names and order.
 *
 * <p>Thaumaturge lists add-on tabs in registry order (the ids sorted alphabetically) and ignores
 * their {@code index}, so every tab id starts with its two-digit position (creative-section order, no gaps).
 * Every research entry of this mod goes through {@link #place}: the entry generators keep their old
 * category, and the saver moves the entry to its tab and to the column and row of {@link #LAYOUT}.
 * Parents in other tabs still work: Thaumaturge resolves parents by id, not by tab.</p>
 *
 * <p>Edible Items has no research, so it gets no tab (an empty tab would still be shown).</p>
 */
public class ResearchTabs implements DataProvider {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String OVERLAY = "thaumaturge:textures/gui/gui_research_back_over.png";
    private static final String MOD_TEXTURES = MOD + ":textures/";

    public enum Tab {
        AVARITIA(CreativeSection.CORE, Style.AVARITIA, MOD_TEXTURES + "research/cat_avaritia.png"),
        PEARLS(CreativeSection.PEARLS, Style.AVARITIA, MOD_TEXTURES + "item/copper_star_crucifix.png",
                anyFeature("planetary_pearl_copper", "planetary_pearl_thaumium", "star_crucifix_copper")),
        SOLAR(CreativeSection.SOLAR, Style.TECH, MOD_TEXTURES + "research/cat_solarflux.png",
                CompatJson.modLoaded("solargeneration"), CompatJson.feature("solargeneration"), CompatJson.feature("solar_alchemical")),
        CREATE(CreativeSection.CREATE, Style.TECH, MOD_TEXTURES + "item/seal_create.png",
                CompatJson.modLoaded("create"), CompatJson.feature("create_tech")),
        ORITECH(CreativeSection.ORITECH, Style.TECH, MOD_TEXTURES + "item/brass_speed_addon.png",
                CompatJson.modLoaded("oritech"), CompatJson.feature("oritech")),
        MEKANISM(CreativeSection.MEKANISM, Style.TECH, MOD_TEXTURES + "item/energized_thaumium_pickaxe.png",
                CompatJson.modLoaded("mekanism"), CompatJson.feature("mekanism_energy_tools")),
        BOTANIA(CreativeSection.BOTANIA, Style.TECH, MOD_TEXTURES + "block/alchemical_brass_spreader_side.png",
                CompatJson.modLoaded("botania"), CompatJson.feature("botania")),
        VOID(CreativeSection.VOID, Style.TECH, "thaumaturge:textures/block/metal_void.png",
                CompatJson.modLoaded("voidminersremastered"), CompatJson.feature("node_power")),
        NODES(CreativeSection.NODES, Style.TECH, MOD_TEXTURES + "block/flux_jar_side_0.png",
                CompatJson.feature("node_power")),
        FLUX_NETWORK(CreativeSection.FLUX_NETWORK, Style.TECH, "thaumaturge:textures/research/r_fluxrift.png",
                CompatJson.feature("flux_network_thaumium")),
        FLUX_GEAR(CreativeSection.FLUX_GEAR, Style.TECH, "thaumaturge:textures/research/r_flux.png",
                CompatJson.feature("flux_network_thaumium")),
        FOOD_TAINTED(CreativeSection.FOOD_TAINTED, Style.TECH, MOD_TEXTURES + "item/tainted_apple.png",
                CompatJson.feature("tainted_food")),
        FOOD_FARMERS(CreativeSection.FOOD_FARMERS, Style.TECH, MOD_TEXTURES + "item/thaumium_knife.png",
                CompatJson.modLoaded("farmersdelight")),
        PURITY(CreativeSection.PURITY, Style.PURITY, MOD_TEXTURES + "research/cat_purity.png",
                CompatJson.feature("purification_potion")),
        ENDERIO(CreativeSection.ENDERIO, Style.TECH, MOD_TEXTURES + "item/thaumium_capacitor.png",
                CompatJson.modLoaded("enderio"), CompatJson.feature("enderio")),
        EXTRA_METALS(CreativeSection.EXTRA_METALS, Style.TECH, MOD_TEXTURES + "item/crimson_ingot.png"),
        EXTRAS(CreativeSection.EXTRAS, Style.TECH, MOD_TEXTURES + "research/cat_tech.png");

        private final CreativeSection section;
        private final Style style;
        private final String icon;
        private final JsonObject[] conditions;

        Tab(CreativeSection section, Style style, String icon, JsonObject... conditions) {
            this.section = section;
            this.style = style;
            this.icon = icon;
            this.conditions = conditions;
        }

        public CreativeSection section() {
            return section;
        }

        public String path() {
            // numbered by tab, not by section: sections without a tab (Edible Items) leave no gap
            return String.format(Locale.ROOT, "%02d_%s", ordinal() + 1, section.id());
        }

        public String id() {
            return MOD + ":" + path();
        }

        public String langKey() {
            return "research_category." + MOD + "." + path();
        }
    }

    private enum Style {
        AVARITIA("thaumaturge:textures/gui/gui_research_back_6_smooth.png",
                MOD + ":terminus", 20, "thaumaturge:alienis", 10, "thaumaturge:desiderium", 10,
                "thaumaturge:praecantatio", 5, "thaumaturge:potentia", 5, "thaumaturge:metallum", 5,
                "thaumaturge:vitreus", 5, "thaumaturge:permutatio", 5),
        TECH("thaumaturge:textures/gui/gui_research_back_3_smooth.png",
                "thaumaturge:machina", 20, "thaumaturge:potentia", 10, "thaumaturge:motus", 5,
                "thaumaturge:instrumentum", 5, "thaumaturge:fabrico", 5, "thaumaturge:metallum", 5),
        PURITY("thaumaturge:textures/gui/gui_research_back_4_smooth.png",
                "thaumaturge:ordo", 20, "thaumaturge:victus", 10, "thaumaturge:lux", 10,
                "thaumaturge:aqua", 5, "thaumaturge:alkimia", 5, "thaumaturge:vitium", 5);

        private final String background;
        private final Object[] formula;

        Style(String background, Object... formula) {
            this.background = background;
            this.formula = formula;
        }
    }

    public record Placement(Tab tab, int column, int row) {
    }

    /** Where every research entry of this mod sits. Each tab starts at 0,0; chains go down by two rows, side branches two columns to the right. */
    private static final Map<String, Placement> LAYOUT = new HashMap<>();

    static {
        at(Tab.AVARITIA, 0, 0, "extremely_primordial_pearl");
        at(Tab.AVARITIA, 2, 0, "akashic_record");
        at(Tab.AVARITIA, 4, 0, "infinity_wand");
        at(Tab.AVARITIA, 0, 2, "avaritia_scribing");
        at(Tab.AVARITIA, 2, 2, "avaritia_creative");
        // Avaritia machines, devices, jars and matrices share the Avaritia tab, one block of rows each
        at(Tab.AVARITIA, 0, 4, "avaritia_machine_tiers");

        at(Tab.AVARITIA, 0, 6, "avaritia_smelters");
        at(Tab.AVARITIA, 2, 6, "avaritia_stabilizers");
        at(Tab.AVARITIA, 4, 6, "avaritia_transducers");
        at(Tab.AVARITIA, 6, 6, "avaritia_furnaces");
        at(Tab.AVARITIA, 0, 8, "avaritia_brain_box");
        at(Tab.AVARITIA, 2, 8, "avaritia_workbench_charger");
        at(Tab.AVARITIA, 4, 8, "avaritia_golems");
        at(Tab.AVARITIA, 6, 8, "avaritia_foci");
        at(Tab.AVARITIA, 0, 10, "matrix_wand");
        at(Tab.AVARITIA, 2, 10, "hephaestus_smeltery");
        at(Tab.AVARITIA, 0, 12, "neutronium_wand");

        at(Tab.AVARITIA, 0, 14, "avaritia_jars");

        at(Tab.AVARITIA, 0, 16, "infusion_matrix_variants");
        at(Tab.AVARITIA, 2, 16, "infusion_stone_variants");
        at(Tab.AVARITIA, 4, 16, "infusion_altar_variants");
        at(Tab.AVARITIA, 0, 18, "salis_mundus_variants");

        at(Tab.PEARLS, 0, 0, "planetary_endest_pearl");
        at(Tab.PEARLS, 0, 2, "planetary_endest_pearl_apex");
        at(Tab.PEARLS, 2, 0, "star_crucifix");

        at(Tab.SOLAR, 0, 0, "solargeneration_alchemical");
        at(Tab.SOLAR, 0, 2, "solargeneration_brass");
        at(Tab.SOLAR, 0, 4, "solargeneration_thaumium");
        at(Tab.SOLAR, 0, 6, "solargeneration_void");
        at(Tab.SOLAR, 2, 0, "solargeneration_alchemical_cobalt");
        at(Tab.SOLAR, 2, 2, "solargeneration_brass_andesite");
        at(Tab.SOLAR, 2, 4, "solargeneration_thaumium_zinc");
        at(Tab.SOLAR, 2, 6, "solargeneration_void_uranium");

        at(Tab.CREATE, 0, 0, "create_seal");
        at(Tab.CREATE, 0, 2, "create_casing_brass");
        at(Tab.CREATE, 0, 4, "create_casing_thaumium");
        at(Tab.CREATE, 0, 6, "create_casing_void");
        at(Tab.CREATE, 2, 2, "create_thaummotor");

        at(Tab.ORITECH, 0, 0, "oritech_nodes");
        at(Tab.ORITECH, 2, 0, "oritech_brass_addons");
        at(Tab.ORITECH, 2, 2, "oritech_thaumium_addons");
        at(Tab.ORITECH, 2, 4, "oritech_void_addons");
        at(Tab.ORITECH, 4, 0, "oritech_unholy_intelligence");

        // ids of MekanismToolContent.entryName (mekanismtools_<tier>), the same as in 1.21.1
        at(Tab.MEKANISM, 0, 0, "mekanismtools_thaumium");
        at(Tab.MEKANISM, 0, 2, "mekanismtools_alchemical_brass");
        at(Tab.MEKANISM, 0, 4, "mekanismtools_void");

        at(Tab.BOTANIA, 0, 0, "botania_spreaders");
        at(Tab.BOTANIA, 2, 0, "botania_wand");

        at(Tab.VOID, 0, 0, "voidminers_magical_miner_thaumium");
        at(Tab.VOID, 0, 2, "voidminers_magical_miner_alchemical_brass");
        at(Tab.VOID, 0, 4, "voidminers_magical_miner_void_metal");
        at(Tab.VOID, 2, 0, "voidminers_magical_miner_upgrades_thaumium");
        at(Tab.VOID, 2, 2, "voidminers_magical_miner_upgrades_alchemical_brass");
        at(Tab.VOID, 2, 4, "voidminers_magical_miner_upgrades_void_metal");
        at(Tab.VOID, 4, 0, "voidminers_magical_solar_thaumium");
        at(Tab.VOID, 4, 2, "voidminers_magical_solar_alchemical_brass");
        at(Tab.VOID, 4, 4, "voidminers_magical_solar_void_metal");
        at(Tab.VOID, 6, 0, "voidminers_magical_solar_upgrades_thaumium");
        at(Tab.VOID, 6, 2, "voidminers_magical_solar_upgrades_alchemical_brass");
        at(Tab.VOID, 6, 4, "voidminers_magical_solar_upgrades_void_metal");

        at(Tab.NODES, 0, 0, "node_power_flux_jar");
        at(Tab.NODES, 0, 2, "node_power_rift_jar");
        at(Tab.NODES, 2, 0, "node_power_flux_jars_avaritia");

        at(Tab.FLUX_NETWORK, 0, 0, "flux_rift_extractor");
        at(Tab.FLUX_NETWORK, 0, 2, "flux_transmission_link");
        at(Tab.FLUX_NETWORK, 0, 4, "flux_transmission_link_alchemical_brass");
        at(Tab.FLUX_NETWORK, 0, 6, "flux_transmission_link_void_metal");
        at(Tab.FLUX_NETWORK, 2, 0, "flux_storage_core_thaumium");
        at(Tab.FLUX_NETWORK, 2, 2, "flux_storage_core_alchemical_brass");
        at(Tab.FLUX_NETWORK, 2, 4, "flux_storage_core_void_metal");
        at(Tab.FLUX_NETWORK, 4, 0, "flux_charger_thaumium");
        at(Tab.FLUX_NETWORK, 4, 2, "flux_charger_alchemical_brass");
        at(Tab.FLUX_NETWORK, 4, 4, "flux_charger_void_metal");

        at(Tab.FLUX_GEAR, 0, 0, "field_shield_thaumium");
        at(Tab.FLUX_GEAR, 0, 2, "field_shield_alchemical_brass");
        at(Tab.FLUX_GEAR, 0, 4, "field_shield_void_metal");
        at(Tab.FLUX_GEAR, 2, 0, "field_terraformer_thaumium");
        at(Tab.FLUX_GEAR, 2, 2, "field_terraformer_alchemical_brass");
        at(Tab.FLUX_GEAR, 2, 4, "field_terraformer_void_metal");
        at(Tab.FLUX_GEAR, 4, 0, "field_flight_thruster");
        at(Tab.FLUX_GEAR, 6, 0, "flux_portable_void_siphon");
        at(Tab.FLUX_GEAR, 6, 2, "flux_discharge_cannon");

        at(Tab.FOOD_TAINTED, 0, 0, "tainted_food");

        at(Tab.FOOD_FARMERS, 0, 0, "tainted_farmersdelight_food");
        at(Tab.FOOD_FARMERS, 2, 0, "farmersdelight_thaumium_knife");
        at(Tab.FOOD_FARMERS, 2, 2, "farmersdelight_alchemical_brass_knife");
        at(Tab.FOOD_FARMERS, 2, 4, "farmersdelight_void_metal_knife");
        at(Tab.FOOD_FARMERS, 2, 6, "farmersdelight_flux_knife");

        at(Tab.PURITY, 0, 0, "purity");
        at(Tab.PURITY, 0, 2, "purification_potion");
        at(Tab.PURITY, 0, 4, "biome_purification_potion");
        at(Tab.PURITY, 2, 4, "clarity_potion");
        at(Tab.PURITY, 2, 6, "absolution_potion");

        at(Tab.ENDERIO, 0, 0, "enderio_conduits");
        at(Tab.ENDERIO, 2, 0, "enderio_capacitors");
        at(Tab.ENDERIO, 2, 2, "enderio_capacitor_banks");
        at(Tab.ENDERIO, 4, 0, "enderio_wand_parts");

        at(Tab.EXTRA_METALS, 0, 0, "extra_metals");

        at(Tab.EXTRAS, 0, 0, TechCategory.HEADER);
        at(Tab.EXTRAS, 2, 0, "aura_converter_thaumium");
        at(Tab.EXTRAS, 2, 2, "aura_converter_brass");
        at(Tab.EXTRAS, 2, 4, "aura_converter_void");
        at(Tab.EXTRAS, 4, 0, "forcecraft_repair");
        at(Tab.EXTRAS, 6, 0, "projecte_emc");
        at(Tab.EXTRAS, 8, 0, "tech_essences");
    }

    private final PackOutput output;

    public ResearchTabs(PackOutput output) {
        this.output = output;
    }

    private static void at(Tab tab, int column, int row, String entry) {
        LAYOUT.put(entry, new Placement(tab, column, row));
    }

    private static JsonObject anyFeature(String... features) {
        JsonArray values = new JsonArray();
        for (String feature : features) {
            values.add(CompatJson.feature(feature));
        }
        JsonObject or = new JsonObject();
        or.addProperty("type", "neoforge:or");
        or.add("values", values);
        return or;
    }

    public static Placement placement(String entry) {
        return LAYOUT.get(entry);
    }

    /**
     * Moves a research entry of this mod to its tab and position. Entries missing from {@link #LAYOUT}
     * (compats whose data is not generated on this branch) go to Extras and keep their own column and row.
     */
    public static JsonObject place(String entry, JsonObject json) {
        if (!json.has("category") || !json.get("category").getAsString().startsWith(MOD + ":")) {
            return json;
        }
        JsonObject placed = json.deepCopy();
        Placement placement = LAYOUT.get(entry);
        if (placement == null) {
            placed.addProperty("category", Tab.EXTRAS.id());
            return placed;
        }
        placed.addProperty("category", placement.tab().id());
        placed.addProperty("column", placement.column());
        placed.addProperty("row", placement.row());
        return placed;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> tasks = new ArrayList<>();
        for (Tab tab : Tab.values()) {
            tasks.add(ContentJson.save(output, cache, "data/" + MOD + "/thaumaturge/research_category/" + tab.path() + ".json", category(tab)));
        }
        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Thaumaturge Tech Compats research tabs";
    }

    private static JsonObject category(Tab tab) {
        JsonObject json = new JsonObject();
        if (tab.conditions.length > 0) {
            JsonArray conditions = new JsonArray();
            for (JsonObject condition : tab.conditions) {
                conditions.add(condition.deepCopy());
            }
            json.add("neoforge:conditions", conditions);
        }
        json.addProperty("background", tab.style.background);
        json.add("formula", ContentJson.aspects(tab.style.formula));
        json.addProperty("icon", tab.icon);
        json.addProperty("index", 20 + tab.section.ordinal());
        json.addProperty("overlay_background", OVERLAY);
        return json;
    }

    public static void english(LanguageProvider lang) {
        for (Tab tab : Tab.values()) {
            lang.add(tab.langKey(), tab.section.english());
        }
    }

    public static void spanish(LanguageProvider lang) {
        for (Tab tab : Tab.values()) {
            lang.add(tab.langKey(), tab.section.spanish());
        }
    }
}
