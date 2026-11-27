package nadiendev.ThaumaturgeTechCompats.compats.enderio.conduit;

import com.enderio.enderio.api.EnderIORegistries;
import com.enderio.enderio.api.conduits.Conduit;
import com.enderio.enderio.api.conduits.ConduitApi;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.enderio.EnderIOTier;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public final class ConduitRegistry {
    private ConduitRegistry() {
    }

    /**
     * Ender IO 26.1 conduits are a data-driven registry (enderio:conduit) and the item is a single
     * enderio:conduit item with the enderio:conduit data component, so the stacks are built from the registry.
     */
    public static void fillCreativeTab(HolderLookup.Provider provider, CreativeModeTab.Output output) {
        if (provider == null) {
            return;
        }
        HolderLookup.RegistryLookup<Conduit<?, ?>> conduits = provider.lookup(EnderIORegistries.Keys.CONDUIT).orElse(null);
        if (conduits == null) {
            return;
        }
        for (ConduitKind kind : ConduitKind.values()) {
            for (EnderIOTier tier : EnderIOTier.values()) {
                ResourceKey<Conduit<?, ?>> key = ResourceKey.create(EnderIORegistries.Keys.CONDUIT, ThaumaturgeTechCompats.id(kind.name(tier)));
                conduits.get(key).ifPresent(holder -> output.accept(ConduitApi.INSTANCE.getConduitItem(holder)));
            }
        }
    }
}
