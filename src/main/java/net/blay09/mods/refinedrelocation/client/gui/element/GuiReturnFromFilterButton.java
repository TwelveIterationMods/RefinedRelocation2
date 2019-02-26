package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;

public class GuiReturnFromFilterButton extends GuiImageButton {

    public GuiReturnFromFilterButton(int buttonId, int x, int y) {
        super(buttonId, x, y, 12, 12, GuiTextures.RETURN_FROM_FILTER);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        NetworkHandler.channel.sendToServer(new MessageReturnGUI());
    }

}
