package nadiendev.ThaumaturgeTechCompats.edible;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Item classes and what eating them gives. The tier (0..3) is the amplifier of every effect.
 * Weapons give health boost plus regeneration, the rest give a single effect.
 */
public enum EdibleKind {
    PICKAXE(16, MobEffects.HASTE),
    WEAPON(16, MobEffects.HEALTH_BOOST, MobEffects.REGENERATION),
    AXE(16, MobEffects.SPEED),
    HOE(16, MobEffects.LUCK),
    SHOVEL(16, MobEffects.JUMP_BOOST),
    GOGGLES(16, MobEffects.NIGHT_VISION),
    INGOT(64, MobEffects.ABSORPTION),
    GEM(64, MobEffects.NIGHT_VISION),
    DOOR(64, MobEffects.RESISTANCE);

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

    public List<MobEffectInstance> effects(int tier) {
        List<MobEffectInstance> instances = new ArrayList<>();
        for (Holder<MobEffect> effect : effects) {
            instances.add(new MobEffectInstance(effect, duration(tier), tier));
        }
        return instances;
    }

    /** Small food value that grows with the tier; always edible. */
    public static FoodProperties food(int tier) {
        return new FoodProperties(1 + tier, 0.3F + 0.2F * tier, true);
    }
}
