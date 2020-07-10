package net.blay09.mods.refinedrelocation.client.render;

import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;

public class SortingChestTileEntityRenderer extends ChestTileEntityRenderer<SortingChestTileEntity> {

    public SortingChestTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected RenderMaterial getMaterial(SortingChestTileEntity tileEntity, ChestType chestType) {
        return tileEntity.getChestType().getMaterial();
    }
}
