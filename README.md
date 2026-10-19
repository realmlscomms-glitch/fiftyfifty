# 50/50 — Fabric mod for Minecraft 26.2

Rig your droppers. Load a dropper with a water bucket and a lava bucket, then:

| Key (default) | What it does |
|---|---|
| **J** | Force droppers to pull the **water bucket** |
| **K** | Force droppers to pull the **lava bucket** |
| **H** | Back to a fair **50/50** coin flip |
| **O** | Open the 50/50 panel with buttons |

Change the keys in Options → Controls → Key Binds → **50/50**.

## How it works
Minecraft picks which slot a dropper fires in `DispenserBlockEntity.getRandomSlot`.
A mixin hooks that method: when a dropper holds water and/or lava buckets it returns the
slot you forced. In fair mode it's a true 50/50 between the two items (even if one fills
more slots). Droppers without buckets, and dispensers, are left vanilla.
The setting is world-wide and resets to fair when the game restarts.

## Build the .jar
You need **JDK 25** (e.g. Temurin 25).

```
gradlew build        (Windows)
./gradlew build      (Mac/Linux)
```

The mod is `build/libs/fiftyfifty-1.0.0.jar` (not the `-sources` one).

No Java? Push this folder to a GitHub repo — the included workflow builds it and the jar
appears under the run's **Artifacts**.

## Install with the Modrinth App
1. Create/open a profile: **Minecraft 26.2**, loader **Fabric**.
2. Add **Fabric API** to that profile (search it in the Modrinth App).
3. Profile → ⋯ → **Open folder** → drop `fiftyfifty-1.0.0.jar` into `mods`.
4. Launch.

## Test in-game (dev)
`gradlew runClient` launches 26.2 with the mod loaded.
