package nadiendev.ThaumaturgeTechCompats.device.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Map;

/**
 * 26.1 replacement of the 1.21.1 TextureSwapBuffer: a submit collector that hands every submit to the real one,
 * but swaps the render types of the Thaumaturge textures for the tier textures of this mod. Thaumaturge draws the
 * furnace, stabilizer and transducer with {@code RenderTypes.entityCutout/entityTranslucent(texture)}, which are
 * cached per texture, so a map lookup by render type finds them.
 */
public final class TextureSwapCollector implements SubmitNodeCollector {
    private final OrderedSubmitNodeCollector delegate;
    private final Map<RenderType, RenderType> swaps;

    private TextureSwapCollector(OrderedSubmitNodeCollector delegate, Map<RenderType, RenderType> swaps) {
        this.delegate = delegate;
        this.swaps = swaps;
    }

    public static SubmitNodeCollector wrap(SubmitNodeCollector collector, Map<RenderType, RenderType> swaps) {
        return swaps == null || swaps.isEmpty() ? collector : new TextureSwapCollector(collector, swaps);
    }

    private RenderType swap(RenderType type) {
        RenderType replacement = swaps.get(type);
        return replacement == null ? type : replacement;
    }

    @Override
    public OrderedSubmitNodeCollector order(int order) {
        if (delegate instanceof SubmitNodeCollector collector) {
            return new TextureSwapCollector(collector.order(order), swaps);
        }
        return this;
    }

    @Override
    public void submitShadow(PoseStack pose, float radius, List<EntityRenderState.ShadowPiece> pieces) {
        delegate.submitShadow(pose, radius, pieces);
    }

    @Override
    public void submitNameTag(PoseStack pose, Vec3 position, int offset, Component name, boolean seeThrough, int light, double distance, CameraRenderState camera) {
        delegate.submitNameTag(pose, position, offset, name, seeThrough, light, distance, camera);
    }

    @Override
    public void submitText(PoseStack pose, float x, float y, FormattedCharSequence text, boolean shadow, Font.DisplayMode mode, int light, int color, int background, int outline) {
        delegate.submitText(pose, x, y, text, shadow, mode, light, color, background, outline);
    }

    @Override
    public void submitFlame(PoseStack pose, EntityRenderState state, Quaternionf rotation) {
        delegate.submitFlame(pose, state, rotation);
    }

    @Override
    public void submitLeash(PoseStack pose, EntityRenderState.LeashState leash) {
        delegate.submitLeash(pose, leash);
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack pose, RenderType type, int light, int overlay, int color,
                                TextureAtlasSprite sprite, int outline, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        delegate.submitModel(model, state, pose, swap(type), light, overlay, color, sprite, outline, crumbling);
    }

    @Override
    public void submitModelPart(ModelPart part, PoseStack pose, RenderType type, int light, int overlay, TextureAtlasSprite sprite,
                                boolean sheeted, boolean glint, int color, ModelFeatureRenderer.CrumblingOverlay crumbling, int outline) {
        delegate.submitModelPart(part, pose, swap(type), light, overlay, sprite, sheeted, glint, color, crumbling, outline);
    }

    @Override
    public void submitMovingBlock(PoseStack pose, MovingBlockRenderState state) {
        delegate.submitMovingBlock(pose, state);
    }

    @Override
    public void submitBlockModel(PoseStack pose, RenderType type, List<BlockStateModelPart> parts, int[] tints, int light, int overlay, int outline) {
        delegate.submitBlockModel(pose, swap(type), parts, tints, light, overlay, outline);
    }

    @Override
    public void submitMultiLayerBlockModel(PoseStack pose, List<BlockStateModelPart> parts, boolean translucent, int[] tints, int light, int overlay, int outline) {
        delegate.submitMultiLayerBlockModel(pose, parts, translucent, tints, light, overlay, outline);
    }

    @Override
    public void submitBreakingBlockModel(PoseStack pose, BlockStateModel model, long seed, int progress) {
        delegate.submitBreakingBlockModel(pose, model, seed, progress);
    }

    @Override
    public void submitItem(PoseStack pose, ItemDisplayContext context, int light, int overlay, int outline, int[] tints, List<BakedQuad> quads,
                           ItemStackRenderState.FoilType foil) {
        delegate.submitItem(pose, context, light, overlay, outline, tints, quads, foil);
    }

    @Override
    public void submitCustomGeometry(PoseStack pose, RenderType type, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        delegate.submitCustomGeometry(pose, swap(type), renderer);
    }

    @Override
    public void submitParticleGroup(SubmitNodeCollector.ParticleGroupRenderer renderer) {
        delegate.submitParticleGroup(renderer);
    }
}
