package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.element.SizableButton;
import net.blay09.mods.refinedrelocation.menu.BlockExtenderMenu;
import net.minecraft.network.chat.TranslatableComponent;

public class BlockExtenderFilterButton extends SizableButton {

    private final boolean isOutputFilter;

    public BlockExtenderFilterButton(int buttonId, int x, int y, int width, int height, boolean isOutputFilter) {
        super(x, y, width, height, new TranslatableComponent("gui.refinedrelocation:block_extender.configure"), it -> {
        });
        this.isOutputFilter = isOutputFilter;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        String key = isOutputFilter ? BlockExtenderMenu.KEY_CONFIGURE_OUTPUT_FILTER : BlockExtenderMenu.KEY_CONFIGURE_INPUT_FILTER;
        RefinedRelocationAPI.sendContainerMessageToServer(key, isOutputFilter ? 1 : 0);
    }

}
