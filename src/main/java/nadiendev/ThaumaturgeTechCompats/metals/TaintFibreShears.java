package nadiendev.ThaumaturgeTechCompats.metals;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Thaumaturge's taint fibre block only drops its item with Silk Touch. Breaking it with shears drops the
 * Taint Fibre item too.
 */
@EventBusSubscriber(modid = ThaumaturgeTechCompats.MOD_ID)
public final class TaintFibreShears {
    private static final ResourceLocation TAINT_FIBRE = ResourceLocation.fromNamespaceAndPath("thaumaturge", "taint_fibre");

    private TaintFibreShears() {
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || event.getPlayer().isCreative()) {
            return;
        }
        ItemStack tool = event.getPlayer().getMainHandItem();
        BlockState state = event.getState();
        if (!tool.canPerformAction(ItemAbilities.SHEARS_DIG) || !BuiltInRegistries.BLOCK.getKey(state.getBlock()).equals(TAINT_FIBRE)) {
            return;
        }
        Item fibre = BuiltInRegistries.ITEM.get(TAINT_FIBRE);
        if (fibre != Items.AIR) {
            Block.popResource(level, event.getPos(), new ItemStack(fibre));
        }
    }
}
