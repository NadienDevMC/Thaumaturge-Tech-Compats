package nadiendev.ThaumaturgeTechCompats.compats.solargeneration;

import edivad.solargeneration.blocks.SolarPanelBlock;
import edivad.solargeneration.tools.SolarPanelLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/**
 * Reuses the shape, waterlogging and drop handling of the Solar Generation panel. Its fixed tier is
 * never read: the block entity, ticker and menu below replace everything that depended on it.
 */
public class ThaumSolarPanelBlock extends SolarPanelBlock {
    private final SolarPanelType type;

    public ThaumSolarPanelBlock(SolarPanelType type, Properties properties) {
        super(SolarPanelLevel.ULTIMATE, properties);
        this.type = type;
    }

    public SolarPanelType type() {
        return type;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player instanceof ServerPlayer serverPlayer) {
            level.getBlockEntity(pos, SolarGenerationRegistry.blockEntityType())
                    .ifPresent(panel -> serverPlayer.openMenu(panel, pos));
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ThaumSolarPanelBlockEntity(pos, state);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide() || blockEntityType != SolarGenerationRegistry.blockEntityType()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<ThaumSolarPanelBlockEntity>) ThaumSolarPanelBlockEntity::serverTick;
    }
}
