package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.SoundEvents;

public class GuiButtonW extends GuiElement {

	protected final GuiButton button;

	public GuiButtonW(int buttonId, int x, int y, int width, int height, String text) {
		button = new GuiButton(buttonId, x, y, width, height, text);
	}

	public GuiButtonW(GuiButton button) {
		this.button = button;
		setPosition(button.xPosition, button.yPosition);
		setSize(button.width, button.height);
	}

	@Override
	public final void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Minecraft mc = Minecraft.getMinecraft();
		if (button.mousePressed(mc, mouseX, mouseY)) {
			button.playPressSound(mc.getSoundHandler());
			actionPerformed();
		}
	}

	@Override
	public void drawForeground(Minecraft mc, int mouseX, int mouseY) {
		super.drawForeground(mc, mouseX, mouseY);
		button.xPosition = getAbsoluteX();
		button.yPosition = getAbsoluteY();
		button.width = getWidth();
		button.height = getHeight();
		button.drawButton(mc, mouseX, mouseY);
	}

	public void actionPerformed() {

	}

}
