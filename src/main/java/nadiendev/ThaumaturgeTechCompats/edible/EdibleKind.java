package nadiendev.ThaumaturgeTechCompats.edible;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

import java.util.List;

/**
 * Item classes and what eating them gives. The tier (0..3) is the amplifier of every effect.
 * Weapons give health boost plus regeneration, the rest give a single effect.
 */
public enum EdibleKind {
    PICKAXE(16, MobEffects.DIG_SPEED),
    WEAPON(16, MobEffects.HEALTH_BOOST, MobEffects.REGENERATION),
    AXE(16, MobEffects.MOVEMENT_SPEED),
    HOE(16, MobEffects.LUCK),
    SHOVEL(16, MobEffects.JUMP),
    GOGGLES(16, MobEffects.NIGHT_VISION),
    INGOT(64, MobEffects.ABSORPTION),
    GEM(64, MobEffects.NIGHT_VISION),
    DOOR(64, MobEffects.DAMAGE_RESISTANCE);

    /** Base duration in ticks (30 s) and extra ticks per tier (15 s). */
    private static final int BASE_TICKS = 600;
    private static final int TICKS_PER_TIER = 300;

    private final int stackSize;
    private final List<Holder<MobEffect>> effects;

    @SafeVarargs
    EdibleKind(int stackSize, Holder<MobEffect>... effects) {
        this.stackSize = stackSize;
        this.effects = List.of(effects);
    }

    public int stackSize() {
        return stackSize;
    }

    public static int duration(int tier) {
        return BASE_TICKS + TICKS_PER_TIER * tier;
    }

    /** Small food value that grows with the tier; always edible; effects always apply. */
    public FoodProperties food(int tier) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(1 + tier)
                .saturationModifier(0.3F + 0.2F * tier)
                .alwaysEdible();
        for (Holder<MobEffect> effect : effects) {
            builder.effect(() -> new MobEffectInstance(effect, duration(tier), tier), 1.0F);
        }
        return builder.build();
    }
}
