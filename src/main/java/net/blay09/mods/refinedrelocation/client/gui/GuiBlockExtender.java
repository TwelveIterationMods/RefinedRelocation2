package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiSideButton;
import net.blay09.mods.refinedrelocation.container.ContainerBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GuiBlockExtender extends GuiContainerMod<ContainerBlockExtender> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID,"textures/gui/block_extender.png");

	private final TileBlockExtender tileEntity;

	public GuiBlockExtender(EntityPlayer player, TileBlockExtender tileEntity, EnumFacing clickedFace) {
		super(new ContainerBlockExtender(player, tileEntity));
		this.tileEntity = tileEntity;
		ySize = 175;

		RelativeSide centerFace = RelativeSide.fromFacing(tileEntity.getFacing(), clickedFace);
		RelativeSide topFace = RelativeSide.TOP;
		RelativeSide leftFace;
		if(clickedFace.getAxis() == EnumFacing.Axis.Y) {
			centerFace = RelativeSide.FRONT;
			topFace = RelativeSide.TOP;
			leftFace = RelativeSide.LEFT;
		} else if(tileEntity.getFacing().getAxis() == EnumFacing.Axis.Y) {
			leftFace = clickedFace.getAxis() == EnumFacing.Axis.Z ? centerFace.rotateX() : centerFace.rotateX().getOpposite();
			if(tileEntity.getFacing().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
				leftFace = leftFace.getOpposite();
				topFace = RelativeSide.FRONT;
			} else {
				topFace = RelativeSide.BACK;
			}
		} else {
			leftFace = centerFace.rotateY();
		}
		rootNode.addChild(new GuiSideButton(9, 40, tileEntity, leftFace));
		rootNode.addChild(new GuiSideButton(26, 40, tileEntity, centerFace));
		rootNode.addChild(new GuiSideButton(43, 40, tileEntity, leftFace.getOpposite()));
		rootNode.addChild(new GuiSideButton(60, 40, tileEntity, centerFace.getOpposite()));
		rootNode.addChild(new GuiSideButton(26, 23, tileEntity, topFace));
		rootNode.addChild(new GuiSideButton(26, 57, tileEntity, topFace.getOpposite()));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(tileEntity.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

}
