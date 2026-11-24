package nadiendev.ThaumaturgeTechCompats.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/** Tooltip component that shows the hovered item enlarged, like the cash previews of other addons. */
public record ItemPreview(ItemStack stack) implements TooltipComponent {
}
