package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextButton;
import net.blay09.mods.refinedrelocation.container.ContainerBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.minecraft.client.resources.I18n;

public class GuiButtonBlockExtenderFilter extends GuiTextButton {

	private final TileBlockExtender blockExtender;
	private final boolean isOutputFilter;

	public GuiButtonBlockExtenderFilter(int x, int y, int width, int height, TileBlockExtender blockExtender, boolean isOutputFilter) {
		super(x, y, width, height, I18n.format("gui.refinedrelocation:block_extender.configure"));
		this.blockExtender = blockExtender;
		this.isOutputFilter = isOutputFilter;
	}

	@Override
	public void actionPerformed(int mouseButton) {
		RefinedRelocationAPI.sendContainerMessageToServer(ContainerBlockExtender.KEY_CONFIGURE_FILTER, isOutputFilter ? 1 : 0);
	}

}
