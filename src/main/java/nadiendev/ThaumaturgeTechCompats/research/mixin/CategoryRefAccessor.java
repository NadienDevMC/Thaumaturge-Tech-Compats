package nadiendev.ThaumaturgeTechCompats.research.mixin;

import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Id and data of one Thaumonomicon tab (package-private record). */
@Mixin(targets = "com.leclowndu93150.thaumaturge.client.screen.research.BrowserModel$CategoryRef")
public interface CategoryRefAccessor {
    @Accessor("id")
    Identifier ttc$id();

    @Accessor("data")
    IResearchCategory ttc$data();
}
