package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.Priority;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextButton;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiButtonPriority extends GuiTextButton {

	private final Priority.Enum[] values;
	private int currentIndex;

	public GuiButtonPriority(int x, int y, int width, int height) {
		super(x, y, width, height, "");
		values = Priority.Enum.values();
		currentIndex = 2;
		text = I18n.format(values[currentIndex].getLangKey());
	}

	@Override
	public void actionPerformed(int mouseButton) {
		if(mouseButton == 0) {
			currentIndex++;
		} else if(mouseButton == 1) {
			currentIndex--;
		}
		currentIndex = Math.max(0, Math.min(values.length - 1, currentIndex));
		text = I18n.format(values[currentIndex].getLangKey());

		RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_PRIORITY, values[currentIndex].getPriority());
	}

	@Override
	public void addTooltip(List<String> list) {
		list.add(TextFormatting.GREEN + "Left-click to increase");
		list.add(TextFormatting.RED + "Right-click to decrease");
		list.add("Priority: " + values[currentIndex].getPriority());
	}

}
