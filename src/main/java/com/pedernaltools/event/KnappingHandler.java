package com.pedernaltools.event;

import com.pedernaltools.config.Config;
import com.pedernaltools.item.ModItems;
import com.pedernaltools.tag.ModTags;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;

/**
 * Knapping: right-clicking a stone-like block (tag {@code pedernaltools:knapping_blocks})
 * with flint in the main hand chips the flint into {@link #SHARDS_PER_FLINT} flint shards,
 * consuming one flint. This replaces the old crafting recipe for flint shards.
 *
 * <p>Registered on the game event bus from the mod constructor, like the other handlers.
 */
public final class KnappingHandler {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!Config.KNAPPING_ENABLED.get()) {
            return;
        }
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        ItemStack held = event.getItemStack();
        if (!held.is(Items.FLINT)) {
            return;
        }
        Level level = event.getLevel();
        if (!level.getBlockState(event.getPos()).is(ModTags.Blocks.KNAPPING_BLOCKS)) {
            return;
        }

        Player player = event.getEntity();
        if (!level.isClientSide) {
            if (Config.KNAPPING_CONSUMES_FLINT.get()) {
                held.shrink(1);
            }
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FLINT_SHARD.get(), Config.SHARDS_PER_FLINT.get()));
            level.playSound(null, event.getPos(), SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.8F, 1.2F);
        }

        // Consume the interaction (no block activation) and swing the arm.
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }

    private KnappingHandler() {}
}
