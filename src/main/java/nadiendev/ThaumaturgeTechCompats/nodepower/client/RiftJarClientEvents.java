package nadiendev.ThaumaturgeTechCompats.nodepower.client;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.nodepower.NodePowerRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

@EventBusSubscriber(modid = ThaumaturgeTechCompats.MOD_ID, value = Dist.CLIENT)
public final class RiftJarClientEvents {
    private RiftJarClientEvents() {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(NodePowerRegistry.RIFT_JAR_ENTITY.get(), RiftJarRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(RiftJarSpecialRenderer.TYPE, RiftJarSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
