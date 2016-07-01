package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiWhitelistButton extends GuiImageButton {

	private final int filterIndex;

	public GuiWhitelistButton(int x, int y, int filterIndex) {
		super(x, y, "filter_whitelist");
		this.filterIndex = filterIndex;
	}

	@Override
	public void actionPerformed() {
		super.actionPerformed();

		// TODO whitelist/blacklist toggle
	}

	@Override
	public void addTooltip(List<String> list) {
		list.add(TextFormatting.WHITE + "Whitelist");
	}

}
