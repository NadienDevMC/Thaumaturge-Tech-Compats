package nadiendev.ThaumaturgeTechCompats.taintedfood.farmersdelight;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import vectorwing.farmersdelight.common.item.KnifeItem;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;
import java.util.function.Consumer;

/** {@link FluxKnifeItem} for Farmer's Delight ports without the expected knife constructor; see {@link KnifeSupport}. */
public class FluxKnifePlainItem extends Item implements FluxKnifeTool {
    public FluxKnifePlainItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (result != InteractionResult.PASS) {
            return result;
        }
        return FluxKnifeRift.open(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if (FarmersDelightTaintedConfig.riftEnabled()) {
            String key = FarmersDelightTaintedConfig.riftRequireSneak() ? FluxKnife.RIFT_SNEAK_TOOLTIP_KEY : FluxKnife.RIFT_TOOLTIP_KEY;
            tooltip.accept(Component.translatable(key).withStyle(ChatFormatting.DARK_PURPLE));
        }
        if (FarmersDelightTaintedConfig.cutEnabled()) {
            tooltip.accept(Component.translatable(FluxKnife.CUT_TOOLTIP_KEY).withStyle(ChatFormatting.DARK_PURPLE));
        }
        super.appendHoverText(stack, context, display, tooltip, flag);
    }

    /** Knife abilities of Farmer's Delight (cutting board, straw, carving), present in every port. */
    @Override
    public boolean canPerformAction(ItemInstance stack, ItemAbility ability) {
        return KnifeItem.KNIFE_ACTIONS.contains(ability);
    }
}
