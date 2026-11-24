package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.resources.Identifier;

/** One edible copy: the original item, its class and its tier. */
public record EdibleEntry(Identifier original, EdibleKind kind, int tier) {
    public String id() {
        return "edible_" + original.getPath();
    }

    public Identifier itemId() {
        return ThaumaturgeTechCompats.id(id());
    }

    /** Spears have a separate in-hand model. */
    public boolean spear() {
        return original.getPath().endsWith("_spear");
    }

    /** Vanilla and the Thaumaturge hard dependency always exist; other mods need a loaded check. */
    public boolean alwaysLoaded() {
        return original.getNamespace().equals("minecraft") || original.getNamespace().equals(EdibleCatalog.THAUMATURGE);
    }
}
