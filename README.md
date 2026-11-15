# Pedernal Tools

A primitive-to-endgame **flint tool progression** for **NeoForge 1.21.1**. Start
with nothing, knap flint against stone, weave plant fiber into string, and work
your way up a full line of knives — from a fragile flint blade all the way to an
unbreakable **Quantum Knife**.

Built as the starter-tools layer for the upcoming **Industry Nightmare** modpack,
and a companion to **BloodyPunch**. Works as a standalone mod too.

📦 **[Download on Modrinth](https://modrinth.com/mod/pedernaltools)**

## Features

- 🪨 **Knapping** — right-click flint on stone to chip off flint shards. No crafting table needed.
- 🌿 **Plant fiber & string** — harvest fiber from plants and turn it into plant string that works in any vanilla string recipe (bows, leads, scaffolding…).
- 🔪 **A full knife tier line** — flint, stone, iron, gold, diamond, netherite, and a quantum knife.
- 🪓 **Flint axe & pickaxe** — weak, fragile starter tools (wood mining speed, 1 attack damage) to get you going.
- ⚙️ **Deeply configurable** — almost every number and item list is exposed in `config/pedernaltools/config.toml`.

## Progression

```
gravel → flint → (knap on stone) → flint shards → (+ stick) → flint knife
flint knife on plants → plant fiber → (×3) → plant string
shards + string + stick → flint axe / pickaxe
flint knife → (smithing table) → stone → iron → diamond → netherite
quantum knife: netherite knife + Quantum Upgrade in a Modern Industrialization Electric Packer
```

## Knife mechanics

The knife isn't just a weapon — it **cuts materials into other items**:

- **Break** grass, ferns, or flowers with a knife in hand to harvest plant fiber (drop chance scales with the knife's tier).
- **Hold** the knife in your main hand and a stack of leaves/vines in your off hand, then right-click to shred them into fiber. Damaged input items (like worn armor) return proportionally less.

Higher-tier knives can cut tougher materials — fully configurable per material.

## Configuration

Everything lives in `config/pedernaltools/config.toml`:

- Knapping output and whether it consumes flint.
- Per-tier fiber drop chances and the list of harvestable plants (add modded plants!).
- Custom cutting rules: `inputItem;count;outputItem;count;minTier` — add any conversion you like, including items from other mods.

## Compatibility

- **NeoForge 1.21.1**
- **BloodyPunch** *(optional)* — designed to pair with it.
- **Modern Industrialization** *(optional)* — only needed to craft the Quantum Knife. The recipe loads conditionally and is safely ignored if MI isn't installed.

## Building from source

Requires **JDK 21**.

```bash
git clone https://github.com/MartDniel/pedernaltools.git
cd pedernaltools
./gradlew build
```

The built jar will be in `build/libs/`.

## License

Released under the [MIT License](LICENSE) — feel free to use, modify, fork, or
include in modpacks.
