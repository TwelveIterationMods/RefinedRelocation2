package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldWrapper extends GuiElement {

	private final GuiTextField textField;

	public GuiTextFieldWrapper(int componentId, FontRenderer fontRenderer, int x, int y, int width, int height) {
		textField = new GuiTextField(componentId, fontRenderer, x, y, width, height);
	}

	@Override
	public void update() {
		super.update();
		textField.updateCursorCounter();
	}

	@Override
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY, float partialTicks) {
		super.drawBackground(parentScreen, mouseX, mouseY, partialTicks);
		textField.x = getAbsoluteX();
		textField.y = getAbsoluteY();
		textField.width = getWidth();
		textField.height = getHeight();
		textField.drawTextBox();
	}

}
