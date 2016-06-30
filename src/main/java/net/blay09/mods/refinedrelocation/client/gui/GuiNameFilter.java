package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextFieldMultiLine;
import net.blay09.mods.refinedrelocation.container.ContainerNameFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;

public class GuiNameFilter extends GuiContainerMod<ContainerNameFilter> {

	private GuiTextFieldMultiLine txtFilter;

	public GuiNameFilter(NameFilter filter) {
		super(new ContainerNameFilter(filter));
	}

	@Override
	public void initGui() {
		super.initGui();

		txtFilter = new GuiTextFieldMultiLine(fontRendererObj, 0, 0, 100, 100);
		txtFilter.setFocused(true);
		txtFilter.setCanLoseFocus(false);
		rootNode.addChild(txtFilter);
	}

}
