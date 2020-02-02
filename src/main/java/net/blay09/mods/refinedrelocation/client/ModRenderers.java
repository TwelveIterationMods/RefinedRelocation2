package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenderers {
    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.sortingChest, SortingChestTileEntityRenderer::new);

        RenderTypeLookup.setRenderLayer(ModBlocks.blockExtender, it -> it == RenderType.cutout() || it == RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.sortingInterface, it -> it == RenderType.cutout() || it == RenderType.translucent());
    }
}
