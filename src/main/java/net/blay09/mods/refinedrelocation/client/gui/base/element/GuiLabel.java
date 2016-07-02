package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.gui.FontRenderer;

public class GuiLabel extends GuiElement {

	private String text;
	private int textColor;

	public GuiLabel(int x, int y, String text, int textColor) {
		this.text = text;
		this.textColor = textColor;
		setPosition(x, y);
	}

	@Override
	public void drawForeground(IParentScreen parentScreen, int mouseX, int mouseY) {
		super.drawForeground(parentScreen, mouseX, mouseY);
		parentScreen.getFontRenderer().drawString(text, getAbsoluteX(), getAbsoluteY(), textColor);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

}
