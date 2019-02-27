package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;

public class GuiLabel implements IGuiEventListener {

    public int x;
    public int y;
    public String text;
    public int textColor;

    public GuiLabel(int x, int y, String text, int textColor) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.textColor = textColor;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        fontRenderer.drawString(text, x, y, textColor);
    }

}
