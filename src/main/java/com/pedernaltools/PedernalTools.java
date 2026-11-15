package com.pedernaltools;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.pedernaltools.config.Config;
import com.pedernaltools.config.FiberPlants;
import com.pedernaltools.config.KnifeCutting;
import com.pedernaltools.event.FiberHarvestHandler;
import com.pedernaltools.event.KnappingHandler;
import com.pedernaltools.item.ModItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PedernalTools.MODID)
public class PedernalTools {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "pedernaltools";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "pedernaltools" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creative tab for this mod. It has no items yet; tools are added in later phases.
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PEDERNAL_TAB = CREATIVE_MODE_TABS.register("pedernal_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .withTabsBefore(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .icon(() -> ModItems.FLINT_PICKAXE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.FLINT_SHARD.get());
                output.accept(ModItems.PLANT_FIBER.get());
                output.accept(ModItems.PLANT_STRING.get());
                output.accept(ModItems.KNIFE_TEMPLATE.get());
                output.accept(ModItems.FLINT_PICKAXE.get());
                output.accept(ModItems.FLINT_AXE.get());
                output.accept(ModItems.FLINT_KNIFE.get());
                output.accept(ModItems.STONE_KNIFE.get());
                output.accept(ModItems.IRON_KNIFE.get());
                output.accept(ModItems.GOLD_KNIFE.get());
                output.accept(ModItems.DIAMOND_KNIFE.get());
                output.accept(ModItems.NETHERITE_KNIFE.get());
                output.accept(ModItems.QUANTUM_KNIFE.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public PedernalTools(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Registers to the mod event bus so content gets registered
        ModItems.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Game-event handlers
        NeoForge.EVENT_BUS.register(FiberHarvestHandler.class); // knife breaks plants -> fiber
        NeoForge.EVENT_BUS.register(KnappingHandler.class);     // flint on stone -> shards

        // Config at config/pedernaltools/config.toml (subpath forces the folder)
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "pedernaltools/config.toml");
        // Rebuild the config-derived caches whenever the config (re)loads
        modEventBus.addListener(KnifeCutting::onConfigChanged);
        modEventBus.addListener(FiberPlants::onConfigChanged);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Pedernal Tools loaded.");
    }
}
