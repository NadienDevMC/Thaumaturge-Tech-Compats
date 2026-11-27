package nadiendev.ThaumaturgeTechCompats.research;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nadiendev.ThaumaturgeTechCompats.registry.CreativeSection;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Category and position of every research entry in the Thaumonomicon, one category per creative sub-tab.
 * <p>
 * Layout rule inside a category: every chain of tiers (brass, thaumium, void...) is one column, columns go
 * 0, 2, 4... from left to right and tiers go down rows 0, 2, 4...; nothing overlaps. Parents in other
 * categories (the Tech header, the Extremely Primordial Pearl, Thaumaturge's own entries) are kept: the
 * format accepts parents from any category.
 * <p>
 * The content classes still build each entry; {@link #apply} rewrites category, column and row when the
 * entry is saved, so this table is the only place that decides where an entry lives.
 */
public final class ResearchLayout {
    public record Placement(CreativeSection section, int column, int row) {
        public String category() {
            return ResearchCategories.id(section);
        }
    }

    private static final Map<String, Placement> ENTRIES = build();

    private ResearchLayout() {
    }

    private static Map<String, Placement> build() {
        Map<String, Placement> map = new LinkedHashMap<>();
        // Avaritia: one tab, one block of rows per topic (core, machines, devices, jars, matrices)
        put(map, CreativeSection.CORE, "extremely_primordial_pearl", 0, 0);
        put(map, CreativeSection.CORE, "akashic_record", 2, 0);
        put(map, CreativeSection.CORE, "infinity_wand", 4, 0);
        put(map, CreativeSection.CORE, "avaritia_scribing", 0, 2);
        put(map, CreativeSection.CORE, "avaritia_creative", 2, 2);
        // machines
        put(map, CreativeSection.CORE, "avaritia_machine_tiers", 0, 4);
        // devices
        put(map, CreativeSection.CORE, "avaritia_smelters", 0, 6);
        put(map, CreativeSection.CORE, "avaritia_stabilizers", 2, 6);
        put(map, CreativeSection.CORE, "avaritia_transducers", 4, 6);
        put(map, CreativeSection.CORE, "avaritia_furnaces", 6, 6);
        put(map, CreativeSection.CORE, "avaritia_brain_box", 0, 8);
        put(map, CreativeSection.CORE, "avaritia_workbench_charger", 2, 8);
        put(map, CreativeSection.CORE, "avaritia_golems", 4, 8);
        put(map, CreativeSection.CORE, "avaritia_foci", 6, 8);
        put(map, CreativeSection.CORE, "matrix_wand", 0, 10);
        put(map, CreativeSection.CORE, "hephaestus_smeltery", 2, 10);
        put(map, CreativeSection.CORE, "neutronium_wand", 0, 12);
        // jars
        put(map, CreativeSection.CORE, "avaritia_jars", 0, 14);
        put(map, CreativeSection.CORE, "node_power_flux_jars_avaritia", 2, 14);
        // matrices
        put(map, CreativeSection.CORE, "infusion_matrix_variants", 0, 16);
        put(map, CreativeSection.CORE, "infusion_stone_variants", 2, 16);
        put(map, CreativeSection.CORE, "infusion_altar_variants", 4, 16);
        put(map, CreativeSection.CORE, "salis_mundus_variants", 6, 16);
        // Pearls & Cruc.
        put(map, CreativeSection.PEARLS, "planetary_endest_pearl", 0, 0);
        put(map, CreativeSection.PEARLS, "planetary_endest_pearl_apex", 0, 2);
        put(map, CreativeSection.PEARLS, "star_crucifix", 2, 0);
        // Create
        put(map, CreativeSection.CREATE, "create_seal", 0, 0);
        put(map, CreativeSection.CREATE, "create_casing_brass", 0, 2);
        put(map, CreativeSection.CREATE, "create_casing_thaumium", 0, 4);
        put(map, CreativeSection.CREATE, "create_casing_void", 0, 6);
        put(map, CreativeSection.CREATE, "create_thaummotor", 2, 2);
        // Oritech
        put(map, CreativeSection.ORITECH, "oritech_nodes", 0, 0);
        put(map, CreativeSection.ORITECH, "oritech_brass_addons", 2, 0);
        put(map, CreativeSection.ORITECH, "oritech_thaumium_addons", 2, 2);
        put(map, CreativeSection.ORITECH, "oritech_void_addons", 2, 4);
        put(map, CreativeSection.ORITECH, "oritech_unholy_intelligence", 4, 0);
        // Mekanism
        put(map, CreativeSection.MEKANISM, "mekanismtools_thaumium", 0, 0);
        put(map, CreativeSection.MEKANISM, "mekanismtools_alchemical_brass", 0, 2);
        put(map, CreativeSection.MEKANISM, "mekanismtools_void", 0, 4);
        // Industrial F.
        put(map, CreativeSection.FOREGOING, "industrialforegoing_addons_brass", 0, 0);
        put(map, CreativeSection.FOREGOING, "industrialforegoing_addons_thaumium", 0, 2);
        put(map, CreativeSection.FOREGOING, "industrialforegoing_addons_void", 0, 4);
        put(map, CreativeSection.FOREGOING, "magical_infinity_drill", 2, 0);
        put(map, CreativeSection.FOREGOING, "magical_laser_drill", 4, 0);
        put(map, CreativeSection.FOREGOING, "magical_ore_laser_base", 4, 2);
        put(map, CreativeSection.FOREGOING, "magical_fluid_laser_base", 4, 4);
        // FTB Unearthed
        put(map, CreativeSection.FTB, "ftbunearthed_brass_brush", 0, 0);
        put(map, CreativeSection.FTB, "ftbunearthed_thaumium_brush", 0, 2);
        put(map, CreativeSection.FTB, "ftbunearthed_void_brush", 0, 4);
        put(map, CreativeSection.FTB, "ftbunearthed_magical_thaumium", 2, 0);
        put(map, CreativeSection.FTB, "ftbunearthed_magical_alchemical_brass", 2, 2);
        put(map, CreativeSection.FTB, "ftbunearthed_magical_void_metal", 2, 4);
        // Solar Flux
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_alchemical", 0, 0);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_brass", 0, 2);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_thaumium", 0, 4);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_void", 0, 6);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_alchemical_cobalt", 2, 0);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_brass_andesite", 2, 2);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_thaumium_zinc", 2, 4);
        put(map, CreativeSection.SOLAR_FLUX, "solarflux_void_uranium", 2, 6);
        // Sophisticated
        put(map, CreativeSection.SOPHISTICATED, "sophisticated_stack_upgrades", 0, 0);
        put(map, CreativeSection.SOPHISTICATED, "sophisticated_arcane_upgrade", 2, 0);
        put(map, CreativeSection.SOPHISTICATED, "sophisticated_backpack_tiers", 4, 0);
        put(map, CreativeSection.SOPHISTICATED, "sophisticated_storage_tiers", 6, 0);
        // Botania
        put(map, CreativeSection.BOTANIA, "botania_spreaders", 0, 0);
        put(map, CreativeSection.BOTANIA, "botania_wand", 2, 0);
        // Void Miners
        put(map, CreativeSection.VOID, "voidminers_magical_miner_thaumium", 0, 0);
        put(map, CreativeSection.VOID, "voidminers_magical_miner_alchemical_brass", 0, 2);
        put(map, CreativeSection.VOID, "voidminers_magical_miner_void_metal", 0, 4);
        put(map, CreativeSection.VOID, "voidminers_magical_miner_upgrades_thaumium", 2, 0);
        put(map, CreativeSection.VOID, "voidminers_magical_miner_upgrades_alchemical_brass", 2, 2);
        put(map, CreativeSection.VOID, "voidminers_magical_miner_upgrades_void_metal", 2, 4);
        put(map, CreativeSection.VOID, "voidminers_magical_solar_thaumium", 4, 0);
        put(map, CreativeSection.VOID, "voidminers_magical_solar_alchemical_brass", 4, 2);
        put(map, CreativeSection.VOID, "voidminers_magical_solar_void_metal", 4, 4);
        put(map, CreativeSection.VOID, "voidminers_magical_solar_upgrades_thaumium", 6, 0);
        put(map, CreativeSection.VOID, "voidminers_magical_solar_upgrades_alchemical_brass", 6, 2);
        put(map, CreativeSection.VOID, "voidminers_magical_solar_upgrades_void_metal", 6, 4);
        // Nodes & Flux
        put(map, CreativeSection.NODES, "node_power_flux_jar", 0, 0);
        put(map, CreativeSection.NODES, "node_power_rift_jar", 0, 2);
        // Flux Network
        put(map, CreativeSection.FLUX_NETWORK, "flux_rift_extractor", 0, 0);
        put(map, CreativeSection.FLUX_NETWORK, "flux_transmission_link", 0, 2);
        put(map, CreativeSection.FLUX_NETWORK, "flux_transmission_link_alchemical_brass", 0, 4);
        put(map, CreativeSection.FLUX_NETWORK, "flux_transmission_link_void_metal", 0, 6);
        put(map, CreativeSection.FLUX_NETWORK, "flux_storage_core_thaumium", 2, 0);
        put(map, CreativeSection.FLUX_NETWORK, "flux_storage_core_alchemical_brass", 2, 2);
        put(map, CreativeSection.FLUX_NETWORK, "flux_storage_core_void_metal", 2, 4);
        put(map, CreativeSection.FLUX_NETWORK, "flux_charger_thaumium", 4, 0);
        put(map, CreativeSection.FLUX_NETWORK, "flux_charger_alchemical_brass", 4, 2);
        put(map, CreativeSection.FLUX_NETWORK, "flux_charger_void_metal", 4, 4);
        // Flux Gear
        put(map, CreativeSection.FLUX_GEAR, "field_shield_thaumium", 0, 0);
        put(map, CreativeSection.FLUX_GEAR, "field_shield_alchemical_brass", 0, 2);
        put(map, CreativeSection.FLUX_GEAR, "field_shield_void_metal", 0, 4);
        put(map, CreativeSection.FLUX_GEAR, "field_terraformer_thaumium", 2, 0);
        put(map, CreativeSection.FLUX_GEAR, "field_terraformer_alchemical_brass", 2, 2);
        put(map, CreativeSection.FLUX_GEAR, "field_terraformer_void_metal", 2, 4);
        put(map, CreativeSection.FLUX_GEAR, "field_flight_thruster", 4, 0);
        put(map, CreativeSection.FLUX_GEAR, "flux_portable_void_siphon", 6, 0);
        put(map, CreativeSection.FLUX_GEAR, "flux_discharge_cannon", 8, 0);
        // Tainted Food
        put(map, CreativeSection.FOOD_TAINTED, "tainted_food", 0, 0);
        // Farmers Delight
        put(map, CreativeSection.FOOD_FARMERS, "tainted_farmersdelight_food", 0, 0);
        put(map, CreativeSection.FOOD_FARMERS, "farmersdelight_thaumium_knife", 2, 0);
        put(map, CreativeSection.FOOD_FARMERS, "farmersdelight_alchemical_brass_knife", 2, 2);
        put(map, CreativeSection.FOOD_FARMERS, "farmersdelight_void_metal_knife", 2, 4);
        put(map, CreativeSection.FOOD_FARMERS, "farmersdelight_flux_knife", 2, 6);
        // Purity (positions unchanged)
        put(map, CreativeSection.PURITY, "purity", 0, 0);
        put(map, CreativeSection.PURITY, "purification_potion", 0, 2);
        put(map, CreativeSection.PURITY, "biome_purification_potion", -1, 4);
        put(map, CreativeSection.PURITY, "clarity_potion", 1, 4);
        put(map, CreativeSection.PURITY, "absolution_potion", 1, 6);
        // Ender IO
        put(map, CreativeSection.ENDERIO, "enderio_conduits", 0, 0);
        put(map, CreativeSection.ENDERIO, "enderio_capacitors", 2, 0);
        put(map, CreativeSection.ENDERIO, "enderio_capacitor_banks", 2, 2);
        put(map, CreativeSection.ENDERIO, "enderio_wand_parts", 4, 0);
        // Hephaestus Tools: the four extra metals
        put(map, CreativeSection.METALS, "extra_metals", 0, 0);
        // Extras: the Tech header and everything without its own creative tab
        put(map, CreativeSection.EXTRAS, "tech", 0, 0);
        put(map, CreativeSection.EXTRAS, "aura_converter_thaumium", 0, 2);
        put(map, CreativeSection.EXTRAS, "aura_converter_brass", 0, 4);
        put(map, CreativeSection.EXTRAS, "aura_converter_void", 0, 6);
        put(map, CreativeSection.EXTRAS, "forcecraft_repair", 2, 2);
        put(map, CreativeSection.EXTRAS, "projecte_emc", 4, 2);
        put(map, CreativeSection.EXTRAS, "tech_essences", 6, 2);
        return Collections.unmodifiableMap(map);
    }

    private static void put(Map<String, Placement> map, CreativeSection section, String entry, int column, int row) {
        if (map.put(entry, new Placement(section, column, row)) != null) {
            throw new IllegalStateException("Duplicate research layout for " + entry);
        }
    }

    public static Map<String, Placement> all() {
        return ENTRIES;
    }

    /** Moves a research entry being saved to its category and position; any other file is returned untouched. */
    public static JsonElement apply(Path path, JsonElement json) {
        Path parent = path.getParent();
        if (!json.isJsonObject() || parent == null || !"research_entry".equals(parent.getFileName().toString())) {
            return json;
        }
        String file = path.getFileName().toString();
        if (!file.endsWith(".json")) {
            return json;
        }
        Placement placement = ENTRIES.get(file.substring(0, file.length() - ".json".length()));
        if (placement == null) {
            return json;
        }
        JsonObject object = json.getAsJsonObject();
        object.addProperty("category", placement.category());
        object.addProperty("column", placement.column());
        object.addProperty("row", placement.row());
        return object;
    }
}
