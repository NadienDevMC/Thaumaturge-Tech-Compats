package nadiendev.ThaumaturgeTechCompats.compats.hephaestus;

import nadiendev.ThaumaturgeTechCompats.tiercontent.Tier;

public enum MoltenMetal {
    CRYSTAL_MATRIX("crystal_matrix", "Crystal Matrix", "Matriz de Cristal", 0xFF7CF3E8, 1100,
            "avaritia:crystal_matrix_ingot", null, "avaritia:crystal_matrix", Tier.MATRIX),
    NEUTRONIUM("neutronium", "Neutronium", "Neutronio", 0xFF5A6070, 1500,
            "avaritia:neutron_ingot", "avaritia:neutron_nugget", "avaritia:neutron", Tier.NEUTRONIUM),
    INFINITY("infinity", "Infinity", "Infinito", 0xFFFF5FA8, 2000,
            "avaritia:infinity_ingot", "avaritia:infinity_nugget", "avaritia:infinity", Tier.INFINITY);

    private final String id;
    private final String english;
    private final String spanish;
    private final int color;
    private final int temperature;
    private final String ingot;
    private final String nugget;
    private final String block;
    private final Tier tier;

    MoltenMetal(String id, String english, String spanish, int color, int temperature,
                String ingot, String nugget, String block, Tier tier) {
        this.id = id;
        this.english = english;
        this.spanish = spanish;
        this.color = color;
        this.temperature = temperature;
        this.ingot = ingot;
        this.nugget = nugget;
        this.block = block;
        this.tier = tier;
    }

    public String id() {
        return id;
    }

    public String fluidName() {
        return "molten_" + id;
    }

    public String flowingName() {
        return "molten_" + id + "_flowing";
    }

    public String bucketName() {
        return "molten_" + id + "_bucket";
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    /** ARGB tint applied to the Hephaestus molten metal textures. */
    public int color() {
        return color;
    }

    public int temperature() {
        return temperature;
    }

    /** Ingredient string; a leading '#' marks an item tag. */
    public String ingot() {
        return ingot;
    }

    /** Null when the metal has no nugget item. */
    public String nugget() {
        return nugget;
    }

    public String block() {
        return block;
    }

    public String plate() {
        return "thaumaturgetechcompats:" + tier.plateName();
    }
}
