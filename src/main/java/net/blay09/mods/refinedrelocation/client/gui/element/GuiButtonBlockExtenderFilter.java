package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.container.ContainerBlockExtender;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class GuiButtonBlockExtenderFilter extends GuiButton {

    private final boolean isOutputFilter;

    public GuiButtonBlockExtenderFilter(int buttonId, int x, int y, int width, int height, boolean isOutputFilter) {
        super(buttonId, x, y, width, height, I18n.format("gui.refinedrelocation:block_extender.configure"));
        this.isOutputFilter = isOutputFilter;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        RefinedRelocationAPI.sendContainerMessageToServer(ContainerBlockExtender.KEY_CONFIGURE_FILTER, isOutputFilter ? 1 : 0);
    }

}
