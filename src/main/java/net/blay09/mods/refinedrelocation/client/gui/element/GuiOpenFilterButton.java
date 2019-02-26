package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.network.MessageRequestFilterGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.tileentity.TileEntity;

public class GuiOpenFilterButton extends GuiImageButton {

    private final TileEntity tileEntity;

    public GuiOpenFilterButton(int id, int x, int y, TileEntity tileEntity) {
        super(id, x, y, 12, 12, GuiTextures.OPEN_FILTER);
        this.tileEntity = tileEntity;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        NetworkHandler.channel.sendToServer(new MessageRequestFilterGUI(tileEntity.getPos()));
    }

}
