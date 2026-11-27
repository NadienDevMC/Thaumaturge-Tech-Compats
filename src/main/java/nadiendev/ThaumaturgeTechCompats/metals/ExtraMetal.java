package nadiendev.ThaumaturgeTechCompats.metals;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Rarity;

import java.util.List;

/**
 * The four new metals: ingot, nugget, block, native cluster, molten fluid and wand parts.
 * Values aligned with the 26.1.2 branch (materials/ExtraMaterial): rod and cap stats, cap aspects, molten temperature
 * and tint, rarity and glow. {@code tier} is the Hephaestus Tools head tier, used only for the infusion instability
 * (2 * tier - 2); {@code infusionAspect} is the amount of each aspect the infusions ask for.
 */
public enum ExtraMetal {
    SALIS("salis", "Salis", "Salis", Rarity.EPIC, 0xFFF2E3A0, 2000,
            800, 24, true, 0.45F, 0.35F, 16, List.of(TTAspects.PRAECANTATIO, TTAspects.POTENTIA), 3, 32),
    FIBRIC("fibric", "Fibric", "Fibric", Rarity.EPIC, 0xFFB24FCF, 1800,
            400, 18, true, 0.60F, 0.50F, 12, List.of(TTAspects.FABRICO, TTAspects.VITIUM), 3, 26),
    CRIMSON("crimson", "Crimson", "carmesí", Rarity.RARE, 0xFFB3202E, 1700,
            250, 14, false, 0.70F, 0.60F, 10, List.of(TTAspects.PERDITIO, TTAspects.HUMANUS), 5, 22),
    RIFTING("rifting", "Rifting", "Rifting", Rarity.RARE, 0xFF6A4FB5, 1600,
            150, 10, true, 0.78F, 0.68F, 8, List.of(TTAspects.VACUOS, TTAspects.ALIENIS), 5, 18);

    /** Research entry of the four metals, their ingots and their wand parts. */
    public static final String RESEARCH = "extra_metals";

    private final String id;
    private final String english;
    private final String spanish;
    private final Rarity rarity;
    private final int color;
    private final int temperature;
    private final int rodCapacity;
    private final int rodCraftCost;
    private final boolean rodGlow;
    private final float capBaseCost;
    private final float capSpecialCost;
    private final int capCraftCost;
    private final List<ResourceKey<IAspect>> capAspects;
    private final int tier;
    private final int infusionAspect;

    ExtraMetal(String id, String english, String spanish, Rarity rarity, int color, int temperature,
               int rodCapacity, int rodCraftCost, boolean rodGlow,
               float capBaseCost, float capSpecialCost, int capCraftCost, List<ResourceKey<IAspect>> capAspects,
               int tier, int infusionAspect) {
        this.id = id;
        this.english = english;
        this.spanish = spanish;
        this.rarity = rarity;
        this.color = color;
        this.temperature = temperature;
        this.rodCapacity = rodCapacity;
        this.rodCraftCost = rodCraftCost;
        this.rodGlow = rodGlow;
        this.capBaseCost = capBaseCost;
        this.capSpecialCost = capSpecialCost;
        this.capCraftCost = capCraftCost;
        this.capAspects = capAspects;
        this.tier = tier;
        this.infusionAspect = infusionAspect;
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

    public Rarity rarity() {
        return rarity;
    }

    /** ARGB tint of the molten fluid. */
    public int color() {
        return color;
    }

    public int temperature() {
        return temperature;
    }

    public int rodCapacity() {
        return rodCapacity;
    }

    public int rodCraftCost() {
        return rodCraftCost;
    }

    public boolean rodGlow() {
        return rodGlow;
    }

    public float capBaseCost() {
        return capBaseCost;
    }

    public float capSpecialCost() {
        return capSpecialCost;
    }

    public int capCraftCost() {
        return capCraftCost;
    }

    public List<ResourceKey<IAspect>> capAspects() {
        return capAspects;
    }

    /** Instability of the ingot and wand part infusions. */
    public int instability() {
        return 2 * tier - 2;
    }

    /** Amount of each aspect the ingot and wand part infusions ask for. */
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
        return id + "_native_cluster";
    }

    public String capName() {
        return id + "_wand_cap";
    }

    public String inertCapName() {
        return id + "_wand_cap_inert";
    }

    public String rodName() {
        return id + "_wand_rod";
    }

    public String inertRodName() {
        return id + "_wand_rod_inert";
    }
}
