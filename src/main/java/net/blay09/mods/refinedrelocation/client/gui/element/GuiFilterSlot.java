package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.gui.GuiAddFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiFilterSlot extends GuiElement {

	private final GuiRootFilter parentGui;
	private final TextureAtlasRegion texture;
	private final IRootFilter rootFilter;
	private final int index;

	public GuiFilterSlot(GuiRootFilter parentGui, IRootFilter rootFilter, int index) {
		this.parentGui = parentGui;
		this.rootFilter = rootFilter;
		this.index = index;
		texture = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_slot");
		setSize(texture.getIconWidth(), texture.getIconHeight());
	}

	@Override
	public void drawBackground(Minecraft mc, int mouseX, int mouseY) {
		super.drawBackground(mc, mouseX, mouseY);
		texture.draw(getAbsoluteX(), getAbsoluteY(), zLevel);
	}

	@Override
	public void drawForeground(Minecraft mc, int mouseX, int mouseY) {
		super.drawForeground(mc, mouseX, mouseY);
		IFilter filter = rootFilter.getFilter(index);
		if(filter != null) {
			IFilterIcon filterIcon = filter.getFilterIcon();
			if(filterIcon != null) {
				filterIcon.draw(getAbsoluteX(), getAbsoluteY(), zLevel);
			}
		}
		if(isInside(mouseX, mouseY)) {
			drawRect(getAbsoluteX() + 1, getAbsoluteY() + 1, getAbsoluteX() + getWidth() - 1, getAbsoluteY() + getHeight() - 1, 0x99FFFFFF);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		IFilter filter = rootFilter.getFilter(index);
		if(filter == null) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiAddFilter(parentGui));
		} else {
			RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_EDIT_FILTER, index);
		}
	}

	@Override
	public void addTooltip(List<String> list) {
		IFilter filter = rootFilter.getFilter(index);
		if(filter == null) {
			list.add(TextFormatting.GRAY + "No filter set");
			list.add(TextFormatting.YELLOW + "Click to add filter");
		} else {
			list.add(I18n.format(filter.getLangKey()));
			list.add(TextFormatting.YELLOW + "Click to configure");
		}
	}

}
