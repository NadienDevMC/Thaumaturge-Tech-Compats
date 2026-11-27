package nadiendev.ThaumaturgeTechCompats.mixin;

import com.leclowndu93150.thaumaturge.content.entity.EntityCausalityCollapser;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCausalityCollapser.class)
public abstract class EntityCausalityCollapserMixin {
    /** The blast of the collapser keeps its force but does not destroy blocks while the option is on (default). */
    @WrapOperation(method = "detonate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"),
            require = 0)
    private void ttc$noBlockDamage(ServerLevel level, Entity source, double x, double y, double z, float power,
                                   Level.ExplosionInteraction interaction, Operation<Void> original) {
        if (CompatToggles.enabled(CompatToggles.RIFT_COLLAPSER_SAFE)) {
            interaction = Level.ExplosionInteraction.NONE;
        }
        original.call(level, source, x, y, z, power, interaction);
    }
}
