package nadiendev.ThaumaturgeTechCompats.device.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.function.Function;

/**
 * Wraps the Thaumaturge renderer of a block entity type that this mod shares (advanced alchemical furnace, node
 * stabilizer, node transducer): blocks of this mod get their tier textures, every other block is drawn untouched.
 */
public final class TieredBlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<T, S> {
    private final BlockEntityRenderer<T, S> delegate;
    private final Function<Block, Map<RenderType, RenderType>> swaps;

    public TieredBlockEntityRenderer(BlockEntityRenderer<T, S> delegate, Function<Block, Map<RenderType, RenderType>> swaps) {
        this.delegate = delegate;
        this.swaps = swaps;
    }

    @Override
    public S createRenderState() {
        return delegate.createRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, S state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        delegate.extractRenderState(blockEntity, state, partialTick, cameraPos, crumbling);
    }

    @Override
    public void submit(S state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        delegate.submit(state, pose, TextureSwapCollector.wrap(collector, swapsAt(state)), camera);
    }

    private Map<RenderType, RenderType> swapsAt(S state) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || state.blockPos == null) {
            return null;
        }
        return swaps.apply(level.getBlockState(state.blockPos).getBlock());
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return delegate.shouldRenderOffScreen();
    }

    @Override
    public int getViewDistance() {
        return delegate.getViewDistance();
    }

    @Override
    public boolean shouldRender(T blockEntity, Vec3 cameraPos) {
        return delegate.shouldRender(blockEntity, cameraPos);
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return delegate.getRenderBoundingBox(blockEntity);
    }
}
