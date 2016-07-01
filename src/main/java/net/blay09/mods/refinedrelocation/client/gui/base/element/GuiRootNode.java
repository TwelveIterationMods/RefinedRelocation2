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
}
