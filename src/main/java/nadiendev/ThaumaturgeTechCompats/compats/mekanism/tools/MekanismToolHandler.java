package nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;

/** Energy of the Mekanism tools: a NeoForge item energy handler (Mekanism energy is plain FE now) backed by the tool energy component. */
public class MekanismToolHandler extends ItemAccessEnergyHandler {
    public MekanismToolHandler(ItemStack stack, ItemAccess access) {
        super(access, MekanismToolRegistry.ENERGY.get(), (int) MekanismToolBehavior.capacity(stack));
    }
}
