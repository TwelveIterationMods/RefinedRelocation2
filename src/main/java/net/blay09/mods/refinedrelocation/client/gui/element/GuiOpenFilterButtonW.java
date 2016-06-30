package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiButtonW;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;

public class GuiOpenFilterButtonW extends GuiButtonW {

	public GuiOpenFilterButtonW(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		super(RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileEntity, buttonId));
	}

	@Override
	public void actionPerformed() {
		TileWrapper tileWrapper = ((GuiOpenFilterButton) button).getTileWrapper();
		NetworkHandler.wrapper.sendToServer(new MessageOpenGui(GuiHandler.GUI_ROOT_FILTER, tileWrapper.getPos()));
	}

}
