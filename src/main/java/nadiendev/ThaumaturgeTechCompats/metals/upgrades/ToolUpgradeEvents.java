package nadiendev.ThaumaturgeTechCompats.metals.upgrades;

import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.titammods.hephaestus_tools.tools.item.ModifiableItem;
import com.titammods.hephaestus_tools.tools.nbt.ToolConstructionData;
import com.titammods.hephaestus_tools.tools.nbt.ToolStack;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Iterator;

/** Behaviour of the upgrades that cannot be expressed as tool stats. Level 12 of every upgrade is the Indestructible stat. */
public final class ToolUpgradeEvents {
    private static final int REPAIR_INTERVAL = 20;
    private static final float VIS_PER_DURABILITY = 0.01F;

    private ToolUpgradeEvents() {
    }

    /** Level of an upgrade on a Hephaestus tool, 0 when it has none. */
    public static int level(ItemStack stack, ToolUpgradeKind kind) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ModifiableItem) || !ToolStack.isInitialized(stack)) {
            return 0;
        }
        for (ToolConstructionData.ModifierEntry entry : ToolStack.getModifiers(stack)) {
            if (entry.id().equals(kind.id())) {
                return entry.level();
            }
        }
        return 0;
    }

    /** Unbreakable: extra Unbreaking levels, so each durability point is skipped with growing chance. */
    @SubscribeEvent
    public static void onEnchantmentLevel(GetEnchantmentLevelEvent event) {
        int level = level(event.getStack(), ToolUpgradeKind.UNBREAKABLE);
        if (level <= 0) {
            return;
        }
        event.getLookup().get(Enchantments.UNBREAKING).ifPresent(holder -> event.getEnchantments().upgrade(holder, level));
    }

    /** Vis Repair: repairs tools in the inventory with vis of the chunk, or slowly without it. */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide() || player.tickCount % REPAIR_INTERVAL != 0) {
            return;
        }
        for (ItemStack stack : player.getInventory().items) {
            repair(player, stack);
        }
        repair(player, player.getOffhandItem());
    }

    private static void repair(Player player, ItemStack stack) {
        int level = level(stack, ToolUpgradeKind.VIS_REPAIR);
        if (level <= 0) {
            return;
        }
        int damage = ToolStack.getCurrentDamage(stack);
        if (damage <= 0) {
            return;
        }
        int wanted = Math.min(damage, level * 4);
        Level world = player.level();
        BlockPos pos = player.blockPosition();
        float cost = wanted * VIS_PER_DURABILITY;
        int repaired;
        if (AuraHelper.drainVis(world, pos, cost, true) >= cost - 0.0001F) {
            AuraHelper.drainVis(world, pos, cost, false);
            repaired = wanted;
        } else if (player.tickCount % (REPAIR_INTERVAL * 5) == 0) {
            repaired = 1;
        } else {
            return;
        }
        ToolStack.setDamage(stack, damage - repaired);
    }

    /** Void Teleport: chance for each drop to go to the inventory, level / 8, so always from level 8 (like the 26.1.2 branch). */
    private static float voidChance(int level) {
        return Math.min(1.0F, level / 8.0F);
    }

    /** Void Teleport: mined blocks go straight into the inventory. */
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof ServerPlayer player)) {
            return;
        }
        int level = level(event.getTool(), ToolUpgradeKind.VOID_TELEPORT);
        if (level > 0) {
            collect(player, event.getDrops().iterator(), voidChance(level));
        }
    }

    /** Void Teleport: drops of mobs killed with the weapon go straight into the inventory. */
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }
        int level = level(player.getMainHandItem(), ToolUpgradeKind.VOID_TELEPORT);
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
            } else {
                drop.setItem(stack);
            }
        }
    }

    /** Ender Teleport: using the tool blinks the player forward, farther with every level (like the 26.1.2 branch). */
    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        int level = level(stack, ToolUpgradeKind.ENDER_TELEPORT);
        if (level <= 0 || player.getCooldowns().isOnCooldown(stack.getItem())) {
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
                player.getCooldowns().addCooldown(stack.getItem(), Math.max(10, 60 - 4 * level));
                return;
            }
        }
    }

    /** Ender Teleport: a hit may blink the player to a random spot nearby, more often with higher level. */
    @SubscribeEvent
    public static void onHit(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player) || event.getEntity() == player) {
            return;
        }
        int level = level(player.getMainHandItem(), ToolUpgradeKind.ENDER_TELEPORT);
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
