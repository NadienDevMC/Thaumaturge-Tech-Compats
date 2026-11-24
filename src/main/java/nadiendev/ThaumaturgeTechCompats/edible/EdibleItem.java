package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class EdibleItem extends Item {
    public static final String NAME_KEY = "item." + ThaumaturgeTechCompats.MOD_ID + ".edible";

    private final EdibleEntry entry;

    public EdibleItem(EdibleEntry entry, Properties properties) {
        super(properties);
        this.entry = entry;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component original = BuiltInRegistries.ITEM.getOptional(entry.original())
                .map(item -> new ItemStack(item).getHoverName())
                .orElseGet(() -> Component.translatable(entry.original().toLanguageKey("item")));
        return Component.translatable(NAME_KEY, original);
    }

    /** Lists the effects eaten, like a potion does, since vanilla food tooltips do not show them. */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food == null || food.effects().isEmpty()) {
            return;
        }
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
        float tickRate = context.tickRate();
        for (FoodProperties.PossibleEffect possible : food.effects()) {
            MobEffectInstance effect = possible.effect();
            MutableComponent line = Component.translatable(effect.getDescriptionId());
            if (effect.getAmplifier() > 0) {
                line = Component.translatable("potion.withAmplifier", line,
                        Component.translatable("potion.potency." + effect.getAmplifier()));
            }
            if (!effect.endsWithin(20)) {
                line = Component.translatable("potion.withDuration", line,
                        MobEffectUtil.formatDuration(effect, 1.0F, tickRate));
            }
            tooltip.add(line.withStyle(effect.getEffect().value().getCategory().getTooltipFormatting()));
        }
    }
}
