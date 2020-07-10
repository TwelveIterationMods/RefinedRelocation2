package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

public class LabelWidget extends Widget {

    private final FontRenderer fontRenderer;
    public int x;
    public int y;
    public ITextComponent text;
    public int textColor;

    public LabelWidget(FontRenderer fontRenderer, int x, int y, ITextComponent text, int textColor) {
        super(x, y, fontRenderer.func_238414_a_(text), fontRenderer.FONT_HEIGHT, text);
        this.fontRenderer = fontRenderer;
        this.x = x;
        this.y = y;
        this.text = text;
        this.textColor = textColor;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        fontRenderer.func_238422_b_(matrixStack, text, x, y, textColor);
    }

}
