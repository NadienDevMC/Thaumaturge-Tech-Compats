package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public final class EdibleItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgeTechCompats.MOD_ID);
    private static final List<DeferredItem<Item>> REGISTERED = new ArrayList<>();

    private EdibleItems() {
    }

    public static void register(IEventBus modBus) {
        for (EdibleEntry entry : EdibleCatalog.entries()) {
            if (!entry.alwaysLoaded() && !ModList.get().isLoaded(entry.original().getNamespace())) {
                continue;
            }
            REGISTERED.add(ITEMS.registerItem(entry.id(), properties -> new EdibleItem(entry, properties
                    .stacksTo(entry.kind().stackSize())
                    .food(EdibleKind.food(entry.tier()), consumable(entry)))));
        }
        ITEMS.register(modBus);
    }

    public static List<Item> items() {
        List<Item> items = new ArrayList<>();
        for (DeferredItem<Item> holder : REGISTERED) {
            items.add(holder.get());
        }
        return items;
    }

    private static Consumable consumable(EdibleEntry entry) {
        Consumable base = Consumables.DEFAULT_FOOD;
        return new Consumable(base.consumeSeconds(), base.animation(), base.sound(), base.hasConsumeParticles(),
                List.of(new ApplyStatusEffectsConsumeEffect(entry.kind().effects(entry.tier()), 1.0F)));
    }
}
