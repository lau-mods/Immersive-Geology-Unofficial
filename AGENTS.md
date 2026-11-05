# Immersive Geology — unofficial NeoForge port

- Work on `neoforge-1.21.1-uo`. Target Minecraft 1.21.1, NeoForge and Java 21. The full build, six unit tests, server data generation and all 36 dedicated-server GameTests pass. The client starts with JEI and completes model/texture loading; in-world GUI and machine rendering still require manual playtesting.
- Preserve the original gameplay, material and machine design. Do not add backwards compatibility with 1.20.1, unrelated optimizations or features.
- Keep `immersivegeology` as the mod ID. Immersive Engineering remains required; JEI remains optional. TerraFirmaCraft integration is removed by user request; retain its original assets.
- Preserve original textures, models, author credits and license files. This explicit port requirement takes precedence over the parent guide's license-file consolidation rule. Only change resource formats or paths when required by the target version.
- Keep changes minimal and readable. Remove obsolete code and development dependencies; do not remove assets merely because integration code changes.
- Use the versions in `gradle.properties` and actual dependency sources as the API reference.
- Store temporary dependencies, extracted sources and verification scripts under ignored `.tmp/`. Do not commit build outputs or Gradle caches.
- Run `sh gradlew compileJava` during migration and `sh gradlew build` before completion. Validate changed JSON and resource references; check optional-mod loading and dedicated-server startup after changing registration or client/server boundaries. Report any validation that could not be completed.
- Commit at coherent validated milestones. Do not push unless the user subsequently requests it.

- `sh gradlew runData` generates server data into `build/generated` for review. Copy verified `data/` changes into `src/generated/resources`; do not run the data generator directly over the retained assets, since Minecraft removes files absent from generator output. Client assets are retained from the original version except required loader/path changes.
- Minecraft 1.21.1 uses singular data directories (`recipe`, `loot_table`, `structure`, and registry tag directories). Shared tags use `c`; loader data and OBJ loaders use `neoforge`.
- `sh gradlew runGameTestServer -PwithoutJei` validates the optional-JEI-free dedicated server. Default GameTests cover multiblock formation/disassembly, capability invalidation, and recipe loading/network serialization. The expensive 64×64-chunk diagnostic image capture is opt-in via the `immersivegeology.captureSpawnChunks` JVM system property in the GameTest run configuration.
- Validation retains the original missing rod/wire warnings from manual registration (`MaterialHelper.getItem`); those lookups were already present in the original code. Do not silently add new material forms as part of the port.

- Item tint callbacks return opaque ARGB (`alpha = 255`) in Minecraft 1.21.1. Keep material RGB values and texture alpha unchanged; add the opaque alpha at the item-render callback boundary, including the white ore-item/default tint. Block tint and fluid-render behavior are separate.
- Client item rendering validation: all 1,508 registered `IGFlagItem` models have geometry and no missing model; all tinted layers return opaque colors with their original RGB values. A real-client comparison reproduced 1,059 items with zero-alpha tints before the fix and confirmed visible sample icons afterward. Startup/model-loading success alone is insufficient to validate item visibility.
