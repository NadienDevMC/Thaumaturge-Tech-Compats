package nadiendev.ThaumaturgeTechCompats.metals.upgrades;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.resources.ResourceLocation;

/** The four Hephaestus Tools upgrades, each one applied in the Arsenal Table by consuming an ingot-like item. */
public enum ToolUpgradeKind {
    UNBREAKABLE("unbreakable", "thaumaturge", "ingot_thaumium", "Unbreakable", "Irrompible",
            "Chance to avoid durability loss grows with each level. Level 12 makes the tool unbreakable.",
            "La probabilidad de no gastar durabilidad crece con cada nivel. El nivel 12 vuelve la herramienta irrompible."),
    VIS_REPAIR("vis_repair", "thaumaturge", "ingot_brass", "Vis Repair", "Reparación de vis",
            "The tool repairs itself in your inventory using vis from the chunk aura, or slowly without it. Level 12 makes it unbreakable.",
            "La herramienta se repara sola en tu inventario con el vis del aura del chunk, o despacio sin él. El nivel 12 la vuelve irrompible."),
    VOID_TELEPORT("void_teleport", "thaumaturge", "ingot_void", "Void Teleport", "Teletransporte del vacío",
            "Mined blocks and the drops of slain mobs go straight into your inventory, more often with every level (always from level 8). Level 12 makes the tool unbreakable.",
            "Los bloques minados y los drops de los mobs van directos a tu inventario, más veces con cada nivel (siempre desde el nivel 8). El nivel 12 vuelve la herramienta irrompible."),
    ENDER_TELEPORT("ender_teleport", "thaumaturge", "fabric", "Ender Teleport", "Teletransporte ender",
            "Use it to teleport forward like an enderman, farther with every level. Hitting a mob may also blink you away. Level 12 makes the tool unbreakable.",
            "Úsala para teletransportarte hacia delante como un enderman, más lejos con cada nivel. Al golpear a un mob puedes saltar a otro sitio. El nivel 12 vuelve la herramienta irrompible.");

    public static final int MAX_LEVEL = 12;

    private final String id;
    private final String costNamespace;
    private final String costPath;
    private final String english;
    private final String spanish;
    private final String englishDescription;
    private final String spanishDescription;

    ToolUpgradeKind(String id, String costNamespace, String costPath, String english, String spanish,
                    String englishDescription, String spanishDescription) {
        this.id = id;
        this.costNamespace = costNamespace;
        this.costPath = costPath;
        this.english = english;
        this.spanish = spanish;
        this.englishDescription = englishDescription;
        this.spanishDescription = spanishDescription;
    }

    public ResourceLocation id() {
        return ThaumaturgeTechCompats.id(id);
    }

    public ResourceLocation costItemId() {
        return ResourceLocation.fromNamespaceAndPath(costNamespace, costPath);
    }

    public String nameKey() {
        return "upgrade." + ThaumaturgeTechCompats.MOD_ID + "." + id;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public String englishDescription() {
        return englishDescription;
    }

    public String spanishDescription() {
        return spanishDescription;
    }
}
