package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.minecraft.client.gui.widget.Widget;

public class GuiScrollPane extends Widget {

    private final GuiScrollBar scrollBar;

    public GuiScrollPane(GuiScrollBar scrollBar, int x, int y, int width, int height) {
        super(x, y, width, height, "");
        this.scrollBar = scrollBar;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isMouseOver(mouseX, mouseY)) {
            scrollBar.mouseScrolled(mouseX, mouseY, delta);
        }

        return true;
    }

}
