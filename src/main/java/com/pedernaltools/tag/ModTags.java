package com.pedernaltools.tag;

import com.pedernaltools.PedernalTools;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/** Tags declared by this mod. Their contents live in data/pedernaltools/tags/. */
public final class ModTags {
    public static final class Blocks {
        /** Stone-like blocks you can knap flint against (right-click with flint) to get shards. */
        public static final TagKey<Block> KNAPPING_BLOCKS = tag("knapping_blocks");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(PedernalTools.MODID, name));
        }

        private Blocks() {}
    }

    private ModTags() {}
}
