package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.api.filter.IConfigurableFilter;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiAddFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GuiAddFilterButton extends GuiElement {

	private final GuiAddFilter parentGui;
	private IFilter currentFilter;

	public GuiAddFilterButton(GuiAddFilter parentGui) {
		this.parentGui = parentGui;
		setSize(151, 27);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(currentFilter != null) {
			RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_ADD_FILTER, currentFilter.getIdentifier());
			if(!(currentFilter instanceof IConfigurableFilter) && !(currentFilter instanceof IChecklistFilter)) {
				Minecraft.getMinecraft().displayGuiScreen(parentGui.getParentGui());
			}
			return true;
		}
		return false;
	}

	@Override
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY, float partialTicks) {
		super.drawBackground(parentScreen, mouseX, mouseY, partialTicks);

		if(isInside(mouseX, mouseY)) {
			drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX() + getWidth(), getAbsoluteY() + getHeight(), 0xAAFFFFFF);
		}

		if(currentFilter != null) {
			IFilterIcon filterIcon = currentFilter.getFilterIcon();
			if(filterIcon != null) {
				GlStateManager.color(1f, 1f, 1f, 1f);
				filterIcon.draw(getAbsoluteX() + 2, getAbsoluteY() + getHeight() / 2 - 12, 24, 24, zLevel);
			}
			drawString(parentScreen.getFontRenderer(), I18n.format(currentFilter.getLangKey()), getAbsoluteX() + 32, getAbsoluteY() + getHeight() / 2 - parentScreen.getFontRenderer().FONT_HEIGHT / 2, 0xFFFFFF);
		}
	}

	public void setCurrentFilter(@Nullable IFilter currentFilter) {
		this.currentFilter = currentFilter;
	}

	@Override
	public void addTooltip(List<String> list) {
		if(currentFilter != null) {
			Collections.addAll(list, I18n.format(currentFilter.getDescriptionLangKey()).split("\\\\n"));
		}
	}

}
