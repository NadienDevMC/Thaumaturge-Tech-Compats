package nadiendev.ThaumaturgeTechCompats.materials.modifier;

import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Iterator;

/**
 * Behaviour of the four extra Hephaestus Tools modifiers (they are applied in the Arsenal Table, Modify tab) (see {@link ToolModifier}). Hephaestus Tools has no data
 * format for modifiers, so the levels live in the tool like its own modifiers and the effects are plain events.
 */
public final class ToolModifierEvents {
    public static final String HEPHAESTUS_TOOLS = "hephaestus_tools";

    /** Vis taken from the aura for every durability point mended. */
    private static final float VIS_PER_POINT = 0.02F;

    private ToolModifierEvents() {
    }

    public static void register(IEventBus modBus) {
        if (!ModList.get().isLoaded(HEPHAESTUS_TOOLS)) {
            return;
        }
        modBus.addListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(() -> {
            if (!HephaestusLink.init()) {
                return;
            }
            HephaestusLink.registerEffects();
            HephaestusLink.registerUpgrades();
            NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, GetEnchantmentLevelEvent.class, ToolModifierEvents::onEnchantments);
            NeoForge.EVENT_BUS.addListener(PlayerTickEvent.Post.class, ToolModifierEvents::onPlayerTick);
            NeoForge.EVENT_BUS.addListener(BlockDropsEvent.class, ToolModifierEvents::onBlockDrops);
            NeoForge.EVENT_BUS.addListener(LivingDropsEvent.class, ToolModifierEvents::onMobDrops);
            NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.RightClickItem.class, ToolModifierEvents::onUse);
            NeoForge.EVENT_BUS.addListener(LivingDamageEvent.Post.class, ToolModifierEvents::onHit);
        }));
    }

    // Unbreakable: Unbreaking enchantment that grows quickly with the level (the maximum level is handled by the
    // indestructible stat registered in HephaestusLink).

    private static void onEnchantments(GetEnchantmentLevelEvent event) {
        if (!(event.getStack() instanceof ItemStack stack) || !event.isTargetting(Enchantments.UNBREAKING)) {
            return;
        }
        int level = HephaestusLink.level(stack, ToolModifier.UNBREAKABLE);
        if (level <= 0) {
            return;
        }
        int unbreaking = 2 * level * level;
        event.getHolder(Enchantments.UNBREAKING).ifPresent(holder -> {
            if (event.getEnchantments().getLevel(holder) < unbreaking) {
                event.getEnchantments().set(holder, unbreaking);
            }
        });
    }

    // Vis Repair: every two seconds, from the aura if it has vis, otherwise very slowly.

    private static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level) || player.tickCount % 40 != 0) {
            return;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            int modLevel = HephaestusLink.level(stack, ToolModifier.VIS_REPAIR);
            if (modLevel <= 0) {
                continue;
            }
            int damage = HephaestusLink.damage(stack);
            if (damage <= 0) {
                continue;
            }
            int points = Math.min(damage, 5 * modLevel);
            float cost = points * VIS_PER_POINT;
            if (AuraHelper.drainVis(level, player.blockPosition(), cost, true) >= cost - 1.0E-4F) {
                AuraHelper.drainVis(level, player.blockPosition(), cost, false);
            } else {
                points = Math.min(damage, Math.max(1, modLevel / 6));
            }
            HephaestusLink.setToolDamage(stack, damage - points);
        }
    }

    // Void Teleport: drops of mined blocks and of slain mobs go to the inventory.

    private static float voidChance(int level) {
        return Math.min(1.0F, level / 8.0F);
    }

    private static void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof ServerPlayer player)) {
            return;
        }
        int level = HephaestusLink.level(event.getTool(), ToolModifier.VOID_TELEPORT);
        if (level > 0) {
            collect(player, event.getDrops().iterator(), voidChance(level));
        }
    }

    private static void onMobDrops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }
        int level = HephaestusLink.level(player.getMainHandItem(), ToolModifier.VOID_TELEPORT);
        if (level > 0) {
            collect(player, event.getDrops().iterator(), voidChance(level));
        }
    }

    private static void collect(ServerPlayer player, Iterator<ItemEntity> drops, float chance) {
        RandomSource random = player.getRandom();
        while (drops.hasNext()) {
            ItemEntity drop = drops.next();
            if (chance < 1.0F && random.nextFloat() >= chance) {
                continue;
            }
            ItemStack stack = drop.getItem();
            player.getInventory().add(stack);
            if (stack.isEmpty()) {
                drops.remove();
            }
        }
    }

    // Ender Teleport: use to blink forward, hits may blink the player away.

    private static void onUse(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        int level = HephaestusLink.level(stack, ToolModifier.ENDER_TELEPORT);
        if (level <= 0 || player.getCooldowns().isOnCooldown(stack)) {
            return;
        }
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double range = 6.0D + 2.0D * level;
        BlockHitResult hit = player.level().clip(new ClipContext(eye, eye.add(look.scale(range)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        double distance = hit.getType() == HitResult.Type.MISS ? range : Math.max(0.0D, eye.distanceTo(hit.getLocation()) - 0.6D);
        for (double d = distance; d >= 1.0D; d -= 1.0D) {
            Vec3 target = eye.add(look.scale(d));
            if (player.randomTeleport(target.x, target.y - player.getEyeHeight(), target.z, true)) {
                afterBlink(player);
                player.getCooldowns().addCooldown(stack, Math.max(10, 60 - 4 * level));
                return;
            }
        }
    }

    private static void onHit(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player) || event.getEntity() == player) {
            return;
        }
        int level = HephaestusLink.level(player.getMainHandItem(), ToolModifier.ENDER_TELEPORT);
        if (level <= 0 || player.getRandom().nextFloat() >= 0.03F * level) {
            return;
        }
        RandomSource random = player.getRandom();
        double radius = 3.0D + level / 2.0D;
        for (int i = 0; i < 16; i++) {
            double x = player.getX() + (random.nextDouble() - 0.5D) * 2.0D * radius;
            double y = player.getY() + random.nextInt(5) - 2;
            double z = player.getZ() + (random.nextDouble() - 0.5D) * 2.0D * radius;
            if (player.randomTeleport(x, y, z, true)) {
                afterBlink(player);
                return;
            }
        }
    }

    private static void afterBlink(LivingEntity entity) {
        entity.resetFallDistance();
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
