package net.blay09.mods.refinedrelocation.client.gui.base.element;

public class GuiScrollPane extends GuiElement {

	private final GuiScrollBar scrollBar;

	public GuiScrollPane(GuiScrollBar scrollBar, int x, int y, int width, int height) {
		this.scrollBar = scrollBar;
		setPosition(x, y);
		setSize(width, height);
	}

	@Override
	public void mouseWheelMoved(int mouseX, int mouseY, int delta) {
		scrollBar.mouseWheelMoved(mouseX, mouseY, delta);
	}

//	@Override
//	public void drawBackground(Minecraft mc, int mouseX, int mouseY) {
//		super.drawBackground(mc, mouseX, mouseY);
//		drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX() + getWidth(), getAbsoluteY() + getHeight(), 0xAAFFFFFF);
//	}

}
