package com.pedernaltools.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.event.config.ModConfigEvent;

/**
 * Parses {@link Config#KNIFE_CUTTING_RULES} into a lookup map for the flint knife.
 *
 * <p>The map is built lazily on first in-game use rather than at config-load time:
 * items from other mods are only guaranteed to be registered once the world is
 * running, so parsing then lets modded entries resolve. The cache is cleared on
 * any {@link ModConfigEvent} (load/reload) so edits take effect after a reload.
 */
public final class KnifeCutting {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * A single conversion: consume {@code inputCount} of an input item, yield {@code outputCount}
     * of {@code output}. {@code minTier} is the minimum knife cutting level required (0 = any knife).
     */
    public record Rule(int inputCount, Item output, int outputCount, int minTier) {}

    private static volatile Map<Item, Rule> rules;

    /** Returns the rule whose input matches {@code input}, or null if none. */
    public static Rule forInput(Item input) {
        Map<Item, Rule> map = rules;
        if (map == null) {
            map = build();
            rules = map;
        }
        return map.get(input);
    }

    /** Cleared on config (re)load so the next lookup re-parses. */
    public static void onConfigChanged(ModConfigEvent event) {
        rules = null;
    }

    private static synchronized Map<Item, Rule> build() {
        if (rules != null) {
            return rules;
        }
        Map<Item, Rule> map = new HashMap<>();
        for (String entry : Config.KNIFE_CUTTING_RULES.get()) {
            parse(entry, map);
        }
        return map;
    }

    private static void parse(String entry, Map<Item, Rule> map) {
        String[] parts = entry.split(";");
        // 4 parts = input;inputCount;output;outputCount, 5th optional = minTier
        if (parts.length != 4 && parts.length != 5) {
            LOGGER.warn("[PedernalTools] Ignoring malformed knife cutting rule (need 4 or 5 ';'-separated parts): {}", entry);
            return;
        }
        try {
            Item input = resolve(parts[0].trim());
            int inputCount = Integer.parseInt(parts[1].trim());
            Item output = resolve(parts[2].trim());
            int outputCount = Integer.parseInt(parts[3].trim());
            int minTier = parts.length == 5 ? Integer.parseInt(parts[4].trim()) : 0;
            if (input == null || output == null) {
                LOGGER.warn("[PedernalTools] Ignoring knife cutting rule with unknown item: {}", entry);
                return;
            }
            if (inputCount < 1 || outputCount < 1) {
                LOGGER.warn("[PedernalTools] Ignoring knife cutting rule with non-positive count: {}", entry);
                return;
            }
            map.put(input, new Rule(inputCount, output, outputCount, Math.max(0, minTier)));
        } catch (NumberFormatException e) {
            LOGGER.warn("[PedernalTools] Ignoring knife cutting rule with bad number: {}", entry);
        }
    }

    private static Item resolve(String id) {
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) {
            return null;
        }
        Item item = BuiltInRegistries.ITEM.get(rl);
        return item == Items.AIR ? null : item;
    }

    private KnifeCutting() {}
}
