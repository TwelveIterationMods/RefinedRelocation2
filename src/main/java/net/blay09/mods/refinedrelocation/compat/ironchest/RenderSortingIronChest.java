package net.blay09.mods.refinedrelocation.compat.ironchest;

import com.google.common.primitives.SignedBytes;
import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChestType;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.RefinedRelocationConfig;
import net.blay09.mods.refinedrelocation.client.render.ModelLidOverlay;
import net.blay09.mods.refinedrelocation.client.render.RenderUtils;
import net.blay09.mods.refinedrelocation.client.render.SafeTESR;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderSortingIronChest extends SafeTESR<TileSortingIronChest> {

	private static final ResourceLocation[] OVERLAY = new ResourceLocation[] {
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/iron_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/gold_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/diamond_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/copper_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/silver_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/crystal_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/obsidian_chest_overlay.png"),
			new ResourceLocation(RefinedRelocation.MOD_ID, "textures/blocks/dirt_chest_overlay.png"),
	};

	private static final float[][] shifts = {{0.3f, 0.45f, 0.3f}, {0.7f, 0.45f, 0.3f}, {0.3f, 0.45f, 0.7f}, {0.7f, 0.45f, 0.7f}, {0.3f, 0.1f, 0.3f},
			{0.7f, 0.1f, 0.3f}, {0.3f, 0.1f, 0.7f}, {0.7f, 0.1f, 0.7f}, {0.5f, 0.32f, 0.5f},};
	private static final float halfPI = (float) (Math.PI / 2.0);

	private static EntityItem entityItem;

	private final Random random = new Random();
	private final ModelChest model = new ModelChest();
	private final ModelLidOverlay modelLidOverlay = new ModelLidOverlay();
	private RenderEntityItem itemRenderer;

	public RenderSortingIronChest(Block block) {
		super(block);
	}

	@Override
	protected void renderTileEntityAt(TileSortingIronChest tileEntity, double x, double y, double z, float partialTicks, int destroyStage, @Nullable IBlockState state) {
		if (tileEntity.isInvalid()) {
			return;
		}

		EnumFacing facing = EnumFacing.SOUTH;
		IronChestType type = tileEntity.getType();
		if (state != null) {
			facing = tileEntity.getFacing();
			type = state.getValue(BlockIronChest.VARIANT_PROP);
		}

		// Set up breaking animation or bind texture
		if (destroyStage >= 0) {
			bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4f, 4f, 1f);
			GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		} else {
			bindTexture(type.modelTexture);
		}

		// Set up the Matrix
		GlStateManager.pushMatrix();
		if (type == IronChestType.CRYSTAL) {
			GlStateManager.disableCull();
		}
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.translate(x, y + 1f, z + 1f);
		GlStateManager.scale(1f, -1f, -1f);
		GlStateManager.translate(0.5f, 0.5f, 0.5f);
		GlStateManager.rotate(RenderUtils.getFacingAngle(facing), 0f, 1f, 0f);
		GlStateManager.translate(-0.5f, -0.5f, -0.5f);
		if (type.isTransparent()) {
			GlStateManager.scale(1f, 0.99F, 1f);
		}

		// Do ugly lid angle math
		float lidAngle = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
		lidAngle = 1f - lidAngle;
		lidAngle = 1f - lidAngle * lidAngle * lidAngle;
		model.chestLid.rotateAngleX = -lidAngle * halfPI;
		model.renderAll();

		// Render the lid overlay only on the normal pass
		if(destroyStage == -1) {
			GlStateManager.enableAlpha();
			bindTexture(OVERLAY[type.ordinal()]);
			modelLidOverlay.chestLid.rotateAngleX = -lidAngle * halfPI;
			modelLidOverlay.renderAll();
		}

		// Pop the breaking animation matrix
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(GL11.GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		}

		// Reset everything else
		if (type == IronChestType.CRYSTAL) {
			GlStateManager.enableCull();
		}
		GlStateManager.popMatrix();
		GlStateManager.color(1f, 1f, 1f, 1f);

		// If the chest is transparent, render the top items inside
		if (type.isTransparent() && tileEntity.getDistanceSq(rendererDispatcher.entityX, rendererDispatcher.entityY, rendererDispatcher.entityZ) < 128.0) {
			random.setSeed(254);
			float shiftX;
			float shiftY;
			float shiftZ;
			int shift = 0;
			float blockScale = 0.70f;
			float timeD = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;
			if (tileEntity.getTopItemStacks()[1] == null) {
				shift = 8;
				blockScale = 0.85f;
			}
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);

			if(entityItem == null) {
				entityItem = new EntityItem(getWorld());
			}
			entityItem.hoverStart = 0f;
			for (ItemStack item : tileEntity.getTopItemStacks()) {
				if (shift > shifts.length) {
					break;
				}
				if (item == null) {
					shift++;
					continue;
				}
				shiftX = shifts[shift][0];
				shiftY = shifts[shift][1];
				shiftZ = shifts[shift][2];
				shift++;
				GlStateManager.pushMatrix();
				GlStateManager.translate(shiftX, shiftY, shiftZ);
				GlStateManager.rotate(timeD, 0f, 1f, 0f);
				GlStateManager.scale(blockScale, blockScale, blockScale);
				entityItem.setEntityItemStack(item);

				if (itemRenderer == null) {
					itemRenderer = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
						@Override
						public int getModelCount(ItemStack stack) {
							return SignedBytes.saturatedCast(Math.min(stack.stackSize / 32, 15) + 1);
						}

						@Override
						public boolean shouldBob() {
							return false;
						}

						@Override
						public boolean shouldSpreadItems() {
							return true;
						}
					};
				}

				itemRenderer.doRender(entityItem, 0, 0, 0, 0f, partialTicks);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}

	}

	@Override
	protected boolean shouldRenderNameTag(TileSortingIronChest tileEntity) {
		return tileEntity.hasCustomName() && RefinedRelocationConfig.renderChestNameTags;
	}
}
