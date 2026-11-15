package com.pedernaltools.event;

import com.pedernaltools.config.Config;
import com.pedernaltools.config.FiberPlants;
import com.pedernaltools.item.KnifeItem;
import com.pedernaltools.item.ModItems;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Mechanic A of the flint progression: breaking a small plant (configurable via
 * {@code fiberPlants}: grass, ferns, flowers...) while holding a knife in the main
 * hand drops plant fiber, on top of any normal drop. The drop chance depends on the
 * knife's tier (configurable per tier).
 *
 * <p>Registered on the game event bus from the mod constructor, matching the
 * BloodyPunch handler style.
 */
public final class FiberHarvestHandler {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!Config.BREAK_HARVEST_ENABLED.get()) {
            return;
        }
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        ItemStack knife = player.getMainHandItem();
        if (!(knife.getItem() instanceof KnifeItem knifeItem)) {
            return;
        }
        if (!(event.getLevel() instanceof Level level) || level.isClientSide) {
            return;
        }
        BlockState state = event.getState();
        if (!FiberPlants.matches(state)) {
            return;
        }
        double chance = Config.BREAK_DROP_CHANCE.get(knifeItem.tier()).get();
        if (level.getRandom().nextDouble() >= chance) {
            return;
        }
        Block.popResource(level, event.getPos(),
                new ItemStack(ModItems.PLANT_FIBER.get(), Config.FIBER_PER_PLANT.get()));

        int cost = Config.BREAK_DURABILITY_COST.get();
        if (cost > 0) {
            knife.hurtAndBreak(cost, player, EquipmentSlot.MAINHAND);
        }
    }

    private FiberHarvestHandler() {}
}
