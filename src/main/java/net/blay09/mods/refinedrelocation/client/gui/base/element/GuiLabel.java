package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.gui.FontRenderer;

public class GuiLabel extends GuiElement {

	private String text;
	private FontRenderer fontRenderer;
	private int textColor;

	public GuiLabel(String text, FontRenderer fontRenderer, int textColor) {
		this.text = text;
		this.fontRenderer = fontRenderer;
		this.textColor = textColor;
	}

	@Override
	public void drawForeground(IParentScreen parentScreen, int mouseX, int mouseY) {
		super.drawForeground(parentScreen, mouseX, mouseY);
		fontRenderer.drawString(text, getAbsoluteX(), getAbsoluteY(), textColor);
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
