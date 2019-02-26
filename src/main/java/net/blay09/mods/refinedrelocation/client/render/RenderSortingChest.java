package net.blay09.mods.refinedrelocation.client.render;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.RefinedRelocationConfig;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderSortingChest extends SafeTESR<TileSortingChest> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/chest/normal.png");
    private static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/entity/sorting_overlay_wood.png");

    private final ModelChest model = new ModelChest();

    public RenderSortingChest() {
        super(ModBlocks.sortingChest);
    }

    @Override
    public void renderTileEntityAt(TileSortingChest tileEntity, double x, double y, double z, float partialTicks, int destroyStage, @Nullable IBlockState state) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);

        if (destroyStage >= 0) {
            bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4f, 4f, 1f);
            GlStateManager.translatef(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        } else {
            bindTexture(TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0) {
            GlStateManager.color4f(1f, 1f, 1f, 1f);
        }

        GlStateManager.translated(x, y + 1, z + 1);
        GlStateManager.scalef(1f, -1f, -1f);
        GlStateManager.translatef(0.5f, 0.5f, 0.5f);
        float angle = state != null ? RenderUtils.getFacingAngle(state) : 0f;
        GlStateManager.rotatef(angle, 0f, 1f, 0f);
        GlStateManager.translatef(-0.5f, -0.5f, -0.5f);
        float lidAngle = tileEntity.getDoorAnimator().getRenderAngle(partialTicks);
        model.getLid().rotateAngleX = -(lidAngle * ((float) Math.PI / 2f));
        model.renderAll();
        if (destroyStage == -1) {
            bindTexture(TEXTURE_OVERLAY);
            GlStateManager.translatef(0f, -0.001f, 0f);
            model.renderAll();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1f, 1f, 1f, 1f);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        }
    }

    @Override
    protected boolean shouldRenderNameTag(TileSortingChest tileEntity) {
        return RefinedRelocationConfig.CLIENT.renderChestNameTags.get();
    }

}
