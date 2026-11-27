package nadiendev.ThaumaturgeTechCompats.taintedfood.farmersdelight;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import vectorwing.farmersdelight.common.item.KnifeItem;

/**
 * The two 26.1.2 ports of Farmer's Delight build {@link KnifeItem} differently: BuaichiZY (the one this mod is built against)
 * takes (ToolMaterial, Properties) and jexxme's Farmer's Delight Neoforged only (Properties). Calling the missing constructor crashes the
 * game while items register. When the constructor this mod was built against is not there, the knives are registered as plain
 * tool items instead: they keep the knife tags, mining tag, damage, taint and flux behaviour, only not the Farmer's Delight class.
 */
public final class KnifeSupport {
    private static final boolean KNIFE_ITEM_CONSTRUCTOR = detect();

    private KnifeSupport() {
    }

    public static boolean extendKnifeItem() {
        return KNIFE_ITEM_CONSTRUCTOR;
    }

    private static boolean detect() {
        try {
            KnifeItem.class.getConstructor(ToolMaterial.class, Item.Properties.class);
            return true;
        } catch (NoSuchMethodException | LinkageError e) {
            return false;
        }
    }
}
