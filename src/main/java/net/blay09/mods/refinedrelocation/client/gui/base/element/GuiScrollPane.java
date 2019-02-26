package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.minecraft.client.gui.IGuiEventListener;

public class GuiScrollPane implements IGuiEventListener {

    private final GuiScrollBar scrollBar;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public GuiScrollPane(GuiScrollBar scrollBar, int x, int y, int width, int height) {
        this.scrollBar = scrollBar;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean mouseScrolled(double delta) {
        // TODO check if we're actually inside of it
        scrollBar.mouseScrolled(delta);
        return true;
    }

}
