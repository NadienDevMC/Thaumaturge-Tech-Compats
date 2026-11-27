package nadiendev.ThaumaturgeTechCompats.research.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/** Tab lists of the Thaumonomicon model (package-private class): left column (built-in) and right column (add-ons). */
@Mixin(targets = "com.leclowndu93150.thaumaturge.client.screen.research.BrowserModel")
public interface BrowserModelAccessor {
    @Accessor("builtIn")
    List<Object> ttc$builtIn();

    @Accessor("addOn")
    List<Object> ttc$addOn();
}
