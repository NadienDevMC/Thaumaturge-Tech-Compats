package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.resources.ResourceLocation;

/** One edible copy: the original item, its class and its tier. */
public record EdibleEntry(ResourceLocation original, EdibleKind kind, int tier) {
    public String id() {
        return "edible_" + original.getPath();
    }

    public ResourceLocation itemId() {
        return ThaumaturgeTechCompats.id(id());
    }

    /** Vanilla and the Thaumaturge hard dependency always exist; other mods need a loaded check. */
    public boolean alwaysLoaded() {
        return original.getNamespace().equals("minecraft") || original.getNamespace().equals(EdibleCatalog.THAUMATURGE);
    }
}
