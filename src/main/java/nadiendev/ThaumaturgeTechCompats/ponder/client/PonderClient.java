package nadiendev.ThaumaturgeTechCompats.ponder.client;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Ponder is optional (it ships with Create). Not an {@code @EventBusSubscriber}, so this class is only loaded
 * when {@link #register} is called, which the main class does only if Ponder is installed.
 */
public final class PonderClient {
    private PonderClient() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(PonderClient::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new TuuPonderPlugin());
    }
}
