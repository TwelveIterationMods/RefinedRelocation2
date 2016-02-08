package net.blay09.mods.refinedrelocation2.client.render;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class TileSortingChestRenderer extends TileEntitySpecialRenderer<TileSortingChest> {

    private static final ResourceLocation texture = new ResourceLocation("textures/entity/chest/normal.png");
    private static final ResourceLocation textureOverlay = new ResourceLocation(RefinedRelocation2.MOD_ID, "textures/entity/sorting_chest_overlay.png");

    private ModelChest model = new ModelChest();

    @Override
    public void renderTileEntityAt(TileSortingChest tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        int metadata;
        if (tileEntity == null || !tileEntity.hasWorldObj()) {
            metadata = 0;
        } else {
            metadata = tileEntity.getBlockMetadata();
        }

        if (destroyStage >= 0) {
            bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4f, 4f, 1f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        } else {
            bindTexture(texture);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0) {
            GlStateManager.color(1f, 1f, 1f, 1f);
        }

        GlStateManager.translate(x, y + 1f, z + 1f);
        GlStateManager.scale(1f, -1f, -1f);
        GlStateManager.translate(0.5f, 0.5f, 0.5f);

        int angle = 0;
        switch (metadata) {
            case 2:
                angle = 180;
                break;
            case 3:
                angle = 0;
                break;
            case 4:
                angle = 90;
                break;
            case 5:
                angle = -90;
                break;
        }

        GlStateManager.rotate(angle, 0f, 1f, 0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);

        float lidAngle = 0f;
        if(tileEntity != null) {
            lidAngle = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
        }
        lidAngle = 1f - lidAngle;
        lidAngle = 1f - lidAngle * lidAngle * lidAngle;
        model.chestLid.rotateAngleX = -(lidAngle * (float) Math.PI / 2f);
        model.renderAll();
        if(destroyStage == -1) {
            GlStateManager.translate(0, -0.01f, 0);
            bindTexture(textureOverlay);
            GlStateManager.enableBlend();
            model.renderAll();
            GlStateManager.disableBlend();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        }
    }

}
