package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public class GuiScrollPane extends Widget {

    private final GuiScrollBar scrollBar;

    public GuiScrollPane(GuiScrollBar scrollBar, int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""));
        this.scrollBar = scrollBar;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isMouseOver(mouseX, mouseY)) {
            scrollBar.forceMouseScrolled(delta);
            return true;
        }

        return false;
    }

    @Override
    protected boolean clicked(double p_clicked_1_, double p_clicked_3_) {
        return false;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
    }
}
