package com.pedernaltools.item;

import com.pedernaltools.PedernalTools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Items registered by this mod.
 *
 * <p>We instantiate the vanilla {@link PickaxeItem}/{@link AxeItem} directly with
 * our {@link ModTiers#FLINT} tier — no subclasses, since we add no new behaviour.
 * The vanilla constructors already attach the {@code DataComponents.TOOL} component
 * derived from the tier (see {@code DiggerItem}), which is what BloodyPunch reads.
 *
 * <p>Attack damage is intentionally minimal: the {@code createAttributes} damage
 * param is {@code 0.0F} and the FLINT tier's attack bonus is {@code 0.0F}, so the
 * ATTACK_DAMAGE modifier is 0 and total damage stays at the player's base 1.0
 * (a bare-fist hit) for both tools and the knife. Attack-speed floats keep the
 * vanilla feel (pickaxe {@code -2.8F}, axe {@code -3.2F}).
 */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PedernalTools.MODID);

    public static final DeferredItem<PickaxeItem> FLINT_PICKAXE = ITEMS.register("flint_pickaxe",
            () -> new PickaxeItem(ModTiers.FLINT, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModTiers.FLINT, 0.0F, -2.8F))));

    public static final DeferredItem<AxeItem> FLINT_AXE = ITEMS.register("flint_axe",
            () -> new AxeItem(ModTiers.FLINT, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModTiers.FLINT, 0.0F, -3.2F))));

    // --- Crafting materials (RLCraft-style flint progression) ---

    // Knapped from flint (1 flint -> 2 shards). Used to craft the flint knife.
    public static final DeferredItem<Item> FLINT_SHARD = ITEMS.registerSimpleItem("flint_shard");

    // Harvested from grass/leaves with the flint knife (logic added in a later phase).
    public static final DeferredItem<Item> PLANT_FIBER = ITEMS.registerSimpleItem("plant_fiber");

    // 3 plant fiber -> 1 plant string. Added to the c:strings tag so it works in
    // vanilla string recipes (bow, lead, scaffolding, ...).
    public static final DeferredItem<Item> PLANT_STRING = ITEMS.registerSimpleItem("plant_string");

    // Smithing template consumed to upgrade a knife to the next tier (flint -> stone -> iron -> diamond,
    // plus gold as a side branch). One template per upgrade.
    public static final DeferredItem<Item> KNIFE_TEMPLATE = ITEMS.registerSimpleItem("knife_template");

    // Knives. All share KnifeItem (the cutting behaviour); they only differ in durability,
    // which mirrors the vanilla tool durability of each material. The quantum knife has no
    // durability set -> unbreakable. Netherite/quantum are fire-resistant like vanilla netherite gear.
    public static final DeferredItem<KnifeItem> FLINT_KNIFE = ITEMS.register("flint_knife",
            () -> new KnifeItem(new Item.Properties().durability(32), KnifeTier.FLINT));
    public static final DeferredItem<KnifeItem> STONE_KNIFE = ITEMS.register("stone_knife",
            () -> new KnifeItem(new Item.Properties().durability(131), KnifeTier.STONE));
    public static final DeferredItem<KnifeItem> IRON_KNIFE = ITEMS.register("iron_knife",
            () -> new KnifeItem(new Item.Properties().durability(250), KnifeTier.IRON));
    public static final DeferredItem<KnifeItem> GOLD_KNIFE = ITEMS.register("gold_knife",
            () -> new KnifeItem(new Item.Properties().durability(32), KnifeTier.GOLD));
    public static final DeferredItem<KnifeItem> DIAMOND_KNIFE = ITEMS.register("diamond_knife",
            () -> new KnifeItem(new Item.Properties().durability(1561), KnifeTier.DIAMOND));
    public static final DeferredItem<KnifeItem> NETHERITE_KNIFE = ITEMS.register("netherite_knife",
            () -> new KnifeItem(new Item.Properties().durability(2031).fireResistant(), KnifeTier.NETHERITE));
    public static final DeferredItem<KnifeItem> QUANTUM_KNIFE = ITEMS.register("quantum_knife",
            () -> new KnifeItem(new Item.Properties().fireResistant(), KnifeTier.QUANTUM));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    private ModItems() {}
}
