package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButtonWrapper;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSortingChest extends GuiContainerMod<ContainerSortingChest> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	private final TileSortingChest tileEntity;

	public GuiSortingChest(EntityPlayer player, TileSortingChest tileEntity) {
		super(new ContainerSortingChest(player, tileEntity));
		this.tileEntity = tileEntity;
		this.ySize = 168;

		rootNode.addChild(new GuiOpenFilterButtonWrapper(this, tileEntity, 0));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 3 * 18 + 17);
		drawTexturedModalRect(guiLeft, guiTop + 3 * 18 + 17, 0, 126, xSize, 96);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(tileEntity.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

}
