package nadiendev.ThaumaturgeTechCompats.taintedfood.farmersdelight;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import vectorwing.farmersdelight.common.item.KnifeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/** {@link ThaumaturgeKnifeItem} for Farmer's Delight ports without the expected knife constructor; see {@link KnifeSupport}. */
public class ThaumaturgeKnifePlainItem extends Item {
    private final FarmersDelightKnife knife;

    public ThaumaturgeKnifePlainItem(FarmersDelightKnife knife, Properties properties) {
        super(properties);
        this.knife = knife;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int duration = FarmersDelightTaintedConfig.taintDuration(knife);
        if (!attacker.level().isClientSide() && duration > 0
                && attacker.getRandom().nextFloat() < FarmersDelightTaintedConfig.taintChance(knife)) {
            MobEffectInstance effect = FarmersDelightTaintedEffect.create(
                    FarmersDelightTaintedConfig.knifeEffectId(),
                    duration,
                    FarmersDelightTaintedConfig.knifeEffectAmplifier());
            target.addEffect(effect, attacker);
        }
        super.postHurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if (FarmersDelightTaintedConfig.taintDuration(knife) > 0 && FarmersDelightTaintedConfig.taintChance(knife) > 0.0F) {
            tooltip.accept(Component.translatable(FarmersDelightKnife.taintTooltipKey()).withStyle(ChatFormatting.DARK_PURPLE));
        }
        super.appendHoverText(stack, context, display, tooltip, flag);
    }

    /** Knife abilities of Farmer's Delight (cutting board, straw, carving), present in every port. */
    @Override
    public boolean canPerformAction(ItemInstance stack, ItemAbility ability) {
        return KnifeItem.KNIFE_ACTIONS.contains(ability);
    }
}
