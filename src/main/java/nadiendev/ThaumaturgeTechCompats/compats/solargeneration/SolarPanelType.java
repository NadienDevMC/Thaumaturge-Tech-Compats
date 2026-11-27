package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.neoforged.fml.ModList;

/**
 * The panels of the solar compat. Declaration order is the registration order and so the
 * creative tab order. Holds no reference to Solar Generation, so it is safe to load without it.
 */
public enum SolarPanelType {
    ALCHEMICAL_COBALT("alchemical_cobalt_solar_panel", CompatToggles.SOLAR_ALCHEMICAL_COBALT, 29_500, 50_500, 2_000_000,
            "Alchemical Cobalt Solar Panel", "Panel solar de cobalto alquímico", "solar_panel/alchemical_cobalt", "hardened", "hephaestus"),
    ALCHEMICAL("alchemical_solar_panel", CompatToggles.SOLAR_ALCHEMICAL, 89_500, 50_500, 18_000_000,
            "Alchemical Solar Panel", "Panel solar alquímico", "solar_panel/alchemical", "redstone"),
    BRASS_ANDESITE("brass_andesite_solar_panel", CompatToggles.SOLAR_BRASS_ANDESITE, 11_000, 5_095, 500_000,
            "Brass Andesite Solar Panel", "Panel solar de latón y andesita", "solar_panel/brass_andesite", "leadstone", "create"),
    BRASS("brass_solar_panel", CompatToggles.SOLAR_ALCHEMICAL_BRASS, 250_000, 100_000, 78_000_000,
            "Brass Solar Panel", "Panel solar de latón", "solar_panel/brass", "signalum"),
    THAUMIUM_ZINC("thaumium_zinc_solar_panel", CompatToggles.SOLAR_THAUMIUM_ZINC, 19_500, 50_500, 1_000_000,
            "Thaumium Zinc Solar Panel", "Panel solar de taumio y zinc", "solar_panel/thaumium_zinc", "resonant", "create", "alltheores"),
    // thaumium and void metal panels of Void Miners already use the plain ids, so these two carry a prefix
    THAUMIUM("thaumium_generation_solar_panel", CompatToggles.SOLAR_THAUMIUM, 791_800, 7_000_000, 88_000_000,
            "Thaumium Solar Panel", "Panel solar de taumio", "solar_panel/thaumium", "advanced"),
    VOID_URANIUM("void_uranium_solar_panel", CompatToggles.SOLAR_VOID_URANIUM, 15_035_000, 150_350_000, 95_060_000,
            "Void Uranium Solar Panel", "Panel solar de uranio del vacío", "solar_panel/void_uranium", "ultimate", "alltheores", "mekanism"),
    VOID_METAL("void_metal_generation_solar_panel", CompatToggles.SOLAR_VOID_METAL, 15_500_000, 155_000_000, 98_000_000,
            "Void Metal Solar Panel", "Panel solar de metal del vacío", "solar_panel/void_metal", "ultimate");

    private final String id;
    private final String toggle;
    private final int generation;
    private final int transfer;
    private final int capacity;
    private final String english;
    private final String spanish;
    private final String texture;
    private final String placeholder;
    private final String[] anyOf;

    SolarPanelType(String id, String toggle, int generation, int transfer, int capacity, String english, String spanish,
                   String texture, String placeholder, String... anyOf) {
        this.id = id;
        this.toggle = toggle;
        this.generation = generation;
        this.transfer = transfer;
        this.capacity = capacity;
        this.english = english;
        this.spanish = spanish;
        this.texture = texture;
        this.placeholder = placeholder;
        this.anyOf = anyOf;
    }

    /** Registry path of the block and the item. */
    public String id() {
        return id;
    }

    public String toggle() {
        return toggle;
    }

    /** FE produced per tick under full sun. */
    public int generation() {
        return generation;
    }

    /** FE per tick it can hand to neighbours. */
    public int transfer() {
        return transfer;
    }

    public int capacity() {
        return capacity;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    /** Path of the final texture under textures/block/ (not drawn yet). */
    public String texture() {
        return texture;
    }

    /** Texture of Solar Generation (block/&lt;name&gt;) used until the final one exists. */
    public String placeholder() {
        return placeholder;
    }

    /** Mods of which at least one must be loaded for the panel to exist (empty means none needed). */
    public String[] anyOf() {
        return anyOf;
    }

    public boolean modsPresent() {
        if (anyOf.length == 0) {
            return true;
        }
        for (String mod : anyOf) {
            if (ModList.get().isLoaded(mod)) {
                return true;
            }
        }
        return false;
    }

    /** Toggle on and, if the panel needs other mods, at least one of them loaded. */
    public boolean enabled() {
        return CompatToggles.enabled(toggle) && modsPresent();
    }
}
