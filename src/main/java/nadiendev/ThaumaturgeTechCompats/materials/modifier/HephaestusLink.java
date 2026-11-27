package nadiendev.ThaumaturgeTechCompats.materials.modifier;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * Reflection bridge to Hephaestus Tools, which is not a compile dependency (so everything stays optional).
 * Hephaestus keeps its modifiers in plain Java: the stack stores (id, level) entries and a private map holds the
 * stat effect of every known id. Here we read and write those entries and add our stat effects to that map.
 */
final class HephaestusLink {
    private static final String BASE = "com.titammods.hephaestus_tools.";

    private static boolean tried;
    private static boolean ok;

    private static Method isInitialized;
    private static Method getModifiers;
    private static Method addModifier;
    private static Method recalculate;
    private static Method getCurrentDamage;
    private static Method setDamage;
    private static Method usedSlots;
    private static Method unlockedSlots;
    private static Method entryId;
    private static Method entryLevel;
    private static Constructor<?> entryCtor;
    private static Class<?> modifiableItem;
    private static Class<?> modifierEffect;
    private static Field indestructible;
    private static Map<Object, Object> effects;

    private HephaestusLink() {
    }

    @SuppressWarnings("unchecked")
    static synchronized boolean init() {
        if (tried) {
            return ok;
        }
        tried = true;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> toolStack = Class.forName(BASE + "tools.nbt.ToolStack", true, loader);
            Class<?> entry = Class.forName(BASE + "tools.nbt.ToolConstructionData$ModifierEntry", true, loader);
            Class<?> upgrades = Class.forName(BASE + "table.ToolUpgrades", true, loader);
            Class<?> effectsClass = Class.forName(BASE + "tools.modifier.ModifierEffects", true, loader);
            Class<?> statContext = Class.forName(BASE + "tools.modifier.ModifierStatContext", true, loader);
            modifiableItem = Class.forName(BASE + "tools.item.ModifiableItem", true, loader);
            modifierEffect = Class.forName(BASE + "tools.modifier.ModifierEffect", true, loader);

            isInitialized = toolStack.getMethod("isInitialized", ItemStack.class);
            getModifiers = toolStack.getMethod("getModifiers", ItemStack.class);
            addModifier = toolStack.getMethod("addModifier", ItemStack.class, entry);
            recalculate = toolStack.getMethod("recalculate", ItemStack.class);
            getCurrentDamage = toolStack.getMethod("getCurrentDamage", ItemStack.class);
            setDamage = toolStack.getMethod("setDamage", ItemStack.class, int.class);
            usedSlots = upgrades.getMethod("usedSlots", ItemStack.class);
            unlockedSlots = upgrades.getMethod("unlockedSlots", ItemStack.class);
            entryCtor = entry.getConstructor(Identifier.class, int.class);
            entryId = entry.getMethod("id");
            entryLevel = entry.getMethod("level");
            indestructible = statContext.getField("indestructible");

            Field map = effectsClass.getDeclaredField("EFFECTS");
            map.setAccessible(true);
            effects = (Map<Object, Object>) map.get(null);
            ok = true;
        } catch (Throwable t) {
            ok = false;
        }
        return ok;
    }

    /** True for an assembled Hephaestus tool (cheap class check first, since this runs on hot events). */
    static boolean isTool(ItemStack stack) {
        if (!ok || stack.isEmpty() || !modifiableItem.isInstance(stack.getItem())) {
            return false;
        }
        try {
            return (Boolean) isInitialized.invoke(null, stack);
        } catch (Throwable t) {
            return false;
        }
    }

    /** Level of one of our modifiers on the stack, 0 when it has none. */
    static int level(ItemStack stack, ToolModifier modifier) {
        if (!isTool(stack)) {
            return 0;
        }
        try {
            Identifier wanted = modifier.id();
            for (Object entry : (List<?>) getModifiers.invoke(null, stack)) {
                if (wanted.equals(entryId.invoke(entry))) {
                    return (Integer) entryLevel.invoke(entry);
                }
            }
        } catch (Throwable t) {
            // no level
        }
        return 0;
    }

    /** Sets the modifier to the given level and recomputes the tool. */
    static boolean setLevel(ItemStack stack, ToolModifier modifier, int level) {
        try {
            addModifier.invoke(null, stack, entryCtor.newInstance(modifier.id(), level));
            recalculate.invoke(null, stack);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    static boolean hasFreeSlot(ItemStack stack) {
        try {
            return (Integer) usedSlots.invoke(null, stack) < (Integer) unlockedSlots.invoke(null, stack);
        } catch (Throwable t) {
            return true;
        }
    }

    static int damage(ItemStack stack) {
        try {
            return (Integer) getCurrentDamage.invoke(null, stack);
        } catch (Throwable t) {
            return 0;
        }
    }

    static void setToolDamage(ItemStack stack, int damage) {
        try {
            setDamage.invoke(null, stack, damage);
        } catch (Throwable t) {
            // ignored
        }
    }

    /**
     * Adds our modifiers to the Arsenal Table upgrade list (the Modify tab): the table lists every entry of
     * ToolUpgrades.REGISTRY whose roles match the tool, so all three roles are given. Cost is fixed by a mixin.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    static void registerUpgrades() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> upgradesClass = Class.forName(BASE + "table.ToolUpgrades", true, loader);
            Class<?> upgradeClass = Class.forName(BASE + "table.ToolUpgrade", true, loader);
            Class<? extends Enum> roleClass = (Class<? extends Enum>) Class.forName(BASE + "table.ToolRole", true, loader);
            // Builds the built-in list first, so ours are added after it.
            upgradesClass.getMethod("availableFor", net.minecraft.world.item.Item.class)
                    .invoke(null, net.minecraft.world.item.Items.AIR);
            List<Object> registry = (List<Object>) upgradesClass.getField("REGISTRY").get(null);
            Constructor<?> ctor = upgradeClass.getConstructor(Identifier.class, String.class, java.util.Set.class,
                    int.class, net.minecraft.world.item.Item.class, int.class, Identifier.class);
            for (ToolModifier modifier : ToolModifier.values()) {
                if (!net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(modifier.ingot())) {
                    continue;
                }
                net.minecraft.world.item.Item cost = net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(modifier.ingot());
                java.util.EnumSet roles = java.util.EnumSet.noneOf(roleClass);
                roles.addAll(java.util.Arrays.asList(roleClass.getEnumConstants()));
                // The Arsenal Table draws the Modify tab icon from hephaestus_tools:textures/gui/forge_table/modifier_icon/
                // modifier_<upgrade id path>.png (namespace hardcoded), so our PNGs ship under assets/hephaestus_tools/.
                Identifier icon = Identifier.fromNamespaceAndPath("hephaestus_tools",
                        "textures/gui/forge_table/modifier_icon/modifier_" + modifier.id().getPath() + ".png");
                registry.add(ctor.newInstance(modifier.id(), modifier.nameKey(), roles, ToolModifier.MAX_LEVEL, cost, 1, icon));
            }
        } catch (Throwable t) {
            org.slf4j.LoggerFactory.getLogger(HephaestusLink.class)
                    .error("Could not add the extra upgrades to the Hephaestus Tools Arsenal Table", t);
        }
    }

    /** Adds the stat effect of our modifiers: at the maximum level the tool becomes indestructible. */
    static void registerEffects() {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("apply") && args != null && args.length == 2) {
                if ((Integer) args[1] >= ToolModifier.MAX_LEVEL) {
                    indestructible.setBoolean(args[0], true);
                }
                return null;
            }
            if (method.getDeclaringClass() == Object.class) {
                return switch (method.getName()) {
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> "ThaumaturgeTechCompats modifier effect";
                };
            }
            return null;
        };
        Object effect = Proxy.newProxyInstance(modifierEffect.getClassLoader(), new Class<?>[]{modifierEffect}, handler);
        for (ToolModifier modifier : ToolModifier.values()) {
            effects.put(modifier.id(), effect);
        }
    }
}
