package nadiendev.ThaumaturgeTechCompats.registry;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;

public enum CreativeSection {
    CORE(CreativeParent.MAIN, "avaritia", "Avaritia", "Avaritia"),
    PEARLS(CreativeParent.MAIN, "avaritia_pearls", "Pearls & Cruc.", "Perlas y Cruc."),
    SOLAR(CreativeParent.MAIN, "solar_panels", "Solar Panels", "Paneles solares"),
    CREATE(CreativeParent.MAIN, "create", "Create", "Create"),
    ORITECH(CreativeParent.MAIN, "oritech", "Oritech", "Oritech"),
    MEKANISM(CreativeParent.MAIN, "mekanism", "Mekanism", "Mekanism"),
    BOTANIA(CreativeParent.MAIN, "botania", "Botania", "Botania"),
    VOID(CreativeParent.MAIN, "void_miners", "Void Miners", "Void Miners"),
    NODES(CreativeParent.MAIN, "nodes", "Nodes & Flux", "Nodos y flujo"),
    FLUX_NETWORK(CreativeParent.MAIN, "flux_network", "Flux Network", "Red de flujo"),
    FLUX_GEAR(CreativeParent.MAIN, "flux_gear", "Flux Gear", "Equipo flujo"),
    FOOD_TAINTED(CreativeParent.MAIN, "food_tainted", "Tainted Food", "Comida corrupta"),
    FOOD_FARMERS(CreativeParent.MAIN, "food_farmers", "Farmers Delight", "Farmers Delight"),
    FOOD_EDIBLE(CreativeParent.MAIN, "food_edible", "Edible Items", "Comestibles"),
    PURITY(CreativeParent.MAIN, "purity", "Purity", "Pureza"),
    ENDERIO(CreativeParent.MAIN, "enderio", "Ender IO", "Ender IO"),
    EXTRA_METALS(CreativeParent.MAIN, "extra_metals", "Hephaestus Tools", "Hephaestus Tools"),
    EXTRAS(CreativeParent.MAIN, "extras", "Extras", "Extras");

    private final CreativeParent parent;
    private final String id;
    private final String english;
    private final String spanish;

    CreativeSection(CreativeParent parent, String id, String english, String spanish) {
        this.parent = parent;
        this.id = id;
        this.english = english;
        this.spanish = spanish;
    }

    public CreativeParent parent() {
        return parent;
    }

    public String id() {
        return id;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public String key() {
        return "itemGroup." + ThaumaturgeTechCompats.MOD_ID + "." + id;
    }
}
