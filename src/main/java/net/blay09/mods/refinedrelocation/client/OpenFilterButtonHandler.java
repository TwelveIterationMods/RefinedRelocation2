package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.network.MessageRequestFilterGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.GuiScreenEvent;

public class OpenFilterButtonHandler {

    @Deprecated // TODO move to button handler
    public static void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event) {
        if (event.getButton() instanceof GuiOpenFilterButton) {
            TileEntity tileEntity = ((GuiOpenFilterButton) event.getButton()).getTileEntity();
            NetworkHandler.channel.sendToServer(new MessageRequestFilterGUI(tileEntity.getPos()));
        }
    }

}
