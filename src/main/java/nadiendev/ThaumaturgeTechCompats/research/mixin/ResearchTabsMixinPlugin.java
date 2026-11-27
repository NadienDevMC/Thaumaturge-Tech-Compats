package nadiendev.ThaumaturgeTechCompats.research.mixin;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * The Thaumonomicon tab mixin is optional (require = 0): if Thaumaturge changes its book screen, the tabs just keep
 * the texture icons and Thaumaturge's own columns. This plugin logs a warning for every hook that did not apply.
 */
public final class ResearchTabsMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger("thaumaturgetechcompats");
    /** Handlers of {@link ThaumonomiconTabsMixin}; each must be called from the screen once applied. */
    private static final List<String> HANDLERS = List.of("ttcLeftColumn", "ttcRememberTab", "ttcTabIcon");

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (!mixinClassName.endsWith(".ThaumonomiconTabsMixin")) {
            return;
        }
        for (String handler : HANDLERS) {
            if (!called(targetClass, handler)) {
                LOGGER.warn("Thaumonomicon tab hook '{}' was not applied to {}; our tabs keep the texture icon and Thaumaturge's own columns",
                        handler, targetClassName);
            }
        }
    }

    private static boolean called(ClassNode target, String handler) {
        for (MethodNode method : target.methods) {
            for (AbstractInsnNode insn : method.instructions) {
                if (insn instanceof MethodInsnNode call && call.name.endsWith(handler)) {
                    return true;
                }
            }
        }
        return false;
    }
}
