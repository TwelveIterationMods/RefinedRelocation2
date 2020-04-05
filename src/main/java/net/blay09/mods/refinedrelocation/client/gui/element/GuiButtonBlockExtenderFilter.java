package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.element.SizableButton;
import net.blay09.mods.refinedrelocation.container.BlockExtenderContainer;
import net.minecraft.client.resources.I18n;

public class GuiButtonBlockExtenderFilter extends SizableButton {

    private final boolean isOutputFilter;

    public GuiButtonBlockExtenderFilter(int buttonId, int x, int y, int width, int height, boolean isOutputFilter) {
        super(x, y, width, height, I18n.format("gui.refinedrelocation:block_extender.configure"), it -> {
        });
        this.isOutputFilter = isOutputFilter;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        String key = isOutputFilter ? BlockExtenderContainer.KEY_CONFIGURE_OUTPUT_FILTER : BlockExtenderContainer.KEY_CONFIGURE_INPUT_FILTER;
        RefinedRelocationAPI.sendContainerMessageToServer(key, isOutputFilter ? 1 : 0);
    }

}
