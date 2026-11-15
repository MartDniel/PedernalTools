package com.pedernaltools.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.event.config.ModConfigEvent;

/**
 * Parses {@link Config#FIBER_PLANTS} (the blocks that drop fiber when broken with a
 * knife — Mechanic A) into a fast lookup. Entries are block ids, or tag references
 * prefixed with {@code #} (e.g. {@code #minecraft:flowers}). Built lazily on first
 * use and cleared on config (re)load, same pattern as {@link KnifeCutting}.
 */
public final class FiberPlants {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static volatile Set<Block> blocks;
    private static volatile List<TagKey<Block>> tags;

    public static boolean matches(BlockState state) {
        ensureBuilt();
        if (blocks.contains(state.getBlock())) {
            return true;
        }
        for (TagKey<Block> tag : tags) {
            if (state.is(tag)) {
                return true;
            }
        }
        return false;
    }

    /** Cleared on config (re)load so the next lookup re-parses. */
    public static void onConfigChanged(ModConfigEvent event) {
        blocks = null;
        tags = null;
    }

    private static synchronized void ensureBuilt() {
        if (blocks != null) {
            return;
        }
        Set<Block> blockSet = new HashSet<>();
        List<TagKey<Block>> tagList = new ArrayList<>();
        for (String entry : Config.FIBER_PLANTS.get()) {
            parse(entry.trim(), blockSet, tagList);
        }
        tags = tagList;
        blocks = blockSet;
    }

    private static void parse(String entry, Set<Block> blockSet, List<TagKey<Block>> tagList) {
        if (entry.isEmpty()) {
            return;
        }
        if (entry.startsWith("#")) {
            ResourceLocation rl = ResourceLocation.tryParse(entry.substring(1));
            if (rl == null) {
                LOGGER.warn("[PedernalTools] Ignoring invalid fiber plant tag: {}", entry);
                return;
            }
            tagList.add(TagKey.create(Registries.BLOCK, rl));
            return;
        }
        ResourceLocation rl = ResourceLocation.tryParse(entry);
        if (rl == null || !BuiltInRegistries.BLOCK.containsKey(rl)) {
            LOGGER.warn("[PedernalTools] Ignoring unknown fiber plant block: {}", entry);
            return;
        }
        blockSet.add(BuiltInRegistries.BLOCK.get(rl));
    }

    private FiberPlants() {}
}
