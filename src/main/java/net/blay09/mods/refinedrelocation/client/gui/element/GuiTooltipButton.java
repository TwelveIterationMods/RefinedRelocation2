package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.minecraft.client.gui.GuiButton;

public abstract class GuiTooltipButton extends GuiButton implements ITooltipElement {
    public GuiTooltipButton(int buttonId, int x, int y, String displayString) {
        super(buttonId, x, y, displayString);
    }

    public GuiTooltipButton(int buttonId, int x, int y, int width, int height, String displayString) {
        super(buttonId, x, y, width, height, displayString);
    }
}
