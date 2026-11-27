package nadiendev.ThaumaturgeTechCompats.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Passes each stack (same item and same components) to the tab only once, so merged sections never repeat an item. */
public final class DistinctOutput implements CreativeModeTab.Output {
    private final CreativeModeTab.Output delegate;
    private final List<ItemStack> seen = new ArrayList<>();

    public DistinctOutput(CreativeModeTab.Output delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        for (ItemStack other : seen) {
            if (ItemStack.isSameItemSameComponents(other, stack)) {
                return;
            }
        }
        seen.add(stack);
        delegate.accept(stack, visibility);
    }
}
