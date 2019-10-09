package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenderers {
    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(SortingChestTileEntity.class, new SortingChestTileEntityRenderer());
    }
}
