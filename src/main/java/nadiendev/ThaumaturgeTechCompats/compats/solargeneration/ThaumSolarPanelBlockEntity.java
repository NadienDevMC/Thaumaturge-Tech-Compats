package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import edivad.solargeneration.tools.ProductionSolarPanel;
import edivad.solargeneration.tools.SolarGenerationDataComponents;
import edivad.solargeneration.tools.SolarPanelBattery;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

/** Panel with the stats of its {@link SolarPanelType}; battery and sun curve come from Solar Generation. */
public class ThaumSolarPanelBlockEntity extends BlockEntity implements MenuProvider {
    private static final int MASK = 0xFFFF;

    private final SolarPanelType type;
    private final SolarPanelBattery battery;
    private int production;
    private int lastEnergy = -1;
    private int lastProduction = -1;

    public ThaumSolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(SolarGenerationRegistry.blockEntityType(), pos, state);
        this.type = ((ThaumSolarPanelBlock) state.getBlock()).type();
        this.battery = new SolarPanelBattery(type.transfer(), type.capacity());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ThaumSolarPanelBlockEntity panel) {
        int produced = (int) (panel.type.generation() * ProductionSolarPanel.computeSunIntensity(level, pos, SolarPanelLevel.ULTIMATE));
        panel.battery.generatePower(produced);
        panel.sendEnergy();
        panel.production = panel.battery.isFullEnergy() ? 0 : produced;
        int stored = panel.battery.getAmountAsInt();
        if (stored != panel.lastEnergy || panel.production != panel.lastProduction) {
            panel.lastEnergy = stored;
            panel.lastProduction = panel.production;
            panel.setChanged();
        }
    }

    private void sendEnergy() {
        int stored = battery.getAmountAsInt();
        for (Direction facing : Direction.values()) {
            if (stored <= 0) {
                return;
            }
            if (facing == Direction.UP) {
                continue;
            }
            var handler = level.getCapability(Capabilities.Energy.BLOCK, worldPosition.relative(facing), facing.getOpposite());
            if (handler == null) {
                continue;
            }
            try (var transaction = Transaction.openRoot()) {
                int inserted = handler.insert(Math.min(stored, type.transfer()), transaction);
                if (inserted == 0) {
                    continue;
                }
                stored -= inserted;
                battery.consumePower(inserted);
                setChanged();
                transaction.commit();
            }
        }
    }

    public SolarPanelType type() {
        return type;
    }

    @Nullable
    public SolarPanelBattery getBattery(@Nullable Direction direction) {
        return direction != Direction.UP ? battery : null;
    }

    /** Energy and production split in 16-bit halves, since container data travels as shorts. */
    public ContainerData dataAccess() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> battery.getAmountAsInt() & MASK;
                    case 1 -> battery.getAmountAsInt() >>> 16;
                    case 2 -> production & MASK;
                    case 3 -> production >>> 16;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return ThaumSolarPanelMenu.DATA_COUNT;
            }
        };
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        battery.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        battery.serialize(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        battery.set(components.getOrDefault(SolarGenerationDataComponents.ENERGY_COMPONENT, 0));
        super.applyImplicitComponents(components);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(SolarGenerationDataComponents.ENERGY_COMPONENT, battery.getAmountAsInt());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ThaumSolarPanelMenu(id, this, dataAccess());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }
}
