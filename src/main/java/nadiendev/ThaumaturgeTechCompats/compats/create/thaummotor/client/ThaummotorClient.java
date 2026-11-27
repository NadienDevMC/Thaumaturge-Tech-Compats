package nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.client;

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorBlockEntity;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorRegistry;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueLabelRenderer;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Create client hooks. Not an {@code @EventBusSubscriber}: NeoForge would load this class (and its Create imports)
 * at startup even without Create. {@link #register} is only called when Create is installed.
 * The shaft partial model is registered by Flywheel itself when {@link ThaummotorPartials} is initialised.
 */
public final class ThaummotorClient {
    private ThaummotorClient() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ThaummotorClient::registerRenderers);
        modBus.addListener(ThaummotorClient::registerVisual);
        NeoForge.EVENT_BUS.addListener(ThaummotorClient::submitValueBox);
    }

    /**
     * Draws the speed number and the value box brackets with Create's own motor label renderer, the same way Create
     * does for its creative motor (its hook is hard-wired to CreativeMotorBlockEntity).
     */
    private static void submitValueBox(SubmitCustomGeometryEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || !(minecraft.hitResult instanceof BlockHitResult hit)) {
            return;
        }
        if (!(minecraft.level.getBlockEntity(hit.getBlockPos()) instanceof ThaummotorBlockEntity blockEntity)) {
            return;
        }
        ScrollValueBehaviour speed = blockEntity.getBehaviour(ScrollValueBehaviour.TYPE);
        if (speed == null) {
            return;
        }
        var camera = event.getLevelRenderState().cameraRenderState;
        Vec3 cameraPos = camera.pos;
        var pos = blockEntity.getBlockPos();
        var stack = event.getPoseStack();
        stack.pushPose();
        stack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);
        ScrollValueLabelRenderer.submitMotor(pos, blockEntity.getBlockState().getValue(ThaummotorBlock.FACING),
                speed.getValue(), null, stack, event.getSubmitNodeCollector(), null);
        stack.popPose();
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ThaummotorRegistry.THAUMMOTOR_ENTITY.get(), ThaummotorRenderer::new);
    }

    private static void registerVisual(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SimpleBlockEntityVisualizer.<ThaummotorBlockEntity>builder(ThaummotorRegistry.THAUMMOTOR_ENTITY.get())
                    .factory(OrientedRotatingVisual.of(ThaummotorPartials.SHAFT_HALF))
                    .skipVanillaRender(blockEntity -> true)
                    .apply();
        });
    }
}
