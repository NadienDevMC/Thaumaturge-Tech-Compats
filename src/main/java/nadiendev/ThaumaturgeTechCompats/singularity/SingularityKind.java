package nadiendev.ThaumaturgeTechCompats.singularity;

import nadiendev.ThaumaturgeTechCompats.ThaumaturgeTechCompats;

public enum SingularityKind {
    THAUMIUM("thaumium", "thaumaturge:ingot_thaumium", 0xB07CE8, 0x4B2A86, "Thaumium", "Thaumio"),
    VOID_METAL("void_metal", "thaumaturge:ingot_void", 0x7A4FB0, 0x120A24, "Void Metal", "Metal del Vacío"),
    BRASS("brass", "thaumaturge:ingot_brass", 0xF2C14E, 0xA0701C, "Alchemical Brass", "Latón Alquímico"),
    RIFTING("rifting", "thaumaturgetechcompats:rifting_ingot", 0x3FD6C4, 0x0B0818, "Rifting", "Rifting"),
    FIBRIC("fibric", "thaumaturgetechcompats:fibric_ingot", 0x6EC8F5, 0x1F3F9A, "Fibric", "Fibric"),
    SALIS("salis", "thaumaturgetechcompats:salis_ingot", 0xF08AE8, 0x8A3A8F, "Salis", "Salis"),
    CRIMSON("crimson", "thaumaturgetechcompats:crimson_ingot", 0xE0384E, 0x3D1230, "Crimson", "Carmesí");

    private final String id;
    private final String ingredient;
    private final int overlayColor;
    private final int underlayColor;
    private final String english;
    private final String spanish;

    SingularityKind(String id, String ingredient, int overlayColor, int underlayColor, String english, String spanish) {
        this.id = id;
        this.ingredient = ingredient;
        this.overlayColor = overlayColor;
        this.underlayColor = underlayColor;
        this.english = english;
        this.spanish = spanish;
    }

    public String id() {
        return id;
    }

    public String ingredient() {
        return ingredient;
    }

    public int overlayColor() {
        return overlayColor;
    }

    public int underlayColor() {
        return underlayColor;
    }

    public String displayKey() {
        return "singularity." + ThaumaturgeTechCompats.MOD_ID + "." + id;
    }

    public String english() {
        return english;
    }

    public String spanish() {
        return spanish;
    }
}
