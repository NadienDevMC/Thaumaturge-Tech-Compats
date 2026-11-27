package nadiendev.ThaumaturgeTechCompats.smelter;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.aspect.AspectIndex;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.mixin.AspectIndexAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * The Thaumaturge Essentia Smelter holds 256 essentia and refuses any item whose aspects add up to more than what is free, so
 * the items of this addon whose recipes are rich (flux cores, solar panel controllers, jars...) showed their aspects in JEI and
 * in the tooltip but dissolved into nothing. After every aspect index load the totals of our items are scaled down, keeping the
 * proportions, so that each of them fits in the smelter. JEI and tooltips read the same index, so they show the same values.
 */
public final class SmelterAspectCap {
    /** Largest total a modded item (anything but vanilla and Thaumaturge) keeps. Below the 256 of the smelter so it can still run with some essentia inside. */
    public static final int MAX_TOTAL = 192;

    private SmelterAspectCap() {
    }

    public static AspectIndex apply(AspectIndex index) {
        if (index == null || !((Object) index instanceof AspectIndexAccessor accessor)) {
            return index;
        }
        for (Item item : BuiltInRegistries.ITEM) {
            String ns = BuiltInRegistries.ITEM.getKey(item).getNamespace();
            if ("minecraft".equals(ns) || "thaumaturge".equals(ns)) {
                continue;
            }
            AspectIndex.ItemEntry entry = accessor.ttc$byItem().get(item);
            if (entry == null) {
                continue;
            }
            AspectList capped = cap(entry.base());
            if (capped != entry.base()) {
                accessor.ttc$byItem().put(item, new AspectIndex.ItemEntry(capped, entry.variants()));
            }
        }
        return index;
    }

    static AspectList cap(AspectList list) {
        int total = list.totalAmount();
        if (total <= MAX_TOTAL) {
            return list;
        }
        List<AspectInstance> scaled = new ArrayList<>();
        for (AspectInstance instance : list.entries()) {
            int amount = Math.max(1, (int) Math.floor(instance.amount() * (double) MAX_TOTAL / total));
            scaled.add(new AspectInstance(instance.aspect(), amount));
        }
        return AspectList.ofEntries(scaled);
    }
}
