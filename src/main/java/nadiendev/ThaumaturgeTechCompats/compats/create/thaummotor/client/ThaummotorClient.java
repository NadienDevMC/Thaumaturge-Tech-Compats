package nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.client;

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorBlockEntity;
import nadiendev.ThaumaturgeTechCompats.compats.create.thaummotor.ThaummotorRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

/**
 * Create client hooks. Not an {@code @EventBusSubscriber}: NeoForge would load this class (and its Create imports)
 * at startup even without Create. {@link #register} is only called when Create is installed.
 */
public final class ThaummotorClient {
    private ThaummotorClient() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ThaummotorClient::registerRenderers);
        modBus.addListener(ThaummotorClient::registerPartialModels);
        modBus.addListener(ThaummotorClient::registerVisual);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ThaummotorRegistry.THAUMMOTOR_ENTITY.get(), ThaummotorRenderer::new);
    }

    private static void registerPartialModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(ThaummotorPartials.SHAFT_HALF.modelLocation()));
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
