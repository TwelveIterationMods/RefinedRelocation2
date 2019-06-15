package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;

public class GuiReturnFromFilterButton extends GuiImageButton {

    public GuiReturnFromFilterButton(int x, int y) {
        super(x, y, 16, 16, GuiTextures.CHEST_BUTTON, it -> {
        });
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        NetworkHandler.channel.sendToServer(new MessageReturnGUI());
    }

}
