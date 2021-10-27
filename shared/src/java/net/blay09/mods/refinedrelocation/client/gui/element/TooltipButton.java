package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.SizableButton;
import net.minecraft.network.chat.Component;

public abstract class TooltipButton extends SizableButton implements ITooltipElement {
    public TooltipButton(int x, int y, int width, int height, Component displayString, OnPress onPress) {
        super(x, y, width, height, displayString, onPress);
    }
}
