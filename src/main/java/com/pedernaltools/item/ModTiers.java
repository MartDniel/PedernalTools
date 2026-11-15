package com.pedernaltools.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * Custom {@link Tier}s for this mod.
 *
 * <p>Flint mines as fast as WOOD but is deliberately far more fragile (low
 * durability) — a primitive, early-game tool. It mines and drops the same blocks
 * as a wooden tool ({@code INCORRECT_FOR_WOODEN_TOOL} = the set it CANNOT get
 * drops from), so stone/cobblestone drop fine while iron ore and above do not.
 * This is intentional progression gating, not a bug.
 *
 * <p>Because flint tools extend the vanilla tool classes (PickaxeItem/AxeItem),
 * this tier's {@code speed} feeds the item's TOOL data component, which is what
 * BloodyPunch reads ({@code getDestroySpeed(state) > 1}) to treat the tool as
 * effective.
 */
public final class ModTiers {
    public static final Tier FLINT = new SimpleTier(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL, // blocks this tier CANNOT get drops from (= wood level)
            30,                                  // uses (durability): deliberately fragile, well below wood (59)
            2.0F,                                // mining speed: same as wood
            0.0F,                                // attack damage bonus: same as wood
            5,                                   // enchantment value: primitive, low
            () -> Ingredient.of(Items.FLINT));   // repaired with flint

    private ModTiers() {}
}
