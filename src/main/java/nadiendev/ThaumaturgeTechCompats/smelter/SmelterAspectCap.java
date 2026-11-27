package nadiendev.ThaumaturgeTechCompats.smelter;

import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.aspect.AspectIndex;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.mixin.AspectIndexAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Thaumaturge Essentia Smelter holds at most 256 points of essentia and only starts an item whose aspects fit in what is
 * left, so any item worth more than that sits in the input slot forever. Aspects derived from recipes make a lot of our items
 * (cores, cells, jars, solar parts) worth far more, so after the aspect index is built (or loaded from its cache) the items of
 * this mod above the limit are scaled down, keeping the proportions, to an amount the smelter can dissolve.
 */
public final class SmelterAspectCap {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmelterAspectCap.class);

    /** Total points the smelter can hold. Items above this can never be dissolved. */
    public static final int SMELTER_CAPACITY = 256;
    /** Total an oversized item is scaled to, leaving some room in the smelter for essentia that is still inside. */
    public static final int TARGET_TOTAL = 192;

    private SmelterAspectCap() {
    }

    public static AspectIndex apply(AspectIndex index) {
        if (index == null || index.isEmpty()) {
            return index;
        }
        Map<Item, AspectIndex.ItemEntry> source = ((AspectIndexAccessor) (Object) index).ttc$byItem();
        Map<Item, AspectIndex.ItemEntry> result = new HashMap<>(source.size());
        int capped = 0;
        for (Map.Entry<Item, AspectIndex.ItemEntry> entry : source.entrySet()) {
            AspectIndex.ItemEntry value = entry.getValue();
            if (isOurs(entry.getKey()) && value.base().totalAmount() > SMELTER_CAPACITY) {
                value = new AspectIndex.ItemEntry(scale(value.base()), value.variants());
                capped++;
            }
            result.put(entry.getKey(), value);
        }
        if (capped == 0) {
            return index;
        }
        LOGGER.info("Scaled the aspects of {} modded items down to {} points so the Essentia Smelter can dissolve them", capped, TARGET_TOTAL);
        return AspectIndex.ofEntries(result);
    }

    private static boolean isOurs(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        // Vanilla and Thaumaturge keep their own values; every other mod (ours and the compat mods) is capped.
        return !"minecraft".equals(id.getNamespace()) && !"thaumaturge".equals(id.getNamespace());
    }

    private static AspectList scale(AspectList list) {
        int total = list.totalAmount();
        List<AspectInstance> scaled = new ArrayList<>(list.entries().size());
        int sum = 0;
        for (AspectInstance instance : list.entries()) {
            int amount = Math.max(1, Math.round(instance.amount() * (float) TARGET_TOTAL / total));
            scaled.add(instance.withAmount(amount));
            sum += amount;
        }
        while (sum > SMELTER_CAPACITY) {
            int largest = 0;
            for (int i = 1; i < scaled.size(); i++) {
                if (scaled.get(i).amount() > scaled.get(largest).amount()) {
                    largest = i;
                }
            }
            AspectInstance instance = scaled.get(largest);
            if (instance.amount() <= 1) {
                break;
            }
            scaled.set(largest, instance.withAmount(instance.amount() - 1));
            sum--;
        }
        return AspectList.ofEntries(scaled);
    }
}
