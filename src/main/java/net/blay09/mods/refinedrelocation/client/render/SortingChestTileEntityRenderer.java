package net.blay09.mods.refinedrelocation.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.block.SortingChestBlock;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.tileentity.IChestLid;

public class SortingChestTileEntityRenderer extends ChestTileEntityRenderer<SortingChestTileEntity> {

    private final ChestModel chestModel = new ChestModel();

    @Override
    public void render(SortingChestTileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState state = tileEntity.getBlockState();
        SortingChestType chestType = ((SortingChestBlock) state.getBlock()).getChestType();
        bindTexture(chestType.getTextureLocation());
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4f, 4f, 1f);
            GlStateManager.translatef(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            GlStateManager.color4f(1f, 1f, 1f, 1f);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translatef((float) x, (float) y + 1f, (float) z + 1f);
        GlStateManager.scalef(1f, -1f, -1f);
        float angle = state.get(ChestBlock.FACING).getHorizontalAngle();
        if ((double) Math.abs(angle) > 0f) {
            GlStateManager.translatef(0.5f, 0.5f, 0.5f);
            GlStateManager.rotatef(angle, 0f, 1f, 0f);
            GlStateManager.translatef(-0.5f, -0.5f, -0.5f);
        }

        this.applyLidRotation(tileEntity, partialTicks, chestModel);
        chestModel.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private void applyLidRotation(SortingChestTileEntity tileEntity, float partialTicks, ChestModel model) {
        float angle = ((IChestLid) tileEntity).getLidAngle(partialTicks);
        angle = 1f - angle;
        angle = 1f - angle * angle * angle;
        model.getLid().rotateAngleX = -(angle * ((float) Math.PI / 2f));
    }
}
