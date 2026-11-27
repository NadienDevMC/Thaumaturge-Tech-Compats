package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.Tooltip;
import edivad.solargeneration.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class ThaumSolarPanelItem extends BlockItem {
    private final SolarPanelType type;

    public ThaumSolarPanelItem(ThaumSolarPanelBlock block, SolarPanelType type, Properties properties) {
        super(block, properties);
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        int energy = stack.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT.get(), 0);
        if (energy > 0) {
            tooltip.accept(Tooltip.showInfoCtrl(energy));
        }
        if (Minecraft.getInstance().hasShiftDown()) {
            tooltip.accept(line(Translations.GENERATION, type.generation()));
            tooltip.accept(line(Translations.TRANSFER, type.transfer()));
            tooltip.accept(line(Translations.CAPACITY, type.capacity()).append(" FE"));
        } else {
            tooltip.accept(Component.translatable(Translations.HOLD).withStyle(ChatFormatting.GRAY)
                    .append(" ")
                    .append(Component.literal("Shift").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC))
                    .append(" ")
                    .append(Component.translatable(Translations.FOR_DETAILS).withStyle(ChatFormatting.GRAY)));
        }
    }

    private static MutableComponent line(String key, int value) {
        return Component.translatable(key, String.valueOf(value)).withStyle(ChatFormatting.GRAY);
    }
}
