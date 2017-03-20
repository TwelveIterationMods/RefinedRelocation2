package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OpenFilterButtonHandler {

	@SubscribeEvent
	public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event) {
		if(event.getButton() instanceof GuiOpenFilterButton) {
			TileEntity tileEntity = ((GuiOpenFilterButton) event.getButton()).getTileEntity();
			NetworkHandler.wrapper.sendToServer(new MessageOpenGui(GuiHandler.GUI_ROOT_FILTER, tileEntity.getPos()));
		}
	}

}
