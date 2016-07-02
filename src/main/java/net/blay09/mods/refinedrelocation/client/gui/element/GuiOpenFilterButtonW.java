package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiButtonW;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;

public class GuiOpenFilterButtonW extends GuiButtonW {

	public GuiOpenFilterButtonW(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		super(RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileEntity, buttonId));
		setPosition(getRelativeX() - guiContainer.guiLeft, getRelativeY() - guiContainer.guiTop);
	}

	public GuiOpenFilterButtonW(GuiContainer guiContainer, TileOrMultipart tileEntity, int buttonId) {
		super(createOpenFilterButton(guiContainer, tileEntity, buttonId));
		setPosition(getRelativeX() - guiContainer.guiLeft, getRelativeY() - guiContainer.guiTop);
	}

	@Override
	public void actionPerformed() {
		TileOrMultipart tileEntity = ((GuiOpenFilterButton) button).getTileEntity();
		NetworkHandler.wrapper.sendToServer(new MessageOpenGui(GuiHandler.GUI_ROOT_FILTER, tileEntity.getPos()));
	}

	private static GuiButton createOpenFilterButton(GuiContainer guiContainer, TileOrMultipart tileEntity, int buttonId) {
		if(tileEntity.isMultipart()) {
			return RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileEntity.getMultipart(), buttonId);
		} else {
			return RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileEntity.getTileEntity(), buttonId);
		}
	}

}
