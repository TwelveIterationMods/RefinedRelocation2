package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextFieldMultiLine;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButtonWrapper;
import net.blay09.mods.refinedrelocation.container.ContainerNameFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiNameFilter extends GuiContainerMod<ContainerNameFilter> implements IFilterPreviewGui {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter.png");

	private static final int UPDATE_INTERVAL = 20;

	private final EntityPlayer player;
	private final TileEntity tileEntity;
	private final GuiTextFieldMultiLine txtFilter;

	private int ticksSinceUpdate;
	private String lastSentText = "";

	public GuiNameFilter(EntityPlayer player, TileEntity tileEntity, NameFilter filter) {
		super(new ContainerNameFilter(player, tileEntity, filter));
		this.player = player;
		this.tileEntity = tileEntity;

		ySize = 210;
		shouldKeyRepeat = true;

		txtFilter = new GuiTextFieldMultiLine(0, 0, 150, 84);
		txtFilter.setFocused(true);
		txtFilter.setCanLoseFocus(false);
		txtFilter.setText(container.getValue());

		GuiScrollBar scrollBar = new GuiScrollBar(161, 20, 83, txtFilter);

		GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, 8, 20, 150, 84);
		rootNode.addChild(scrollPane);
		scrollPane.addChild(txtFilter);

		rootNode.addChild(scrollBar);

		rootNode.addChild(new GuiOpenFilterButtonWrapper(this, container.getTileEntity(), 0));
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

		fontRenderer.drawString(I18n.format("container.refinedrelocation:name_filter"), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	public boolean onGuiAboutToClose() {
		super.onGuiAboutToClose();
		container.sendValueToServer(txtFilter.getText());
		RefinedRelocationAPI.returnToParentContainer();
		return false;
	}

}
