package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiChecklistEntry extends GuiElement {

	private final IChecklistFilter filter;
	private final TextureAtlasRegion texture;
	private final TextureAtlasRegion textureChecked;
	private int currentOption = -1;

	public GuiChecklistEntry(IChecklistFilter filter) {
		this.filter = filter;
		setSize(151, 11);
		texture = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:checklist");
		textureChecked = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:checklist_checked");
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(currentOption != -1) {
			boolean oldState = filter.isOptionChecked(currentOption);
			filter.setOptionChecked(currentOption, !oldState);
			if(!oldState) {
				RefinedRelocationAPI.sendContainerMessageToServer(ContainerChecklistFilter.KEY_CHECK, currentOption);
			} else {
				RefinedRelocationAPI.sendContainerMessageToServer(ContainerChecklistFilter.KEY_UNCHECK, currentOption);
			}
			return true;
		}
		return false;
	}

	@Override
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY) {
		super.drawBackground(parentScreen, mouseX, mouseY);

		if(isInside(mouseX, mouseY)) {
			drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX() + getWidth(), getAbsoluteY() + getHeight(), 0x66FFFFFF);
		}

		GlStateManager.color(1f, 1f, 1f, 1f);

		if(currentOption != -1) {
			if(filter.isOptionChecked(currentOption)) {
				textureChecked.draw(getAbsoluteX() + 1, getAbsoluteY() + getHeight() / 2 - textureChecked.getIconHeight() / 2, zLevel);
			} else {
				texture.draw(getAbsoluteX() + 1, getAbsoluteY() + getHeight() / 2 - texture.getIconHeight() / 2, zLevel);
			}

			drawString(parentScreen.getFontRenderer(), I18n.format(filter.getOptionLangKey(currentOption)), getAbsoluteX() + 14, getAbsoluteY() + getHeight() / 2 - parentScreen.getFontRenderer().FONT_HEIGHT / 2, 0xFFFFFF);
		}
	}

	public void setCurrentOption(int currentOption) {
		this.currentOption = currentOption;
	}

}
