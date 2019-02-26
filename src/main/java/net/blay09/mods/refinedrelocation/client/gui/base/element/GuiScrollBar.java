package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.minecraft.client.renderer.GlStateManager;

public class GuiScrollBar extends GuiElement {

	private final TextureAtlasRegion scrollbarTop;
	private final TextureAtlasRegion scrollbarMiddle;
	private final TextureAtlasRegion scrollbarBottom;
	private final IScrollTarget scrollTarget;
	private int barY;
	private int barHeight;
	private int indexWhenClicked;
	private int lastNumberOfMoves;
	private int mouseClickY = -1;

	private int lastRowCount;
	private int lastVisibleRows;
	private int lastOffset;

	public GuiScrollBar(int x, int y, int height, IScrollTarget scrollTarget) {
		this.scrollTarget = scrollTarget;
		setPosition(x, y);
		setSize(7, height);
		updatePosition();

		scrollbarTop = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:scrollbar_top");
		scrollbarMiddle = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:scrollbar_middle");
		scrollbarBottom = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:scrollbar_bottom");
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
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && mouseX >= getAbsoluteX() && mouseX < getAbsoluteX() + getWidth() && mouseY >= barY && mouseY < barY + barHeight) {
			mouseClickY = mouseY;
			indexWhenClicked = scrollTarget.getCurrentOffset();
			return true;
		}
		return false;
	}

	@Override
	public void updatePosition() {
		super.updatePosition();

		barHeight = (int) (getHeight() * Math.min(1f, ((float) scrollTarget.getVisibleRows() / (Math.ceil(scrollTarget.getRowCount())))));
		barY = getAbsoluteY() + ((getHeight() - barHeight) * scrollTarget.getCurrentOffset() / Math.max(1, (int) Math.ceil((scrollTarget.getRowCount())) - scrollTarget.getVisibleRows()));
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);

		updatePosition();
	}

	@Override
	public void update() {
		super.update();

		if(lastRowCount != scrollTarget.getRowCount() || lastVisibleRows != scrollTarget.getVisibleRows() || lastOffset != scrollTarget.getCurrentOffset()) {
			updatePosition();
			lastRowCount = scrollTarget.getRowCount();
			lastVisibleRows = scrollTarget.getVisibleRows();
			lastOffset = scrollTarget.getCurrentOffset();
		}
	}

	@Override
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY, float partialTicks) {
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

		GlStateManager.color4f(1f, 1f, 1f, 1f);
		scrollbarTop.draw(getAbsoluteX() - 2, getAbsoluteY() - 1, zLevel);
		scrollbarBottom.draw(getAbsoluteX() - 2, getAbsoluteY() + getHeight() - 1, zLevel);
		scrollbarMiddle.draw(getAbsoluteX() - 2, getAbsoluteY() + 2, scrollbarMiddle.getWidth(), getHeight() - 3, zLevel);

		drawRect(getAbsoluteX(), barY, getAbsoluteX() + getWidth(), barY + barHeight, 0xFFAAAAAA);

		super.drawBackground(parentScreen, mouseX, mouseY, partialTicks);
	}

	public void setCurrentOffset(int offset) {
		int currentOffset = Math.max(0, Math.min(offset, (int) (Math.ceil(scrollTarget.getRowCount()) - scrollTarget.getVisibleRows())));
		scrollTarget.setCurrentOffset(currentOffset);
		updatePosition();
	}

}
