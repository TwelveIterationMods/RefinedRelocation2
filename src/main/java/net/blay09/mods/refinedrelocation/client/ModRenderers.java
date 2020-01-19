package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenderers {
    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.sortingChest, dispatcher -> new SortingChestTileEntityRenderer());
    }
}
