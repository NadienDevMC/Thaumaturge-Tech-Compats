package nadiendev.ThaumaturgeTechCompats.taintedfood.farmersdelight;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import nadiendev.ThaumaturgeTechCompats.taintedfood.OriginalItemComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class FarmersDelightTaintedItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeTechCompats.MOD_ID);
    private static final Map<FarmersDelightFood, DeferredItem<Item>> FOODS = new EnumMap<>(FarmersDelightFood.class);
    private static final Map<FarmersDelightKnife, DeferredItem<Item>> KNIVES = new EnumMap<>(FarmersDelightKnife.class);
    private static DeferredItem<Item> fluxKnife;

    private static final float KNIFE_BASE_DAMAGE = 0.5F;

    private FarmersDelightTaintedItems() {
    }

    public static void register(IEventBus modBus, boolean foods, boolean knives, boolean fluxKnifeEnabled) {
        if (foods) {
            FoodProperties placeholder = new FoodProperties(1, 0.1F, false);
            for (FarmersDelightFood food : FarmersDelightFood.values()) {
                if (FarmersDelightTaintedConfig.foodEnabled(food)) {
                    FOODS.put(food, ITEMS.register(food.id(), () -> new FarmersDelightFoodItem(food, foodProperties(food, placeholder))));
                }
            }
        }
        if (knives) {
            for (FarmersDelightKnife knife : FarmersDelightKnife.values()) {
                if (FarmersDelightTaintedConfig.knifeEnabled(knife)) {
                    KNIVES.put(knife, ITEMS.register(knife.id(), () -> new ThaumaturgeKnifeItem(knife, knifeProperties(knife))));
                }
            }
        }
        if (fluxKnifeEnabled) {
            fluxKnife = ITEMS.register(FluxKnife.ID, () -> new FluxKnifeItem(fluxKnifeProperties()));
        }
        ITEMS.register(modBus);
        modBus.addListener(FarmersDelightTaintedItems::onModifyDefaults);
    }

    public static List<Item> foods() {
        List<Item> items = new ArrayList<>();
        for (DeferredItem<Item> holder : FOODS.values()) {
            items.add(holder.get());
        }
        return items;
    }

    public static List<Item> knives() {
        List<Item> items = new ArrayList<>();
        for (DeferredItem<Item> holder : KNIVES.values()) {
            items.add(holder.get());
        }
        return items;
    }

    public static Item fluxKnife() {
        return fluxKnife.get();
    }

    private static Item.Properties fluxKnifeProperties() {
        return new Item.Properties().rarity(FluxKnife.RARITY)
                .tool(FluxKnife.material(), ModTags.Blocks.MINEABLE_WITH_KNIFE, KNIFE_BASE_DAMAGE, FarmersDelightTaintedConfig.fluxKnifeAttackSpeed(), 0.0F);
    }

    private static Item.Properties foodProperties(FarmersDelightFood food, FoodProperties placeholder) {
        Item.Properties properties = new Item.Properties().food(placeholder);
        if (food.bowl()) {
            properties.craftRemainder(Items.BOWL);
        }
        return properties;
    }

    private static Item.Properties knifeProperties(FarmersDelightKnife knife) {
        return new Item.Properties().rarity(knife.rarity())
                .tool(knife.material(), ModTags.Blocks.MINEABLE_WITH_KNIFE, KNIFE_BASE_DAMAGE, FarmersDelightTaintedConfig.attackSpeed(knife), 0.0F);
    }

    private static void onModifyDefaults(ModifyDefaultComponentsEvent event) {
        for (Map.Entry<FarmersDelightFood, DeferredItem<Item>> entry : FOODS.entrySet()) {
            Identifier originalId = entry.getKey().original();
            // Original components are resolved while this item's defaults are built (holders are not bound yet)
            event.modify(entry.getValue().get(), (builder, context, item) -> {
                DataComponentMap components = OriginalItemComponents.get(context, originalId);
                if (components == null) {
                    return;
                }
                FoodProperties food = components.get(DataComponents.FOOD);
                if (food == null) {
                    return;
                }
                builder.set(DataComponents.FOOD, taint(food));
                builder.set(DataComponents.CONSUMABLE, taint(components.getOrDefault(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD)));
                UseRemainder remainder = components.get(DataComponents.USE_REMAINDER);
                if (remainder != null) {
                    builder.set(DataComponents.USE_REMAINDER, remainder);
                }
                builder.set(DataComponents.MAX_STACK_SIZE, components.getOrDefault(DataComponents.MAX_STACK_SIZE, 64));
                Rarity rarity = components.get(DataComponents.RARITY);
                if (rarity != null) {
                    builder.set(DataComponents.RARITY, rarity);
                }
            });
        }
    }

    private static FoodProperties taint(FoodProperties food) {
        int nutrition = Math.max(1, Math.round(food.nutrition() * FarmersDelightTaintedConfig.hungerMultiplier()));
        float saturation = food.saturation() * FarmersDelightTaintedConfig.saturationMultiplier();
        return new FoodProperties(nutrition, saturation, food.canAlwaysEat());
    }

    private static Consumable taint(Consumable consumable) {
        List<ConsumeEffect> effects = new ArrayList<>(consumable.onConsumeEffects());
        if (FarmersDelightTaintedConfig.foodEffectDuration() > 0 && FarmersDelightTaintedConfig.foodEffectChance() > 0.0F) {
            effects.add(new ApplyStatusEffectsConsumeEffect(
                    List.of(FarmersDelightTaintedEffect.create(
                            FarmersDelightTaintedConfig.foodEffectId(),
                            FarmersDelightTaintedConfig.foodEffectDuration(),
                            FarmersDelightTaintedConfig.foodEffectAmplifier())),
                    FarmersDelightTaintedConfig.foodEffectChance()));
        }
        return new Consumable(consumable.consumeSeconds(), consumable.animation(), consumable.sound(), consumable.hasConsumeParticles(), effects);
    }
}
