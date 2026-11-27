package nadiendev.ThaumaturgeTechCompats.taintedfood;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Reads the pending default components of other items while default components are still being built
 * (item holders are not bound yet at that point, so {@code Item#components()} throws).
 */
public final class OriginalItemComponents {
    private static boolean resolving;
    private static HolderLookup.Provider cachedContext;
    private static Map<Identifier, DataComponentMap> cache;

    private OriginalItemComponents() {
    }

    /** Returns the pending components of the item, or null if unknown or called re-entrantly. */
    public static synchronized DataComponentMap get(HolderLookup.Provider context, Identifier id) {
        if (resolving) {
            return null;
        }
        if (cache == null || cachedContext != context) {
            resolving = true;
            try {
                Map<Identifier, DataComponentMap> result = new HashMap<>();
                for (DataComponentInitializers.PendingComponents<?> pending : BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(context)) {
                    if (pending.key().equals(Registries.ITEM)) {
                        pending.forEach((holder, components) -> result.put(holder.key().identifier(), components));
                    }
                }
                cache = result;
                cachedContext = context;
            } finally {
                resolving = false;
            }
        }
        return cache.get(id);
    }
}
