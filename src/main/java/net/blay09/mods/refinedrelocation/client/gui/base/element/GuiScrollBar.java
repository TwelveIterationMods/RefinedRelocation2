package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.minecraft.client.Minecraft;

public class GuiScrollBar extends GuiElement {

	private final IScrollTarget scrollTarget;
	private int barY;
	private int barHeight;
	private int indexWhenClicked;
	private int lastNumberOfMoves;
	private int mouseClickY = -1;

	public GuiScrollBar(int x, int y, int height, IScrollTarget scrollTarget) {
		this.scrollTarget = scrollTarget;
		setPosition(x, y);
		setSize(7, height);
		updatePosition();
	}

	@Override
	public void mouseWheelMoved(int mouseX, int mouseY, int delta) {
		setCurrentOffset(delta > 0 ? scrollTarget.getCurrentOffset() - 1 : scrollTarget.getCurrentOffset() + 1);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (mouseClickY != -1) {
			mouseClickY = -1;
			indexWhenClicked = 0;
			lastNumberOfMoves = 0;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && mouseX >= getAbsoluteX() && mouseX < getAbsoluteX() + getWidth() /*&& mouseY >= barY && mouseY < barY + barHeight*/) {
			mouseClickY = mouseY;
			indexWhenClicked = scrollTarget.getCurrentOffset();
		}
	}

	@Override
	public void updatePosition() {
		super.updatePosition();

		barHeight = (int) (getHeight() * Math.min(1f, ((float) scrollTarget.getVisibleRows() / (Math.ceil(scrollTarget.getRowCount())))));
		barY = getAbsoluteY() + ((getHeight() - barHeight) * scrollTarget.getCurrentOffset() / Math.max(1, (int) Math.ceil((scrollTarget.getRowCount())) - scrollTarget.getVisibleRows()));
	}

	@Override
	public void drawBackground(Minecraft mc, int mouseX, int mouseY) {
		if (mouseClickY != -1) {
			float pixelsPerFilter = (getHeight() - barHeight) / (float) Math.max(1, (int) Math.ceil(scrollTarget.getRowCount()) - scrollTarget.getVisibleRows());
			if (pixelsPerFilter != 0) {
				int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
				if (numberOfFiltersMoved != lastNumberOfMoves) {
					setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
					lastNumberOfMoves = numberOfFiltersMoved;
				}
			}
		}

		drawRect(getAbsoluteX(), barY, getAbsoluteX() + getWidth(), barY + barHeight, 0xFFAAAAAA);

		super.drawBackground(mc, mouseX, mouseY);
	}

	public void setCurrentOffset(int offset) {
		int currentOffset = Math.max(0, Math.min(offset, (int) (Math.ceil(scrollTarget.getRowCount()) - scrollTarget.getVisibleRows())));
		scrollTarget.setCurrentOffset(currentOffset);
		updatePosition();
	}

}