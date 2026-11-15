package com.pedernaltools.config;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.pedernaltools.item.KnifeTier;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Runtime-tunable settings, read by the event handlers and the flint knife.
 * Registered to the file {@code config/pedernaltools/config.toml} from the mod
 * constructor. Only values read at runtime live here; tool stats are static by
 * design (they are baked into the items at registration).
 */
public final class Config {
    public static final ModConfigSpec SPEC;

    // --- Knapping (flint on stone -> shards) ---
    public static final ModConfigSpec.BooleanValue KNAPPING_ENABLED;
    public static final ModConfigSpec.IntValue SHARDS_PER_FLINT;
    public static final ModConfigSpec.BooleanValue KNAPPING_CONSUMES_FLINT;

    // --- Fiber from breaking plants with the knife (Mechanic A) ---
    public static final ModConfigSpec.BooleanValue BREAK_HARVEST_ENABLED;
    public static final ModConfigSpec.IntValue FIBER_PER_PLANT;
    public static final ModConfigSpec.IntValue BREAK_DURABILITY_COST;
    /** Per-tier chance (0.0-1.0) that breaking a plant drops fiber. */
    public static final Map<KnifeTier, ModConfigSpec.DoubleValue> BREAK_DROP_CHANCE = new EnumMap<>(KnifeTier.class);
    /** Blocks (or #tags) that drop fiber when broken with a knife. */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> FIBER_PLANTS;

    // --- Knife off-hand cutting (Mechanic B): data-driven conversion rules ---
    public static final ModConfigSpec.BooleanValue KNIFE_HARVEST_ENABLED;
    public static final ModConfigSpec.IntValue KNIFE_DURABILITY_COST;
    public static final ModConfigSpec.BooleanValue SCALE_OUTPUT_BY_DURABILITY;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> KNIFE_CUTTING_RULES;

    private static final List<String> DEFAULT_CUTTING_RULES = List.of(
            "minecraft:oak_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:spruce_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:birch_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:jungle_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:acacia_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:dark_oak_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:mangrove_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:cherry_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:azalea_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:flowering_azalea_leaves;4;pedernaltools:plant_fiber;1",
            "minecraft:vine;4;pedernaltools:plant_fiber;1",
            "minecraft:dead_bush;2;pedernaltools:plant_fiber;1");

    private static final List<String> DEFAULT_FIBER_PLANTS = List.of(
            "minecraft:short_grass",
            "minecraft:tall_grass",
            "minecraft:fern",
            "minecraft:large_fern",
            "#minecraft:flowers");

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.comment("Knapping: right-click a stone block with flint in hand to get shards.")
                .push("knapping");
        KNAPPING_ENABLED = b.comment("Enable knapping.").define("enabled", true);
        SHARDS_PER_FLINT = b.comment("Flint shards produced per knap.").defineInRange("shardsPerFlint", 2, 1, 64);
        KNAPPING_CONSUMES_FLINT = b.comment("Whether knapping consumes one flint.").define("consumesFlint", true);
        b.pop();

        b.comment("Plant fiber from breaking small plants (grass, ferns, flowers) with a knife.")
                .push("fiber_from_breaking_plants");
        BREAK_HARVEST_ENABLED = b.comment("Enable this harvesting method.").define("enabled", true);
        FIBER_PER_PLANT = b.comment("Plant fiber dropped per plant broken.").defineInRange("fiberPerPlant", 1, 1, 64);
        BREAK_DURABILITY_COST = b.comment("Durability the knife loses per plant harvested (0 = none).")
                .defineInRange("durabilityCost", 1, 0, 64);
        FIBER_PLANTS = b.comment(
                        "Blocks that drop fiber when broken with a knife. Use a block id, or a tag with '#'.",
                        "Add grasses from other mods here (e.g. \"somemod:tall_weeds\" or \"#somemod:bushes\").")
                .defineListAllowEmpty("fiberPlants", DEFAULT_FIBER_PLANTS, () -> "", o -> o instanceof String);
        b.comment("Chance (0.0-1.0) per knife tier that breaking a plant drops fiber.")
                .push("dropChanceByTier");
        for (KnifeTier t : KnifeTier.values()) {
            BREAK_DROP_CHANCE.put(t, b.defineInRange(t.name().toLowerCase(Locale.ROOT), t.defaultDropChance, 0.0, 1.0));
        }
        b.pop();
        b.pop();

        b.comment("Knife off-hand cutting: hold the knife in the main hand and an input item in the off hand, then right-click.")
                .push("fiber_from_knife");
        KNIFE_HARVEST_ENABLED = b.comment("Enable off-hand cutting.").define("enabled", true);
        KNIFE_DURABILITY_COST = b.comment("Durability the knife loses per cut (0 = none).")
                .defineInRange("durabilityCost", 1, 0, 64);
        SCALE_OUTPUT_BY_DURABILITY = b.comment(
                        "If true, when the input item is damageable (armor, tools...), the output is scaled by",
                        "its remaining durability (a half-worn leather helmet gives ~half its configured leather).",
                        "A damaged input always yields at least 1. Non-damageable inputs are unaffected.")
                .define("scaleOutputByDurability", true);
        KNIFE_CUTTING_RULES = b.comment(
                        "Cutting rules. Format: inputItem;inputCount;outputItem;outputCount[;minTier]",
                        "Right-clicking with inputCount of inputItem in the off hand consumes them and yields outputCount of outputItem.",
                        "minTier (optional, default 0) is the minimum knife cutting level required:",
                        "  flint=0, gold=1, stone=1, iron=2, diamond=3, netherite=4, quantum=5.",
                        "E.g. \"minecraft:obsidian;1;minecraft:obsidian;1;4\" -> only netherite+ knives can cut it.",
                        "Add items from other mods here too.")
                .defineListAllowEmpty("cuttingRules", DEFAULT_CUTTING_RULES, () -> "", o -> o instanceof String);
        b.pop();

        SPEC = b.build();
    }

    private Config() {}
}
