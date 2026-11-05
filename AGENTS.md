# Immersive Geology — unofficial NeoForge port

- Work on `neoforge-1.21.1-uo`. Target Minecraft 1.21.1, NeoForge and Java 21; the migration is in progress until build and runtime validation pass.
- Preserve the original gameplay, material and machine design. Do not add backwards compatibility with 1.20.1, unrelated optimizations or features.
- Keep `immersivegeology` as the mod ID. Immersive Engineering remains required; JEI and TerraFirmaCraft integrations remain optional.
- Preserve original textures, models, author credits and license files. This explicit port requirement takes precedence over the parent guide's license-file consolidation rule. Only change resource formats or paths when required by the target version.
- Keep changes minimal and readable. Remove obsolete code and development dependencies; do not remove assets merely because integration code changes.
- Use the versions in `gradle.properties` and actual dependency sources as the API reference.
- Store temporary dependencies, extracted sources and verification scripts under ignored `.tmp/`. Do not commit build outputs or Gradle caches.
- Run `sh gradlew compileJava` during migration and `sh gradlew build` before completion. Validate changed JSON and resource references; check optional-mod loading and dedicated-server startup after changing registration or client/server boundaries. Report any validation that could not be completed.
- Commit at coherent validated milestones. Do not push unless the user subsequently requests it.
