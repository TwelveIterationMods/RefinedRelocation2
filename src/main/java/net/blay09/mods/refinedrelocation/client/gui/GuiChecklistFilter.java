package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.minecraft.inventory.Container;

public class GuiChecklistFilter<T extends Container> extends GuiContainerMod<T> implements IScrollTarget {

	private final IChecklistFilter filter;

	private int currentOffset;

	public GuiChecklistFilter(T container, IChecklistFilter filter) {
		super(container);
		this.filter = filter;

		ySize = 210;

		GuiScrollBar scrollBar = new GuiScrollBar(xSize - 16, 28, 78, this);
		rootNode.addChild(scrollBar);

		GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, 8, 28, 152, 80);
		rootNode.addChild(scrollPane);

		setCurrentOffset(0);
	}

	@Override
	public int getVisibleRows() {
		return 7;
	}

	@Override
	public int getRowCount() {
		return filter.getOptionCount();
	}

	@Override
	public int getCurrentOffset() {
		return currentOffset;
	}

	@Override
	public void setCurrentOffset(int offset) {
		this.currentOffset = offset;
	}
}
