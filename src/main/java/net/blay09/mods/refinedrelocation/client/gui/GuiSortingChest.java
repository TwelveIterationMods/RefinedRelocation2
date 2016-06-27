package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.blay09.mods.refinedrelocation.util.GridContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiSortingChest extends GuiContainerMod {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

	private final EntityPlayer player;
	private final TileSortingChest tileEntity;

	private GuiButton btnOpenFilter;

	public GuiSortingChest(EntityPlayer player, TileSortingChest tileEntity) {
		super(new ContainerSortingChest(player, tileEntity));
		this.player = player;
		this.tileEntity = tileEntity;
		this.ySize = 168;
	}

	@Override
	public void initGui() {
		super.initGui();

		btnOpenFilter = RefinedRelocationAPI.createOpenFilterButton(this, 0);
		buttonList.add(btnOpenFilter);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == btnOpenFilter) {
			RefinedRelocationAPI.openRootFilterGui(new GridContainer(tileEntity));
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 3 * 18 + 17);
		drawTexturedModalRect(guiLeft, guiTop + 3 * 18 + 17, 0, 126, xSize, 96);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(tileEntity.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

}
