package nadiendev.ThaumaturgeTechCompats.compats.tags;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;
import nadiendev.ThaumaturgeTechCompats.compats.create.CreateToggles;
import nadiendev.ThaumaturgeTechCompats.compats.hephaestus.HephaestusSmelteryIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools.MekanismToolIntegration;
import nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools.MekanismToolKind;
import nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools.MekanismToolRegistry;
import nadiendev.ThaumaturgeTechCompats.compats.mekanism.tools.MekanismToolTier;
import nadiendev.ThaumaturgeTechCompats.config.CompatToggles;
import nadiendev.ThaumaturgeTechCompats.infusion.InfusionIntegration;
import nadiendev.ThaumaturgeTechCompats.materials.ExtraMaterial;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Aspects of the items of this addon that Thaumaturge cannot derive from a recipe: no recipe at all (clusters, pillars, energy tools),
 * a recipe type it does not read (dust trigger multiblock) or a chain whose base had no aspects (the extra metals).
 * Ingot = 1 unit, nugget = 1/9, block = the usual tag block scale, cluster = 2 ingots, like the c: tag table.
 */
public final class OwnItemAspectTable {
    private static final String MOD = ThaumaturgeTechCompats.MOD_ID;
    private static final String OWN = CompatToggles.OWN_ITEM_ASPECTS;
    private static final List<AspectPart> NONE = List.of();

    private final List<TagAspectEntry> entries = new ArrayList<>();

    private OwnItemAspectTable() {
    }

    public static List<TagAspectEntry> entries() {
        OwnItemAspectTable table = new OwnItemAspectTable();
        table.extraMetals();
        table.avaritiaJars();
        table.avaritiaFurnaces();
        table.infusionPillars();
        table.moltenBuckets();
        table.thaummotor();
        table.mekanismClusters();
        table.mekanismTools();
        return table.entries;
    }

    private void put(String id, List<AspectPart> parts, List<String> mods, String... features) {
        List<String> all = new ArrayList<>();
        all.add(OWN);
        all.addAll(List.of(features));
        entries.add(new TagAspectEntry(MOD + ":" + id, parts, all, mods));
    }

    private void put(String id, List<AspectPart> parts, String... features) {
        put(id, parts, List.of(), features);
    }

    // ---- Rifting, Fibric, Salis, Crimson ----

    private static List<AspectPart> metal(ExtraMaterial material) {
        return switch (material) {
            case RIFTING -> List.of(Asp.METALLUM.of(10), Asp.VACUOS.of(8), Asp.ALIENIS.of(6));
            case FIBRIC -> List.of(Asp.METALLUM.of(12), Asp.FABRICO.of(8), Asp.VITIUM.of(6));
            case SALIS -> List.of(Asp.METALLUM.of(14), Asp.PRAECANTATIO.of(10), Asp.POTENTIA.of(8));
            case CRIMSON -> List.of(Asp.METALLUM.of(10), Asp.PERDITIO.of(8), Asp.HUMANUS.of(6));
        };
    }

    private void extraMetals() {
        for (ExtraMaterial material : ExtraMaterial.values()) {
            List<AspectPart> ingot = metal(material);
            List<AspectPart> inertRod = AspectMath.scale(ingot, 3, 1, NONE);
            List<AspectPart> inertCap = AspectMath.scale(ingot, 5, 9, NONE);
            List<AspectPart> awakening = new ArrayList<>(List.of(Asp.PRAECANTATIO.of(20), Asp.POTENTIA.of(20)));
            for (String aspect : material.capAspects()) {
                awakening.add(Asp.valueOf(aspect.toUpperCase(Locale.ROOT)).of(material.infusionAspect() / 2));
            }
            put(material.ingotName(), ingot);
            put(material.nuggetName(), TagForm.NUGGET.scale(ingot));
            put(material.blockName(), TagForm.BLOCK.scale(ingot));
            put(material.clusterName(), AspectMath.scale(ingot, 2, 1, List.of(Asp.TERRA.of(5))));
            put(material.inertRodName(), inertRod);
            put(material.inertCapName(), inertCap);
            put(material.rodName(), sum(inertRod, awakening));
            put(material.capName(), sum(inertCap, awakening));
        }
    }

    // ---- Avaritia jars, furnaces and infusion pillars ----

    private static final List<AspectPart> JAR = List.of(Asp.VITREUS.of(10), Asp.ORDO.of(5));
    private static final List<AspectPart> CRYSTAL_MATRIX_INGOT = List.of(Asp.TERMINUS.of(1), Asp.POTENTIA.of(8), Asp.VITREUS.of(32));
    private static final List<AspectPart> NEUTRON_INGOT = List.of(Asp.TERMINUS.of(2), Asp.METALLUM.of(12), Asp.POTENTIA.of(12));
    private static final List<AspectPart> INFINITY_INGOT = List.of(Asp.TERMINUS.of(16), Asp.ALIENIS.of(64));
    private static final List<AspectPart> PRIMORDIAL_PEARL = List.of(Asp.AER.of(10), Asp.IGNIS.of(10), Asp.AQUA.of(10), Asp.TERRA.of(10), Asp.ORDO.of(10), Asp.PERDITIO.of(10));

    private void avaritiaJars() {
        List<AspectPart> matrix = infusion(JAR, times(CRYSTAL_MATRIX_INGOT, 4), List.of(Asp.VITREUS.of(24), Asp.PRAEMUNIO.of(16), Asp.POTENTIA.of(16), Asp.TERMINUS.of(4)));
        List<AspectPart> neutronium = infusion(matrix, times(NEUTRON_INGOT, 4), List.of(Asp.METALLUM.of(32), Asp.PRAEMUNIO.of(32), Asp.VITREUS.of(16), Asp.TERMINUS.of(8)));
        List<AspectPart> pearl = extremelyPrimordialPearl();
        List<AspectPart> infinity = infusion(neutronium, sum(times(INFINITY_INGOT, 4), pearl), List.of(Asp.ALIENIS.of(32), Asp.PRAEMUNIO.of(48), Asp.VACUOS.of(32), Asp.TERMINUS.of(24)));
        put("matrix_jar", matrix);
        put("neutronium_jar", neutronium);
        put("infinity_jar", infinity);

        List<AspectPart> fluxBase = List.of(Asp.VITREUS.of(10), Asp.VACUOS.of(10));
        List<AspectPart> matrixFlux = infusion(fluxBase, times(CRYSTAL_MATRIX_INGOT, 4), List.of(Asp.VACUOS.of(24), Asp.PRAEMUNIO.of(16), Asp.VITREUS.of(16), Asp.TERMINUS.of(4)));
        List<AspectPart> neutroniumFlux = infusion(matrixFlux, times(NEUTRON_INGOT, 4), List.of(Asp.METALLUM.of(32), Asp.PRAEMUNIO.of(32), Asp.VACUOS.of(16), Asp.TERMINUS.of(8)));
        List<AspectPart> infinityFlux = infusion(neutroniumFlux, sum(times(INFINITY_INGOT, 4), pearl), List.of(Asp.ALIENIS.of(32), Asp.PRAEMUNIO.of(48), Asp.VACUOS.of(32), Asp.TERMINUS.of(24)));
        put("matrix_flux_jar", matrixFlux, CompatToggles.NODE_POWER);
        put("neutronium_flux_jar", neutroniumFlux, CompatToggles.NODE_POWER);
        put("infinity_flux_jar", infinityFlux, CompatToggles.NODE_POWER);

        put("extremely_primordial_pearl", pearl);
        put("akashic_record", infusion(List.of(Asp.COGNITIO.of(10)), times(INFINITY_INGOT, 4), List.of(Asp.COGNITIO.of(64), Asp.PRAECANTATIO.of(64), Asp.TERMINUS.of(32))));
    }

    private static List<AspectPart> extremelyPrimordialPearl() {
        return infusion(NONE, times(PRIMORDIAL_PEARL, 7), List.of(
                Asp.ALIENIS.of(64), Asp.PRAECANTATIO.of(64), Asp.AQUA.of(64), Asp.IGNIS.of(64), Asp.TERRA.of(64),
                Asp.AER.of(64), Asp.ORDO.of(64), Asp.PERDITIO.of(64), Asp.TERMINUS.of(32)));
    }

    private void avaritiaFurnaces() {
        put("matrix_alchemical_furnace", List.of(Asp.MACHINA.of(40), Asp.IGNIS.of(30), Asp.VITREUS.of(40), Asp.POTENTIA.of(20), Asp.TERMINUS.of(3)));
        put("neutronium_alchemical_furnace", List.of(Asp.MACHINA.of(60), Asp.IGNIS.of(45), Asp.METALLUM.of(60), Asp.POTENTIA.of(40), Asp.TERMINUS.of(6)));
        put("infinity_alchemical_furnace", List.of(Asp.MACHINA.of(80), Asp.IGNIS.of(60), Asp.ALIENIS.of(60), Asp.POTENTIA.of(60), Asp.TERMINUS.of(20)));
    }

    private void infusionPillars() {
        String altar = InfusionIntegration.ALTAR_TOGGLE;
        put("matrix_infusion_pillar", List.of(Asp.VITREUS.of(32), Asp.POTENTIA.of(8), Asp.ORDO.of(5), Asp.TERMINUS.of(1)), altar);
        put("neutronium_infusion_pillar", List.of(Asp.METALLUM.of(24), Asp.POTENTIA.of(12), Asp.ORDO.of(5), Asp.TERMINUS.of(2)), altar);
        put("infinity_infusion_pillar", List.of(Asp.ALIENIS.of(24), Asp.POTENTIA.of(16), Asp.ORDO.of(5), Asp.TERMINUS.of(6)), altar);
    }

    // ---- Molten buckets of the Hephaestus smeltery ----

    /** A bucket is 10 ingots (1000 mB at 90 mB per ingot, minus a loss), the same scale as 1.21.1. */
    private static final int BUCKET_INGOTS = 10;

    private void moltenBuckets() {
        List<String> mods = List.of(HephaestusSmelteryIntegration.MOD_ID);
        String smeltery = HephaestusSmelteryIntegration.TOGGLE;
        for (ExtraMaterial material : ExtraMaterial.values()) {
            put(material.moltenName() + "_bucket", times(metal(material), BUCKET_INGOTS), mods, smeltery);
        }
        put("molten_crystal_matrix_bucket", times(CRYSTAL_MATRIX_INGOT, BUCKET_INGOTS), mods, smeltery);
        put("molten_neutronium_bucket", times(NEUTRON_INGOT, BUCKET_INGOTS), mods, smeltery);
        put("molten_infinity_bucket", times(INFINITY_INGOT, BUCKET_INGOTS), mods, smeltery);
    }

    // ---- Create and Mekanism ----

    private void thaummotor() {
        put("thaummotor", List.of(Asp.METALLUM.of(30), Asp.MACHINA.of(20), Asp.MOTUS.of(10), Asp.PRAECANTATIO.of(5)), List.of("create"),
                CreateToggles.TECH, CreateToggles.CASING_BRASS, CreateToggles.THAUMMOTOR, CompatToggles.NODE_POWER);
    }

    private void mekanismClusters() {
        for (String id : List.of("osmium", "uranium")) {
            put(id + "_cluster", AspectMath.scale(TagMaterials.require(id).aspects(), 2, 1, List.of(Asp.TERRA.of(5))),
                    List.of(MekanismToolIntegration.MOD_ID), CompatToggles.MEKANISM_CLUSTERS);
        }
    }

    private static final Map<MekanismToolKind, Integer> PLATES = Map.of(
            MekanismToolKind.PICKAXE, 3, MekanismToolKind.AXE, 3, MekanismToolKind.SHOVEL, 1, MekanismToolKind.HOE, 2, MekanismToolKind.SWORD, 2);

    private void mekanismTools() {
        for (MekanismToolTier tier : MekanismToolTier.values()) {
            List<String> features = new ArrayList<>();
            features.add(MekanismToolIntegration.TOGGLE);
            for (MekanismToolTier current = tier; current != null; current = current.previous()) {
                features.add(current.toggle());
            }
            for (MekanismToolKind kind : MekanismToolKind.values()) {
                List<AspectPart> parts = sum(times(plate(tier), PLATES.get(kind)), machinery(tier));
                put(MekanismToolRegistry.itemId(tier, kind), parts, List.of(MekanismToolIntegration.MOD_ID), features.toArray(String[]::new));
            }
        }
    }

    private static List<AspectPart> plate(MekanismToolTier tier) {
        return switch (tier) {
            case THAUMIUM -> List.of(Asp.METALLUM.of(10), Asp.PRAECANTATIO.of(3));
            case BRASS -> TagMaterials.require("brass").aspects();
            case VOID -> List.of(Asp.METALLUM.of(12), Asp.VACUOS.of(8));
        };
    }

    private static List<AspectPart> machinery(MekanismToolTier tier) {
        return switch (tier) {
            case THAUMIUM -> List.of(Asp.MACHINA.of(10), Asp.POTENTIA.of(10), Asp.INSTRUMENTUM.of(8));
            case BRASS -> List.of(Asp.MACHINA.of(16), Asp.POTENTIA.of(16), Asp.INSTRUMENTUM.of(12));
            case VOID -> List.of(Asp.MACHINA.of(24), Asp.POTENTIA.of(24), Asp.INSTRUMENTUM.of(18), Asp.PRAECANTATIO.of(8));
        };
    }

    // ---- helpers ----

    private static List<AspectPart> times(List<AspectPart> parts, int count) {
        return AspectMath.scale(parts, count, 1, NONE);
    }

    private static List<AspectPart> sum(List<AspectPart> first, List<AspectPart> second) {
        Map<Asp, Integer> merged = new LinkedHashMap<>();
        first.forEach(part -> merged.merge(part.aspect(), part.amount(), Integer::sum));
        second.forEach(part -> merged.merge(part.aspect(), part.amount(), Integer::sum));
        List<AspectPart> result = new ArrayList<>();
        merged.forEach((aspect, amount) -> result.add(new AspectPart(aspect, amount)));
        return result;
    }

    /** Same shape as the Thaumaturge infusion derivation: 75% of the ingredients plus the square root of the infusion cost. */
    private static List<AspectPart> infusion(List<AspectPart> base, List<AspectPart> ingredients, List<AspectPart> cost) {
        List<AspectPart> share = AspectMath.scale(ingredients, 3, 4, NONE);
        List<AspectPart> drain = new ArrayList<>();
        cost.forEach(part -> drain.add(new AspectPart(part.aspect(), Math.max(1, (int) Math.round(Math.sqrt(part.amount()))))));
        return sum(sum(base, share), drain);
    }
}
