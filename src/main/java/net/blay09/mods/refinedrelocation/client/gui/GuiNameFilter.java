package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextFieldMultiLine;
import net.blay09.mods.refinedrelocation.container.ContainerNameFilter;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiNameFilter extends GuiContainerMod<ContainerNameFilter> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/rootFilter.png");

	private static final int UPDATE_INTERVAL = 20;

	private GuiTextFieldMultiLine txtFilter;
	private int ticksSinceUpdate;
	private String lastSentText = "";

	public GuiNameFilter(EntityPlayer player, TileWrapper tileWrapper, int filterIndex) {
		super(new ContainerNameFilter(player, tileWrapper, filterIndex));

		ySize = 210;
		shouldKeyRepeat = true;
	}

	@Override
	public void initGui() {
		super.initGui();

		txtFilter = new GuiTextFieldMultiLine(fontRendererObj, guiLeft + 8, guiTop + 20, 160, 90);
		txtFilter.setFocused(true);
		txtFilter.setCanLoseFocus(false);
		txtFilter.setText(container.getValue());
		rootNode.addChild(txtFilter);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		// Sync from Server
		if(container.doesGuiNeedUpdate()) {
			txtFilter.setText(container.getValue());
			lastSentText = container.getValue();
			container.markGuiNeedsUpdate(false);
		}

		// Sync to Server
		ticksSinceUpdate++;
		if(ticksSinceUpdate >= UPDATE_INTERVAL) {
			if(!lastSentText.equals(txtFilter.getText())) {
				container.sendValueToServer(txtFilter.getText());
				lastSentText = txtFilter.getText();
			}
			ticksSinceUpdate = 0;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		fontRendererObj.drawString("Sorting Chest (Name Filter)", 8, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	public void onGuiAboutToClose() {
		super.onGuiAboutToClose();
		container.sendValueToServer(txtFilter.getText());
	}

}
