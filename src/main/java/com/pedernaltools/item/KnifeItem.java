package com.pedernaltools.item;

import com.pedernaltools.config.Config;
import com.pedernaltools.config.KnifeCutting;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

/**
 * A knife. Beyond being a weak 1-damage tool, it cuts off-hand materials into
 * other items: hold the knife in the MAIN hand and an input item in the OFF hand,
 * then right-click. The conversion rules (which input, how many, what output) are
 * data-driven from {@link Config#KNIFE_CUTTING_RULES} via {@link KnifeCutting}, so
 * they can be tuned and extended (e.g. modded plants) without code.
 *
 * <p>All knife tiers (flint, stone, iron, ... quantum) share this class; they only
 * differ in durability, set via {@link Item.Properties} at registration. A knife
 * registered without a durability (the quantum knife) is effectively unbreakable,
 * since {@code hurtAndBreak} is a no-op on a non-damageable item.
 */
public class KnifeItem extends Item {
    private final KnifeTier tier;

    public KnifeItem(Properties properties, KnifeTier tier) {
        super(properties);
        this.tier = tier;
    }

    public KnifeTier tier() {
        return tier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack knife = player.getItemInHand(hand);

        if (!Config.KNIFE_HARVEST_ENABLED.get()) {
            return InteractionResultHolder.pass(knife);
        }
        // The knife drives the interaction from the main hand; the input comes from the off hand.
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(knife);
        }

        ItemStack source = player.getOffhandItem();
        KnifeCutting.Rule rule = KnifeCutting.forInput(source.getItem());
        if (rule == null || source.getCount() < rule.inputCount()) {
            return InteractionResultHolder.pass(knife);
        }
        // This knife must be sharp enough (high enough tier) to cut this material.
        if (tier.level < rule.minTier()) {
            return InteractionResultHolder.pass(knife);
        }

        if (!level.isClientSide) {
            int outputCount = scaledOutput(rule, source);
            source.shrink(rule.inputCount());
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(rule.output(), outputCount));
            int durabilityCost = Config.KNIFE_DURABILITY_COST.get();
            if (durabilityCost > 0) {
                knife.hurtAndBreak(durabilityCost, player, EquipmentSlot.MAINHAND);
            }
            level.playSound(null, player.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        // SUCCESS makes the client play the arm-swing animation.
        return InteractionResultHolder.sidedSuccess(knife, level.isClientSide());
    }

    /**
     * Base output count, scaled down by the input's remaining durability when it is a
     * damageable item and the feature is enabled. A damaged input always yields at least 1.
     */
    private static int scaledOutput(KnifeCutting.Rule rule, ItemStack source) {
        int base = rule.outputCount();
        if (!Config.SCALE_OUTPUT_BY_DURABILITY.get() || !source.isDamageableItem()) {
            return base;
        }
        int max = source.getMaxDamage();
        int remaining = max - source.getDamageValue();
        double fraction = (double) remaining / max;
        return Math.max(1, (int) Math.round(base * fraction));
    }
}
