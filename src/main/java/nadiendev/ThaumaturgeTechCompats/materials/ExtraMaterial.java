package nadiendev.ThaumaturgeTechCompats.materials;

import java.util.List;

/**
 * Pure data of the four extra metals (no Minecraft classes, so the JSON builders can run on their own).
 * Tool numbers are the Hephaestus Tools head/handle/binding stats; wand numbers are the Thaumaturge rod and cap stats.
 */
public enum ExtraMaterial {
    RIFTING("rifting", "Rifting", 0x6A4FB5, 0xFF6A4FB5, 1600, false,
            8500, 6.0F, 2.5F, 5, 14, List.of(),
            0.08F, 0.02F, 0.04F, 0.0F, 100,
            150, 10, true, 0.78F, 0.68F, 8, List.of("vacuos", "alienis"), 18),
    FIBRIC("fibric", "Fibric", 0xB24FCF, 0xFFB24FCF, 1800, true,
            45500, 8.0F, 3.5F, 3, 18, List.of(),
            0.12F, 0.04F, 0.08F, 0.0F, 240,
            400, 18, true, 0.60F, 0.50F, 12, List.of("fabrico", "vitium"), 26),
    SALIS("salis", "Salis", 0xF2E3A0, 0xFFF2E3A0, 2000, true,
            75900, 11.0F, 5.0F, 3, 25, List.of(),
            0.20F, 0.08F, 0.15F, 0.0F, 550,
            800, 24, true, 0.45F, 0.35F, 16, List.of("praecantatio", "potentia"), 32),
    CRIMSON("crimson", "Crimson", 0xB3202E, 0xFFB3202E, 1700, false,
            12500, 7.0F, 3.0F, 5, 15, List.of(),
            0.10F, 0.03F, 0.06F, 0.0F, 160,
            250, 14, false, 0.70F, 0.60F, 10, List.of("perditio", "humanus"), 22);


    public static final String MOD = "thaumaturgetechcompats";
    public static final String RESEARCH = "extra_metals";

    private final String id;
    private final String english;
    private final int rgb;
    private final int argb;
    private final int temperature;
    private final boolean epic;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int tier;
    private final int enchantability;
    private final List<String> traits;
    private final float handleDurability;
    private final float handleSpeed;
    private final float handleDamage;
    private final float handleAttackSpeed;
    private final int bindingDurability;
    private final int rodCapacity;
    private final int rodCraftCost;
    private final boolean glow;
    private final float capCost;
    private final float capSpecialCost;
    private final int capCraftCost;
    private final List<String> capAspects;
    private final int infusionAspect;

    ExtraMaterial(String id, String english, int rgb, int argb, int temperature, boolean epic,
                  int durability, float miningSpeed, float attackDamage, int tier, int enchantability, List<String> traits,
                  float handleDurability, float handleSpeed, float handleDamage, float handleAttackSpeed, int bindingDurability,
                  int rodCapacity, int rodCraftCost, boolean glow, float capCost, float capSpecialCost, int capCraftCost,
                  List<String> capAspects, int infusionAspect) {
        this.id = id;
        this.english = english;
        this.rgb = rgb;
        this.argb = argb;
        this.temperature = temperature;
        this.epic = epic;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.tier = tier;
        this.enchantability = enchantability;
        this.traits = traits;
        this.handleDurability = handleDurability;
        this.handleSpeed = handleSpeed;
        this.handleDamage = handleDamage;
        this.handleAttackSpeed = handleAttackSpeed;
        this.bindingDurability = bindingDurability;
        this.rodCapacity = rodCapacity;
        this.rodCraftCost = rodCraftCost;
        this.glow = glow;
        this.capCost = capCost;
        this.capSpecialCost = capSpecialCost;
        this.capCraftCost = capCraftCost;
        this.capAspects = capAspects;
        this.infusionAspect = infusionAspect;
    }

    public String id() {
        return id;
    }

    public String english() {
        return english;
    }

    /** Spanish keeps the names as they are. */
    public String spanish() {
        return english;
    }

    public int rgb() {
        return rgb;
    }

    public int argb() {
        return argb;
    }

    public int temperature() {
        return temperature;
    }

    public boolean epic() {
        return epic;
    }

    public int durability() {
        return durability;
    }

    public float miningSpeed() {
        return miningSpeed;
    }

    public float attackDamage() {
        return attackDamage;
    }

    public int tier() {
        return tier;
    }

    public int enchantability() {
        return enchantability;
    }

    public List<String> traits() {
        return traits;
    }

    public float handleDurability() {
        return handleDurability;
    }

    public float handleSpeed() {
        return handleSpeed;
    }

    public float handleDamage() {
        return handleDamage;
    }

    public float handleAttackSpeed() {
        return handleAttackSpeed;
    }

    public int bindingDurability() {
        return bindingDurability;
    }

    public int rodCapacity() {
        return rodCapacity;
    }

    public int rodCraftCost() {
        return rodCraftCost;
    }

    public boolean glow() {
        return glow;
    }

    public float capCost() {
        return capCost;
    }

    public float capSpecialCost() {
        return capSpecialCost;
    }

    public int capCraftCost() {
        return capCraftCost;
    }

    /** Thaumaturge aspect ids (path only) the cap makes cheaper. */
    public List<String> capAspects() {
        return capAspects;
    }

    /** Amount of each aspect the awakening infusion asks for. */
    public int infusionAspect() {
        return infusionAspect;
    }

    public String ingotName() {
        return id + "_ingot";
    }

    public String nuggetName() {
        return id + "_nugget";
    }

    public String blockName() {
        return id + "_block";
    }

    public String clusterName() {
        return id + "_cluster";
    }

    public String rodName() {
        return id + "_wand_rod";
    }

    public String capName() {
        return id + "_wand_cap";
    }

    public String inertRodName() {
        return id + "_inert_wand_rod";
    }

    public String inertCapName() {
        return id + "_inert_wand_cap";
    }

    public String moltenName() {
        return "molten_" + id;
    }

    public String ingotTag() {
        return "c:ingots/" + id;
    }

    public String nuggetTag() {
        return "c:nuggets/" + id;
    }

    public String blockTag() {
        return "c:storage_blocks/" + id;
    }
}
