package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenderers {
    public static void register() {
        for (TileEntityType<SortingChestTileEntity> sortingChest : ModTileEntities.sortingChests) {
            ClientRegistry.bindTileEntityRenderer(sortingChest, SortingChestTileEntityRenderer::new);
        }

        RenderTypeLookup.setRenderLayer(ModBlocks.blockExtender, it -> it == RenderType.getCutout() || it == RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.sortingInterface, it -> it == RenderType.getCutout() || it == RenderType.getTranslucent());
    }
}
