package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.InternalMethodsImpl;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiDeleteFilterButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiFilterSlot;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiRootFilter extends GuiContainerMod<ContainerRootFilter> {

	// TODO Priority Button
	// TODO Whitelist/Blacklist Toggle

	private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/rootFilter.png");

	public GuiRootFilter(EntityPlayer player, TileOrMultipart tileEntity) {
		super(new ContainerRootFilter(player, tileEntity));

		ySize = 210;

		final GuiFilterSlot[] filterSlots = new GuiFilterSlot[3];
		final GuiDeleteFilterButton[] deleteButtons = new GuiDeleteFilterButton[3];

		int x = 10;
		for(int i = 0; i < filterSlots.length; i++) {
			filterSlots[i] = new GuiFilterSlot(this, container.getRootFilter(), i);
			filterSlots[i].setPosition(x, 30);
			rootNode.addChild(filterSlots[i]);

			deleteButtons[i] = new GuiDeleteFilterButton(x + 19, 27, filterSlots[i]);
			rootNode.addChild(deleteButtons[i]);
			x += 40;
		}

		ITileGuiHandler tileGuiHandler = InternalMethodsImpl.getGuiHandler(tileEntity.isMultipart() ? tileEntity.getMultipart().getClass() : tileEntity.getTileEntity().getClass());
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
		fontRendererObj.drawString("Sorting Chest (Filter)", 8, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);

		fontRendererObj.drawSplitString("Configure up to three filters by clicking the empty slots above.", 10, 70, 120, 4210752);
	}

}
