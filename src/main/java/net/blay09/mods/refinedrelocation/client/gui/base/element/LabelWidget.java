package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;

public class LabelWidget extends Widget {

    public int x;
    public int y;
    public String text;
    public int textColor;

    public LabelWidget(int x, int y, String text, int textColor) {
        super(x, y, text);
        this.x = x;
        this.y = y;
        this.text = text;
        this.textColor = textColor;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        fontRenderer.drawString(text, x, y, textColor);
    }

}
