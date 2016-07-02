package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;

public class GuiRootNode extends GuiElement {

	private final IParentScreen parentScreen;

	public GuiRootNode(IParentScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	public IParentScreen getParentScreen() {
		return parentScreen;
	}

	@Override
	public void update() {
		super.update();

		if(getRelativeX() != parentScreen.getLeft() || getRelativeY() != parentScreen.getTop()) {
			setPosition(parentScreen.getLeft(), parentScreen.getTop());
		}

		if(getWidth() != parentScreen.getWidth() || getHeight() != parentScreen.getHeight()) {
			setSize(parentScreen.getWidth(), parentScreen.getHeight());
		}
	}
}
