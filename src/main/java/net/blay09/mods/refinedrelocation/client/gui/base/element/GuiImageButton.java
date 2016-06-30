package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class GuiImageButton extends GuiElement {

	private final TextureAtlasRegion background;
	private final TextureAtlasRegion backgroundHover;
	private final TextureAtlasRegion backgroundDisabled;
	private boolean enabled = true;

	public GuiImageButton(int x, int y, String textureName) {
		setPosition(x, y);

		background = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:" + textureName);
		backgroundHover = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:" + textureName + "_hover");
		backgroundDisabled = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:" + textureName + "_disabled");

		setSize(background.getIconWidth(), background.getIconHeight());
	}

	@Override
	public final void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1f));
		actionPerformed();
	}

	@Override
	public void drawForeground(Minecraft mc, int mouseX, int mouseY) {
		super.drawForeground(mc, mouseX, mouseY);
		if (isVisible()) {
			GlStateManager.color(1f, 1f, 1f, 1f);
			if(!enabled) {
				backgroundDisabled.draw(getAbsoluteX(), getAbsoluteY(), zLevel);
			} else {
				if(isInside(mouseX, mouseY)) {
					backgroundHover.draw(getAbsoluteX(), getAbsoluteY(), zLevel);
				} else {
					background.draw(getAbsoluteX(), getAbsoluteY(), zLevel);
				}
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void actionPerformed() {

	}

}
