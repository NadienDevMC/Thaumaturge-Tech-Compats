package nadiendev.ThaumaturgeTechCompats.compats.enderio.capacitor.bank.client;

import com.enderio.enderio.client.content.machines.IOOverlayBlockStateModel;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

import java.util.List;

/**
 * Blockstate model type {@code thaumaturgetechcompats:ctm_io_overlay}: Athena CTM textures plus the Ender IO
 * Input/Output overlay. Ender IO's own {@code enderio:io_overlay} type only accepts a plain variant, so Athena's
 * model is wrapped here and Ender IO's overlay quads are appended on top of it.
 * Athena is only a runtime dependency, so its codec is reached by reflection; without Athena (or with a broken
 * CTM definition) the plain {@code model} variant of the same JSON is used with Ender IO's overlay.
 */
public final class CtmOverlayBlockStateModel {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(ThaumaturgeTechCompats.MOD_ID, "ctm_io_overlay");
    private static final String ATHENA_MODEL = "earth.terrarium.athena.neoforge.client.AthenaCustomUnbakedBlockStateModel";

    private static final MapCodec<Unbaked> CODEC = MapCodec.assumeMapUnsafe(
            ExtraCodecs.JSON.comapFlatMap(CtmOverlayBlockStateModel::decode, unbaked -> new com.google.gson.JsonObject()));

    private CtmOverlayBlockStateModel() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(CtmOverlayBlockStateModel::onRegister);
    }

    private static void onRegister(RegisterBlockStateModels event) {
        event.registerModel(ID, CODEC);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static DataResult<Unbaked> decode(JsonElement json) {
        DataResult<MapLike<JsonElement>> map = JsonOps.INSTANCE.getMap(json);
        if (map.result().isEmpty()) {
            return DataResult.error(() -> "Expected a JSON object");
        }
        MapLike<JsonElement> object = map.result().get();
        try {
            MapCodec athena = (MapCodec) Class.forName(ATHENA_MODEL).getField("CODEC").get(null);
            DataResult<?> decoded = athena.decode(JsonOps.INSTANCE, object);
            if (decoded.result().isPresent()) {
                return DataResult.success(new Unbaked((BlockStateModel.Unbaked) decoded.result().get(), true));
            }
        } catch (ReflectiveOperationException | LinkageError | ClassCastException ignored) {
            // fall through to the plain model
        }
        return Variant.MAP_CODEC.decode(JsonOps.INSTANCE, object)
                .map(variant -> new Unbaked(new IOOverlayBlockStateModel.Unbaked(variant), false));
    }

    private record Unbaked(BlockStateModel.Unbaked inner, boolean athena) implements CustomUnbakedBlockStateModel {
        @Override
        public BlockStateModel bake(ModelBaker baker) {
            BlockStateModel model = inner.bake(baker);
            return athena ? new Baked(model) : model;
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            inner.resolveDependencies(resolver);
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return CtmOverlayBlockStateModel.CODEC;
        }
    }

    /** Athena model with Ender IO's I/O overlay appended. */
    private static final class Baked implements BlockStateModel {
        private final BlockStateModel athena;
        private final BlockStateModelPart empty;
        private final IOOverlayBlockStateModel overlay;

        private Baked(BlockStateModel athena) {
            this.athena = athena;
            this.empty = new EmptyPart(athena);
            this.overlay = new IOOverlayBlockStateModel(empty);
        }

        @Override
        public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
            athena.collectParts(random, output);
        }

        @Override
        public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output) {
            athena.collectParts(level, pos, state, random, output);
            int before = output.size();
            overlay.collectParts(level, pos, state, random, output);
            for (int i = output.size() - 1; i >= before; i--) {
                if (output.get(i) == empty) {
                    output.remove(i);
                }
            }
        }

        @Override
        public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
            // The overlay depends on the block entity's I/O config, so the geometry must not be cached by Athena's key.
            return null;
        }

        @Override
        public Material.Baked particleMaterial() {
            return athena.particleMaterial();
        }

        @Override
        public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
            return athena.particleMaterial(level, pos, state);
        }

        @Override
        public int materialFlags() {
            return athena.materialFlags();
        }

        @Override
        public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
            return athena.materialFlags(level, pos, state);
        }
    }

    private record EmptyPart(BlockStateModel source) implements BlockStateModelPart {
        @Override
        public List<BakedQuad> getQuads(Direction direction) {
            return List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public Material.Baked particleMaterial() {
            return source.particleMaterial();
        }

        @Override
        public int materialFlags() {
            return 0;
        }
    }
}
