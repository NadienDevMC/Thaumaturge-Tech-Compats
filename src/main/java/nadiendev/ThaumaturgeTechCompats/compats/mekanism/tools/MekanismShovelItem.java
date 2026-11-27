package nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class MekanismShovelItem extends ShovelItem implements MekanismEnergyTool {
    private final MekanismToolTier tier;

    public MekanismShovelItem(MekanismToolTier tier, Properties properties) {
        super(tier.material(), MekanismToolKind.SHOVEL.attackDamage(tier), MekanismToolKind.SHOVEL.attackSpeed(), properties);
        this.tier = tier;
    }

    @Override
    public MekanismToolTier tier() {
        return tier;
    }

    @Override
    public MekanismToolKind kind() {
        return MekanismToolKind.SHOVEL;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return MekanismToolBehavior.destroySpeed(stack, super.getDestroySpeed(stack, state));
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return MekanismToolBehavior.hasEnergy(stack) && super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            MekanismToolBehavior.spend(stack, MekanismToolConfig.blockCost(tier), level, entity);
        }
        return true;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        MekanismToolBehavior.spend(stack, MekanismToolConfig.attackCost(tier), attacker.level(), attacker);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (!MekanismToolBehavior.hasEnergy(stack)) {
            return InteractionResult.PASS;
        }
        InteractionResult result = super.useOn(context);
        if (result.consumesAction()) {
            MekanismToolBehavior.spend(stack, MekanismToolConfig.useCost(tier), context.getLevel(), context.getPlayer());
        }
        return result;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return MekanismToolBehavior.barWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return MekanismToolBehavior.BAR_COLOR;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        MekanismToolTooltip.append(stack, tooltip);
    }
}
