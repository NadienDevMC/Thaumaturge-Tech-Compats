package nadiendev.ThaumaturgeTechCompats.materials.modifier;

import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * The four extra Hephaestus Tools modifiers. Each one is applied in the Arsenal Table (Modify tab) by consuming its ingot
 * (one ingot per level, up to {@link #MAX_LEVEL}); at the maximum level the tool can never lose durability.
 */
public enum ToolModifier {
    UNBREAKABLE("unbreakable", "reach", "thaumaturge", "ingot_thaumium", "Unbreakable", "Irrompible",
            "Chance to avoid durability loss that grows with every level. Level 12: the tool never loses durability.",
            "Probabilidad de no perder durabilidad que crece con cada nivel. Nivel 12: la herramienta nunca pierde durabilidad."),
    VIS_REPAIR("vis_repair", "attack_speed", "thaumaturge", "ingot_brass", "Vis Repair", "Reparación de vis",
            "The tool mends itself over time, much faster while the aura of the chunk has vis to give. Level 12: the tool never loses durability.",
            "La herramienta se repara con el tiempo, mucho más rápido mientras el aura del chunk tenga vis. Nivel 12: la herramienta nunca pierde durabilidad."),
    VOID_TELEPORT("void_teleport", "critical", "thaumaturge", "ingot_void", "Void Teleport", "Teletransporte del vacío",
            "Mined blocks and the drops of slain mobs go straight into your inventory, more often with every level (always from level 8). Level 12: the tool never loses durability.",
            "Los bloques minados y los botines de los mobs van directos a tu inventario, más veces con cada nivel (siempre desde el nivel 8). Nivel 12: la herramienta nunca pierde durabilidad."),
    ENDER_TELEPORT("ender_teleport", "damage", "thaumaturge", "fabric", "Ender Teleport", "Teletransporte ender",
            "Use it to teleport forward like an enderman, farther with every level. Hitting a mob may also blink you away. Level 12: the tool never loses durability.",
            "Úsala para teletransportarte hacia delante como un enderman, más lejos con cada nivel. Al golpear a un mob puedes saltar a otro sitio. Nivel 12: la herramienta nunca pierde durabilidad.");

    public static final int MAX_LEVEL = 12;
    public static final String MOD = "thaumaturgetechcompats";

    private final String path;
    private final String iconName;
    private final Identifier ingot;
    private final String english;
    private final String spanish;
    private final String englishDesc;
    private final String spanishDesc;

    ToolModifier(String path, String iconName, String ingotNamespace, String ingotPath, String english, String spanish,
                 String englishDesc, String spanishDesc) {
        this.path = path;
        this.iconName = iconName;
        this.ingot = Identifier.fromNamespaceAndPath(ingotNamespace, ingotPath);
        this.english = english;
        this.spanish = spanish;
        this.englishDesc = englishDesc;
        this.spanishDesc = spanishDesc;
    }

    public String iconName() {
        return iconName;
    }

    public String path() {
        return path;
    }

    public Identifier id() {
        return Identifier.fromNamespaceAndPath(MOD, path);
    }

    public Identifier ingot() {
        return ingot;
    }

    public String nameKey() {
        return "modifier." + MOD + "." + path;
    }

    public String descKey() {
        return nameKey() + ".desc";
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }

    public String englishDesc() {
        return englishDesc;
    }

    public String spanishDesc() {
        return spanishDesc;
    }

    public static Optional<ToolModifier> byIngot(Identifier ingot) {
        for (ToolModifier m : values()) {
            if (m.ingot.equals(ingot)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }
}
