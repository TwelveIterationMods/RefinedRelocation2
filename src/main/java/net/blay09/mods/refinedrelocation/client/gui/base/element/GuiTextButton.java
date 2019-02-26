package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiTextButton extends GuiElement {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/widgets.png");

	protected String text;
	private boolean enabled = true;

	public GuiTextButton(int x, int y, int width, int height, String text) {
		setPosition(x, y);
		setSize(width, height);
		this.text = text;
	}

	@Override
	public final boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isEnabled()) {
			Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1f));
			actionPerformed(mouseButton);
		}
		return true;
	}

	@Override
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY, float partialTicks) {
		super.drawBackground(parentScreen, mouseX, mouseY, partialTicks);
		if (isVisible()) {
			int buttonState = !enabled ? 0 : (parentScreen.getMouseElement() == this ? 2 : 1);
			GuiUtils.drawContinuousTexturedBox(TEXTURE, getAbsoluteX(), getAbsoluteY(), 0, 46 + buttonState * 20, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2, this.zLevel);

			int textColor = 0xE0E0E0;
			if (!enabled) {
				textColor = 0xA0A0A0;
			} else if (parentScreen.getMouseElement() == this) {
				textColor = 0xFFFFA0;
			}
			drawCenteredString(parentScreen.getFontRenderer(), text, getAbsoluteX() + getWidth() / 2, getAbsoluteY() + (getHeight() - 8) / 2, textColor);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void actionPerformed(int mouseButton) {

	}

}
