package com.pedernaltools;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = PedernalTools.MODID, dist = Dist.CLIENT)
public class PedernalToolsClient {
    public PedernalToolsClient(ModContainer container) {
        // Client-only setup goes here in later phases (e.g. item model/colour registration).
    }
}
