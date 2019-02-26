package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerReturnable;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiButtonWrapper;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;

public class GuiOpenFilterButtonWrapper extends GuiButtonWrapper {

	private boolean isReturnButton;

	public GuiOpenFilterButtonWrapper(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		super(RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileEntity, buttonId));
		isReturnButton = guiContainer.inventorySlots instanceof IContainerReturnable;
		setPosition(getRelativeX() - guiContainer.guiLeft, getRelativeY() - guiContainer.guiTop);
	}

	@Override
	public void actionPerformed() {
		TileEntity tileEntity = ((GuiOpenFilterButton) button).getTileEntity();
		if(isReturnButton) {
			NetworkHandler.channel.sendToServer(new MessageReturnGUI());
		} else {
			NetworkHandler.channel.sendToServer(new MessageOpenGui(GuiHandler.GUI_ROOT_FILTER, tileEntity.getPos()));
		}
	}

}
