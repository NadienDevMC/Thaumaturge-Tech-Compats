package nadiendev.ThaumaturgeTechCompats.tooltip;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ItemPreviewConfig {
    public static final String FILE_NAME = "thaumaturgetechcompats-tooltip-client.toml";
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue ENABLED;
    private static final ModConfigSpec.IntValue SCALE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("item_preview");
        ENABLED = builder.comment("Show an enlarged picture of the item in the tooltip of every item, vanilla and modded.")
                .define("enabled", true);
        SCALE = builder.comment("Size of the picture in multiples of a normal item icon (16 px).")
                .defineInRange("scale", 3, 1, 8);
        builder.pop();
        SPEC = builder.build();
    }

    private ItemPreviewConfig() {
    }

    public static boolean enabled() {
        return SPEC.isLoaded() ? ENABLED.get() : ENABLED.getDefault();
    }

    public static int scale() {
        return SPEC.isLoaded() ? SCALE.get() : SCALE.getDefault();
    }
}
