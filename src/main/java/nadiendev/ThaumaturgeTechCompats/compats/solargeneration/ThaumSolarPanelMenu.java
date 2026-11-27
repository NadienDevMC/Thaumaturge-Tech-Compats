package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class ThaumSolarPanelMenu extends AbstractContainerMenu {
    public static final int DATA_COUNT = 4;

    private final ThaumSolarPanelBlockEntity panel;
    private final ContainerData data;

    public ThaumSolarPanelMenu(int containerId, ThaumSolarPanelBlockEntity panel, ContainerData data) {
        super(SolarGenerationRegistry.menuType(), containerId);
        this.panel = panel;
        this.data = data;
        addDataSlots(data);
    }

    public ThaumSolarPanelBlockEntity panel() {
        return panel;
    }

    public int energy() {
        return (data.get(1) << 16) | (data.get(0) & 0xFFFF);
    }

    public int production() {
        return (data.get(3) << 16) | (data.get(2) & 0xFFFF);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(panel.getLevel(), panel.getBlockPos()), player, panel.getBlockState().getBlock());
    }
}
