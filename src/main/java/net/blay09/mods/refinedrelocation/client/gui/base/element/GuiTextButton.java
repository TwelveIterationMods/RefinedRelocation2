package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

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
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1f));
		actionPerformed(mouseButton);
		return true;
	}

	@Override
	public void drawForeground(IParentScreen parentScreen, int mouseX, int mouseY) {
		super.drawForeground(parentScreen, mouseX, mouseY);
		if (isVisible()) {
			int buttonState = !enabled ? 0 : (parentScreen.getMouseElement() == this ? 2 : 1);
			GlStateManager.color(1f, 1f, 1f, 1f);
			parentScreen.getMinecraft().getTextureManager().bindTexture(TEXTURE);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			drawTexturedModalRect(getAbsoluteX(), getAbsoluteY(), 0, 46 + buttonState * 20, getWidth() / 2, getHeight());
			drawTexturedModalRect(getAbsoluteX() + getWidth() / 2, getAbsoluteY(), 200 - getWidth() / 2, 46 + buttonState * 20, getWidth() / 2, getHeight());

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
