package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenderers {
    public static void register() {
        for (TileEntityType<SortingChestTileEntity> sortingChest : ModTileEntities.sortingChests) {
            ClientRegistry.bindTileEntitySpecialRenderer(SortingChestTileEntity.class, new SortingChestTileEntityRenderer());
        }
    }
}
