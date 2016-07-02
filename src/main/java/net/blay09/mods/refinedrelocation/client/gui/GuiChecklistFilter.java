package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButtonW;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiChecklistFilter extends GuiContainerMod<ContainerChecklistFilter> implements IScrollTarget {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/checklistFilter.png");

	private final IChecklistFilter filter;

	private int currentOffset;

	public GuiChecklistFilter(EntityPlayer player, TileOrMultipart tileEntity, IChecklistFilter filter) {
		super(new ContainerChecklistFilter(player, tileEntity, filter));
		this.filter = filter;

		ySize = 210;

		GuiScrollBar scrollBar = new GuiScrollBar(xSize - 16, 28, 78, this);
		rootNode.addChild(scrollBar);

		GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, 8, 28, 152, 80);
		rootNode.addChild(scrollPane);

		rootNode.addChild(new GuiOpenFilterButtonW(this, container.getTileEntity(), 0));

		setCurrentOffset(0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	public int getVisibleRows() {
		return 7;
	}

	@Override
	public int getRowCount() {
		return filter.getOptionCount();
	}

	@Override
	public int getCurrentOffset() {
		return currentOffset;
	}

	@Override
	public void setCurrentOffset(int offset) {
		this.currentOffset = offset;
	}
}
