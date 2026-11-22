package nadiendev.ThaumaturgeTechCompats.compats.create.seal;

import com.leclowndu93150.thaumaturge.api.golems.GolemHelper;
import com.leclowndu93150.thaumaturge.api.golems.IGolemAPI;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealBehavior;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealEntity;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealFilter;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealPos;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealSetting;
import com.leclowndu93150.thaumaturge.api.golems.tasks.Task;
import com.leclowndu93150.thaumaturge.api.items.InvHelper;
import com.leclowndu93150.thaumaturge.content.golem.GolemInteractionHelper;
import com.leclowndu93150.thaumaturge.content.golem.seals.behavior.ItemMatchSettings;
import com.leclowndu93150.thaumaturge.content.golem.tasks.TaskBoard;
import nadiendev.ThaumaturgeTechCompats.compats.create.CreateConfig;
import nadiendev.ThaumaturgeTechCompats.compats.create.CreateToggles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.HashMap;
import java.util.Map;

public final class CreateSealBehavior implements ISealBehavior {
    public static final SealSetting INSERT = new SealSetting("create_insert", "gui.thaumaturgetechcompats.seal.setting.insert", true);
    public static final SealSetting EXTRACT = new SealSetting("create_extract", "gui.thaumaturgetechcompats.seal.setting.extract", true);
    public static final SealSetting OPERATE = new SealSetting("create_operate", "gui.thaumaturgetechcompats.seal.setting.operate", false);
    public static final SealSetting SNEAKING = new SealSetting("create_sneaking", "gui.thaumaturgetechcompats.seal.setting.sneak", false);

    private static final int STAGGER = 41;
    private static final int KIND_INSERT = 0;
    private static final int KIND_EXTRACT = 1;
    private static final int KIND_OPERATE = 2;
    private static final int EXTRACT_LIFESPAN = 5;
    private static final int NONE = Integer.MIN_VALUE;

    private final int offset = Math.floorMod(System.identityHashCode(this), STAGGER);
    private final int[] pending = {NONE, NONE, NONE};
    private final Map<Integer, ItemStack> takings = new HashMap<>();
    private int clock;

    @Override
    public void tick(ServerLevel level, ISealEntity seal) {
        int now = clock++ + offset;
        if (!CreateToggles.enabled(CreateToggles.SEAL) || seal.isStoppedByRedstone(level)) {
            return;
        }
        if (now % 100 == 0) {
            takings.keySet().removeIf(id -> !TaskBoard.of(level).isLive(id));
        }
        if (now % CreateConfig.scanInterval() == 0) {
            if (seal.setting(INSERT) && free(level, KIND_INSERT)) {
                post(level, seal, KIND_INSERT, null);
            }
            if (seal.setting(EXTRACT) && free(level, KIND_EXTRACT)) {
                offerExtraction(level, seal);
            }
        }
        if (seal.setting(OPERATE) && now % CreateConfig.operateInterval() == 0 && free(level, KIND_OPERATE)) {
            post(level, seal, KIND_OPERATE, null);
        }
    }

    private boolean free(ServerLevel level, int kind) {
        Task task = TaskBoard.of(level).find(pending[kind]);
        return task == null || task.isClaimed() || task.isEnded() || task.isCompleted();
    }

    private void post(ServerLevel level, ISealEntity seal, int kind, ItemStack wanted) {
        Task task = Task.atBlock(seal.pos(), seal.pos().pos());
        task.setPriority(seal.priority());
        task.setData(kind);
        if (kind == KIND_EXTRACT) {
            task.setLife(EXTRACT_LIFESPAN);
        }
        GolemHelper.addGolemTask(level, task);
        pending[kind] = task.id();
        if (wanted != null) {
            takings.put(task.id(), wanted);
        }
    }

    private void offerExtraction(ServerLevel level, ISealEntity seal) {
        SealPos at = seal.pos();
        ResourceHandler<ItemResource> source = InvHelper.getItemHandlerAt(level, at.pos(), at.face());
        if (source == null) {
            return;
        }
        ISealFilter filter = seal.filter().orElseThrow();
        ItemStack found = InvHelper.findFirstMatchFromFilter(filter.stacks(), filter.isBlacklist(), source, ItemMatchSettings.of(seal), false);
        if (!found.isEmpty()) {
            post(level, seal, KIND_EXTRACT, found);
        }
    }

    @Override
    public void onTaskStarted(ServerLevel level, ISealEntity seal, IGolemAPI golem, Task task) {
        if (task.data() == KIND_INSERT && !seal.isStoppedByRedstone(level)) {
            post(level, seal, KIND_INSERT, null);
        }
    }

    @Override
    public boolean completeTask(ServerLevel level, ISealEntity seal, IGolemAPI golem, Task task) {
        switch (task.data()) {
            case KIND_INSERT -> insert(level, seal, golem);
            case KIND_EXTRACT -> extract(level, seal, golem, task);
            case KIND_OPERATE -> operate(level, seal, golem, task);
            default -> {
            }
        }
        task.end();
        return true;
    }

    private void insert(ServerLevel level, ISealEntity seal, IGolemAPI golem) {
        InvHelper.InvFilter matching = ItemMatchSettings.of(seal);
        InvHelper.FilterMatch carried = carried(seal, golem, matching);
        if (carried.stack().isEmpty()) {
            return;
        }
        SealPos at = seal.pos();
        ResourceHandler<ItemResource> inventory = InvHelper.getItemHandlerAt(level, at.pos(), at.face());
        if (inventory == null) {
            return;
        }
        int amount = carried.stack().getCount();
        if (limited(seal, carried)) {
            amount = Math.max(0, carried.sizeLimit() - InvHelper.countTotalItemsIn(inventory, carried.stack(), matching));
        }
        if (amount <= 0) {
            return;
        }
        ItemStack load = golem.hands().release(carried.stack().copyWithCount(amount));
        ItemStack refused = InvHelper.insertStack(inventory, load, false);
        golem.hands().hold(refused);
        if (refused.getCount() < load.getCount()) {
            handlingSound(golem, 1.0F);
            golem.addRankXp(1);
            golem.swingArm();
        }
    }

    private void extract(ServerLevel level, ISealEntity seal, IGolemAPI golem, Task task) {
        ItemStack wanted = takings.remove(task.id());
        if (wanted == null || wanted.isEmpty()) {
            return;
        }
        int room = golem.hands().room(wanted);
        if (room <= 0) {
            return;
        }
        SealPos at = seal.pos();
        ItemStack taken = InvHelper.removeStackFrom(level, at.pos(), at.face(), InvHelper.copyLimitedStack(wanted, room), ItemMatchSettings.of(seal), false);
        ItemStack spill = golem.hands().hold(taken);
        if (!spill.isEmpty()) {
            InvHelper.ejectStackAt(level, at.pos().relative(at.face()), at.face().getOpposite(), spill);
        }
        handlingSound(golem, 2.0F);
        golem.swingArm();
    }

    private void operate(ServerLevel level, ISealEntity seal, IGolemAPI golem, Task task) {
        ItemStack tool = carried(seal, golem, ItemMatchSettings.of(seal)).stack();
        ItemStack used = ItemStack.EMPTY;
        if (!tool.isEmpty()) {
            used = golem.hands().release(tool.copyWithCount(1));
        }
        GolemInteractionHelper.golemClick(level, golem, task.pos(), seal.pos().face(), used, seal.setting(SNEAKING), true);
    }

    private static InvHelper.FilterMatch carried(ISealEntity seal, IGolemAPI golem, InvHelper.InvFilter matching) {
        ISealFilter filter = seal.filter().orElseThrow();
        return InvHelper.findFirstMatchFromFilterWithSize(filter.stacks(), filter.limits(), filter.isBlacklist(), golem.hands().contents(), matching);
    }

    private static boolean limited(ISealEntity seal, InvHelper.FilterMatch match) {
        return seal.filter().orElseThrow().usesLimits() && match.sizeLimit() > 0;
    }

    private static void handlingSound(IGolemAPI golem, float octave) {
        RandomSource random = golem.level().getRandom();
        golem.asEntity().playSound(SoundEvents.ITEM_PICKUP, 0.125F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * octave);
    }

    @Override
    public boolean canPerform(ISealEntity seal, IGolemAPI golem, Task task) {
        Level level = golem.level();
        SealPos at = seal.pos();
        return switch (task.data()) {
            case KIND_INSERT -> canInsert(level, seal, golem, at);
            case KIND_EXTRACT -> {
                ItemStack wanted = takings.get(task.id());
                yield wanted != null && !wanted.isEmpty() && golem.hands().canTake(wanted, true);
            }
            case KIND_OPERATE -> true;
            default -> false;
        };
    }

    private boolean canInsert(Level level, ISealEntity seal, IGolemAPI golem, SealPos at) {
        InvHelper.InvFilter matching = ItemMatchSettings.of(seal);
        InvHelper.FilterMatch carried = carried(seal, golem, matching);
        if (carried.stack().isEmpty()) {
            return false;
        }
        ResourceHandler<ItemResource> inventory = InvHelper.getItemHandlerAt(level, at.pos(), at.face());
        if (inventory == null || !InvHelper.hasRoomForSome(level, at.pos(), at.face(), carried.stack())) {
            return false;
        }
        return !limited(seal, carried) || InvHelper.countTotalItemsIn(inventory, carried.stack(), matching) < carried.sizeLimit();
    }

    @Override
    public void onTaskSuspended(ServerLevel level, ISealEntity seal, Task task) {
        takings.remove(task.id());
    }
}
