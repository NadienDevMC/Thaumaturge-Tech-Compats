package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.content.aspect.AspectIndex;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AspectIndex.class)
public interface AspectIndexAccessor {
    @Accessor("byItem")
    Reference2ObjectMap<Item, AspectIndex.ItemEntry> ttc$byItem();
}
