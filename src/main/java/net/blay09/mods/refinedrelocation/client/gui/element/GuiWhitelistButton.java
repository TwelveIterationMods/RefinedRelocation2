package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.GuiRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiWhitelistButton extends GuiImageButton {

	private final GuiRootFilter parentGui;
	private final GuiFilterSlot parentSlot;

	private boolean lastBlacklist;

	public GuiWhitelistButton(int x, int y, GuiRootFilter parentGui, GuiFilterSlot parentSlot) {
		super(x, y, "filter_whitelist");
		this.parentGui = parentGui;
		this.parentSlot = parentSlot;
		setSize(8, 8);
		setVisible(false);
	}

	@Override
	public void update() {
		super.update();

		boolean nowBlacklist = parentGui.getContainer().getRootFilter().isBlacklist(parentSlot.getFilterIndex());
		if (lastBlacklist != nowBlacklist) {
			setButtonTexture(nowBlacklist ? "filter_blacklist" : "filter_whitelist");
			lastBlacklist = nowBlacklist;
		}

		setVisible(parentSlot.hasFilter());
	}

	@Override
	public void actionPerformed(int mouseButton) {
		boolean isBlacklist = !parentGui.getContainer().getRootFilter().isBlacklist(parentSlot.getFilterIndex());
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setInteger(ContainerRootFilter.KEY_BLACKLIST_INDEX, parentSlot.getFilterIndex());
		tagCompound.setBoolean(ContainerRootFilter.KEY_BLACKLIST, isBlacklist);
		RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_BLACKLIST, tagCompound);
		parentGui.getContainer().getRootFilter().setIsBlacklist(parentSlot.getFilterIndex(), isBlacklist);
	}

	@Override
	public void addTooltip(List<String> list) {
		boolean nowBlacklist = parentGui.getContainer().getRootFilter().isBlacklist(parentSlot.getFilterIndex());
		list.add(TextFormatting.WHITE + (nowBlacklist ? I18n.format("gui.refinedrelocation:root_filter.blacklist") : I18n.format("gui.refinedrelocation:root_filter.whitelist")));
		list.add(TextFormatting.YELLOW + I18n.format("gui.refinedrelocation:root_filter.click_to_toggle"));
	}

}
