package nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorBlock;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorBlockEntity;
import net.createmod.catnip.api.math.AngleHelper;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Fallback renderer used when Flywheel visualization is unavailable: draws the shaft half oriented towards the
 * block facing and spinning around it.
 */
public class ThaummotorRenderer extends KineticBlockEntityRenderer<ThaummotorBlockEntity> {
    public ThaummotorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected List<BlockStateModelPart> getRotatingModelParts(ThaummotorBlockEntity blockEntity, BlockState state) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        ThaummotorPartials.SHAFT_HALF.get().collectParts(RandomSource.create(state.getSeed(blockEntity.getBlockPos())), parts);
        return parts;
    }

    @Override
    protected void transformRotatingModel(ThaummotorBlockEntity blockEntity, PoseStack stack, float partialTicks) {
        Direction facing = blockEntity.getBlockState().getValue(ThaummotorBlock.FACING);
        stack.translate(0.5D, 0.5D, 0.5D);
        if (facing.getAxis().isHorizontal()) {
            stack.mulPose(rotation(Direction.Axis.Y, (float) Math.toRadians(AngleHelper.horizontalAngle(facing))));
        } else {
            stack.mulPose(rotation(Direction.Axis.X, (float) Math.toRadians(facing == Direction.UP ? -90.0F : 90.0F)));
        }
        float angle = getAngleForBe(blockEntity, blockEntity.getBlockPos(), facing.getAxis(), partialTicks);
        stack.mulPose(rotation(Direction.Axis.Z, angle));
        stack.translate(-0.5D, -0.5D, -0.5D);
    }
}
