package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.minecraft.client.gui.widget.button.Button;

public class GuiScrollBar extends Button implements ITickableElement {

    private final IDrawable scrollbarTop;
    private final IDrawable scrollbarMiddle;
    private final IDrawable scrollbarBottom;
    private final IScrollTarget scrollTarget;

    private int barY;
    private int barHeight;
    private int indexWhenClicked;
    private int lastNumberOfMoves;
    private double mouseClickY = -1;

    private int lastRowCount;
    private int lastVisibleRows;
    private int lastOffset;

    public GuiScrollBar(int x, int y, int height, IScrollTarget scrollTarget) {
        super(x, y, 7, height, "", it -> {});
        this.scrollTarget = scrollTarget;
        updateBarPosition();

        scrollbarTop = GuiTextures.SCROLLBAR_TOP;
        scrollbarMiddle = GuiTextures.SCROLLBAR_MIDDLE;
        scrollbarBottom = GuiTextures.SCROLLBAR_BOTTOM;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // TODO check if inside
        setCurrentOffset(delta > 0 ? scrollTarget.getCurrentOffset() - 1 : scrollTarget.getCurrentOffset() + 1);
        return true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (mouseClickY != -1) {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseX >= x && mouseX < x + getWidth() && mouseY >= barY && mouseY < barY + barHeight) {
            mouseClickY = mouseY;
            indexWhenClicked = scrollTarget.getCurrentOffset();
        }
    }

    private void updateBarPosition() {
        barHeight = (int) (height * Math.min(1f, ((float) scrollTarget.getVisibleRows() / (Math.ceil(scrollTarget.getRowCount())))));
        barY = y + ((height - barHeight) * scrollTarget.getCurrentOffset() / Math.max(1, (int) Math.ceil((scrollTarget.getRowCount())) - scrollTarget.getVisibleRows()));
    }

    @Override
    public void tick() {
        if (lastRowCount != scrollTarget.getRowCount() || lastVisibleRows != scrollTarget.getVisibleRows() || lastOffset != scrollTarget.getCurrentOffset()) {
            updateBarPosition();
            lastRowCount = scrollTarget.getRowCount();
            lastVisibleRows = scrollTarget.getVisibleRows();
            lastOffset = scrollTarget.getCurrentOffset();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (mouseClickY != -1) {
            float pixelsPerFilter = (height - barHeight) / (float) Math.max(1, (int) Math.ceil(scrollTarget.getRowCount()) - scrollTarget.getVisibleRows());
            if (pixelsPerFilter != 0) {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves) {
                    setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        GlStateManager.color4f(1f, 1f, 1f, 1f);
        scrollbarTop.bind();
        scrollbarTop.draw(x - 2, y - 1, blitOffset);
        scrollbarBottom.draw(x - 2, y + height - 1, blitOffset);
        scrollbarMiddle.draw(x - 2, y + 2, 11, height - 3, blitOffset);
//
        fill(x, barY, x + getWidth(), barY + barHeight, 0xFFAAAAAA);
    }

    public void setCurrentOffset(int offset) {
        int currentOffset = Math.max(0, Math.min(offset, (int) (Math.ceil(scrollTarget.getRowCount()) - scrollTarget.getVisibleRows())));
        scrollTarget.setCurrentOffset(currentOffset);
        updateBarPosition();
    }

}
