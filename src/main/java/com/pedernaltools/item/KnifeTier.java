package com.pedernaltools.item;

/**
 * Cutting tier of a knife. The {@code level} is the knife's cutting capability
 * (how hard a material it can cut), which is NOT the same axis as durability —
 * gold is a soft metal, so its level is low even though it is "metal". A knife
 * can apply a cutting rule only when its level is at least the rule's {@code minTier}.
 *
 * <p>{@code defaultDropChance} is the per-tier default chance to drop fiber when
 * breaking a plant (Mechanic A); the actual value is configurable per tier.
 */
public enum KnifeTier {
    FLINT(0, 0.5),
    STONE(1, 0.6),
    IRON(2, 0.7),
    GOLD(1, 0.6),
    DIAMOND(3, 0.85),
    NETHERITE(4, 1.0),
    QUANTUM(5, 1.0);

    public final int level;
    public final double defaultDropChance;

    KnifeTier(int level, double defaultDropChance) {
        this.level = level;
        this.defaultDropChance = defaultDropChance;
    }
}
