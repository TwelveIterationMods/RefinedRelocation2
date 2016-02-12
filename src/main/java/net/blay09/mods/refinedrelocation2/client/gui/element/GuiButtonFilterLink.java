package net.blay09.mods.refinedrelocation2.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonFilterLink extends GuiButton {

    private final int filterIndex;

    public GuiButtonFilterLink(int buttonId, int x, int y, int filterIndex) {
        super(buttonId, x, y, 68, 18, "No Filter");
        this.filterIndex = filterIndex;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        drawCenteredString(mc.fontRendererObj, displayString, xPosition + width / 2, yPosition + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2 - 1, hovered ? 0xFFFFFF : 0xE0E0E0);
    }

}
