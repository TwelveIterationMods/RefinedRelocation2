package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiDeleteFilterButton extends GuiImageButton {

	private final GuiFilterSlot parentSlot;

	public GuiDeleteFilterButton(int x, int y, GuiFilterSlot parentSlot) {
		super(x, y, "filter_delete");
		this.parentSlot = parentSlot;
		setSize(8, 8);
		setVisible(false);
	}

	@Override
	public void update() {
		super.update();

		setVisible(parentSlot.hasFilter());
	}

	@Override
	public void actionPerformed() {
		super.actionPerformed();

		RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_DELETE_FILTER, parentSlot.getFilterIndex());
	}

	@Override
	public void addTooltip(List<String> list) {
		list.add(TextFormatting.RED + "Delete Filter");
	}

}
