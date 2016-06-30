package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.InternalMethodsImpl;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiFilterSlot;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiRootFilter extends GuiContainerMod<ContainerRootFilter> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/rootFilter.png");

	private final EntityPlayer player;
	private final TileWrapper tileWrapper;

	public GuiRootFilter(EntityPlayer player, TileWrapper tileWrapper) {
		super(new ContainerRootFilter(player, tileWrapper));
		this.player = player;
		this.tileWrapper = tileWrapper;

		ySize = 210;
	}

	@Override
	public void initGui() {
		super.initGui();

		GuiFilterSlot filterSlot = new GuiFilterSlot(this, container.getRootFilter(), 0);
		filterSlot.setPosition(guiLeft + 10, guiTop + 30);
		rootNode.addChild(filterSlot);

		filterSlot = new GuiFilterSlot(this, container.getRootFilter(), 1);
		filterSlot.setPosition(guiLeft + 50, guiTop + 30);
		rootNode.addChild(filterSlot);

		filterSlot = new GuiFilterSlot(this, container.getRootFilter(), 2);
		filterSlot.setPosition(guiLeft + 90, guiTop + 30);
		rootNode.addChild(filterSlot);

		ITileGuiHandler tileGuiHandler = InternalMethodsImpl.getGuiHandler(tileWrapper.getTileEntity().getClass());
		if(tileGuiHandler != null) {
			GuiImageButton btnReturn = new GuiImageButton(guiLeft + xSize - 20, guiTop + 4, "chest_button") {
				@Override
				public void actionPerformed() {
					NetworkHandler.wrapper.sendToServer(new MessageReturnGUI());
				}
			};
			rootNode.addChild(btnReturn);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		final int x = guiLeft + 10;
		final int y = guiTop + 30;
		GlStateManager.enableBlend();
		ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_separator").draw(x + 30, y + 7, zLevel);
		ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_separator").draw(x + 70, y + 7, zLevel);

		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRendererObj.drawString("Sorting Chest (Filter)"/*I18n.format("container.refinedrelocation:rootFilter")*/, 8, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);

		fontRendererObj.drawSplitString("Configure up to three filters by clicking the empty slots above.", 10, 70, 120, 4210752);
	}

}
