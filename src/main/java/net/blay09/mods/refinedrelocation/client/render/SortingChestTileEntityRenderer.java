package net.blay09.mods.refinedrelocation.client.render;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.ResourceLocation;

public class SortingChestTileEntityRenderer extends ChestTileEntityRenderer<SortingChestTileEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "entity/sorting_chest/normal");

    private static final Material MATERIAL = new Material(Atlases.CHEST_ATLAS, TEXTURE);

    public SortingChestTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    /*@Override
    protected void drawNameplate(SortingChestTileEntity tileEntity, String name, double x, double y, double z, int maxDistance) {
        if (RefinedRelocationConfig.CLIENT.renderChestNameTags.get()) {
            super.drawNameplate(tileEntity, name, x, y, z, maxDistance);
        }
    }*/

    @Override
    protected Material getMaterial(SortingChestTileEntity tileEntity, ChestType chestType) {
        return MATERIAL;
    }
}
