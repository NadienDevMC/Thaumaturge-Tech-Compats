package nadiendev.ThaumaturgeTechCompats.device.client;

import com.leclowndu93150.thaumaturge.client.render.blockentity.AdvancedAlchemicalFurnaceRenderer;
import com.leclowndu93150.thaumaturge.client.render.blockentity.NodeStabilizerRenderer;
import com.leclowndu93150.thaumaturge.client.render.blockentity.NodeTransducerRenderer;
import com.leclowndu93150.thaumaturge.registry.TTBlockEntities;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

/**
 * Tier textures of the furnaces, stabilizers and transducers (26.1 port of the 1.21.1 FurnaceClient, StabilizerClient
 * and TransducerClient): the Thaumaturge renderers of the shared block entity types are wrapped, registered last so
 * they replace the Thaumaturge ones, and the items use the tiered_device special renderer.
 */
@EventBusSubscriber(modid = ThaumaturgeTechCompats.MOD_ID, value = Dist.CLIENT)
public final class TieredDeviceClient {
    private TieredDeviceClient() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TTBlockEntities.ADVANCED_ALCHEMICAL_FURNACE.get(),
                context -> new TieredBlockEntityRenderer<>(new AdvancedAlchemicalFurnaceRenderer(context), TieredDeviceSwaps::furnace));
        event.registerBlockEntityRenderer(TTBlockEntities.NODE_STABILIZER.get(),
                context -> new TieredBlockEntityRenderer<>(new NodeStabilizerRenderer(context), TieredDeviceSwaps::stabilizer));
        event.registerBlockEntityRenderer(TTBlockEntities.NODE_TRANSDUCER.get(),
                context -> new TieredBlockEntityRenderer<>(new NodeTransducerRenderer(context), TieredDeviceSwaps::transducer));
    }

    @SubscribeEvent
    public static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(TieredDeviceSpecialRenderer.TYPE, TieredDeviceSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
