package net.blay09.mods.refinedrelocation.client.render;

import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;

public class SortingChestTileEntityRenderer extends ChestTileEntityRenderer<SortingChestTileEntity> {

    public SortingChestTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected Material getMaterial(SortingChestTileEntity tileEntity, ChestType chestType) {
        return tileEntity.getChestType().getMaterial();
    }
}
