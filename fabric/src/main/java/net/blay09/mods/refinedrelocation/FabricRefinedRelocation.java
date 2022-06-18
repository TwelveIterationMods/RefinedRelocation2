package net.blay09.mods.refinedrelocation;

import net.blay09.mods.balm.api.Balm;
import net.fabricmc.api.ModInitializer;

public class FabricRefinedRelocation implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(RefinedRelocation.MOD_ID, RefinedRelocation::initialize);
    }
}
