# ⚡ Thaumaturge Tech Compats — Changelog

## 🚀 [1.0.3-26.1.2] 24/11/2026 21:10

### Additions
- Hephaestus Forge smeltery support: Crystal Matrix, Neutronium and Infinity can be melted and cast (ingots, nuggets, plates and blocks), with their own molten fluids and buckets. A new research entry explains it.
- Edible Goggles of Revealing: a version of the Goggles of Revealing you can eat, made with sugar.
- Tooltip with an enlarged picture of the item on every item, vanilla and modded. It can be turned off or resized in the client config.
- A Flux Jar next to a crucible now absorbs the flux the crucible leaks or overflows, instead of polluting the aura.

### Fixes
- Fixed the data pack loading error when creating or opening a world without Void Miners (or other optional compat mods). The pickaxe mining tag no longer breaks, which also fixes the Aether II pickaxe reinforcements crash.
- Fixed the Oritech resource node world generation failing to load (amber, cinnabar and quartz nodes).
- Fixed the missing Sol aspect that broke the Essences research entry.
- Fixed a startup crash ("Block id not set") when Re-Avaritia is installed. The Neutron Collectors and Compressors of higher tiers no longer break the registry.

### Changes
- The Star Crucifix can now bind any creature, not only hostile ones.

### Lineas de codigo cambiadas
+5379 −190

## 🚀 [1.0.2-26.1.2] 22/11/2026 16:50

### Additions
- Create integration is back, now through Create: Adapted. The original Create 1.21.1 jar is still needed as its asset source (see the Create: Adapted page).

### Fixes
- Fixed the "requires Ponder" error when Ponder is not installed.

### Changes
- Ponder is now optional. Create: Adapted already includes it, so it no longer has to be installed separately.
- The Ponder scenes only load when both Create and Ponder are installed.
- The creative tab icon is now the Creative Planetary Endest Pearl.

### Lineas de codigo cambiadas
+248 −103

## 🚀 [1.0.1-26.1.2] 15/11/2026 14:46

### Additions
-

### Fixes
-

### Changes
- Fractal is now bundled inside the mod, so it no longer has to be installed separately.
- Requires Oritech 2.0.0-exp12 or newer and ForceCraft 7.0.6 or newer.

### Lineas de codigo cambiadas
-

## 🚀 [1.0.0-26.1.2] 13/11/2026 14:44

### Additions
- Initial release.

### Fixes
-

### Changes
-

### Lineas de codigo cambiadas
-
