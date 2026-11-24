package nadiendev.ThaumaturgeTechCompats.edible;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.world.item.Item;
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
            REGISTERED.add(ITEMS.register(entry.id(), () -> new EdibleItem(entry, new Item.Properties()
                    .stacksTo(entry.kind().stackSize())
                    .food(entry.kind().food(entry.tier())))));
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
}
