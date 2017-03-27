package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonWrapper extends GuiElement {

	protected final GuiButton button;

	public GuiButtonWrapper(int buttonId, int x, int y, int width, int height, String text) {
		button = new GuiButton(buttonId, x, y, width, height, text);
	}

	public GuiButtonWrapper(GuiButton button) {
		this.button = button;
		setPosition(button.xPosition, button.yPosition);
		setSize(button.width, button.height);
	}

	@Override
	public final boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Minecraft mc = Minecraft.getMinecraft();
		if (button.mousePressed(mc, mouseX, mouseY)) {
			button.playPressSound(mc.getSoundHandler());
			actionPerformed();
			return true;
		}
		return false;
	}

	@Override
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY) {
		super.drawBackground(parentScreen, mouseX, mouseY);
		button.xPosition = getAbsoluteX();
		button.yPosition = getAbsoluteY();
		button.width = getWidth();
		button.height = getHeight();
		button.drawButton(parentScreen.getMinecraft(), mouseX, mouseY);
	}

	public void actionPerformed() {

	}

}
