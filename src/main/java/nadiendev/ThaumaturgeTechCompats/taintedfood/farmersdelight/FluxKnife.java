package nadiendev.ThaumaturgeTechCompats.taintedfood.farmersdelight;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;


public final class FluxKnife {
    public static final String ID = "flux_knife";
    public static final String ENTRY_NAME = "farmersdelight_" + ID;
    public static final String RIFT_TOOLTIP_KEY = "item." + ThaumaturgeTechCompats.MOD_ID + "." + ID + ".tooltip.rift";
    public static final String RIFT_SNEAK_TOOLTIP_KEY = "item." + ThaumaturgeTechCompats.MOD_ID + "." + ID + ".tooltip.rift_sneak";
    public static final String CUT_TOOLTIP_KEY = "item." + ThaumaturgeTechCompats.MOD_ID + "." + ID + ".tooltip.cut";
    public static final String NO_AURA_KEY = "message." + ThaumaturgeTechCompats.MOD_ID + "." + ID + ".not_enough_aura";
    public static final Rarity RARITY = Rarity.EPIC;
    public static final int DEFAULT_DURABILITY = 700;
    public static final double DEFAULT_DAMAGE_BONUS = 3.5D;
    public static final double DEFAULT_ATTACK_SPEED = -1.8D;
    public static final double DEFAULT_MINING_SPEED = 8.0D;
    public static final int DEFAULT_ENCHANTABILITY = 22;

    private static final Identifier INGOT = Identifier.parse("thaumaturge:ingot_void");

    private FluxKnife() {
    }

    public static Identifier itemId() {
        return Identifier.fromNamespaceAndPath(ThaumaturgeTechCompats.MOD_ID, ID);
    }

    public static String nameKey() {
        return "item." + ThaumaturgeTechCompats.MOD_ID + "." + ID;
    }

    public static Identifier ingot() {
        return INGOT;
    }

    public static TagKey<Item> repairTag() {
        return TagKey.create(Registries.ITEM, ThaumaturgeTechCompats.id("knife_repair/" + ID));
    }

    public static ToolMaterial material() {
        return new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, FarmersDelightTaintedConfig.fluxKnifeDurability(),
                FarmersDelightTaintedConfig.fluxKnifeMiningSpeed(), FarmersDelightTaintedConfig.fluxKnifeDamageBonus(),
                FarmersDelightTaintedConfig.fluxKnifeEnchantability(), repairTag());
    }
}
