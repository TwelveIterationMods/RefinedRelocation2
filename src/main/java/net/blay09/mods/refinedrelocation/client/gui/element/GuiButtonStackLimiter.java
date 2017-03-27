package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextButton;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

public class GuiButtonStackLimiter extends GuiTextButton {

	private static final int[] PRESETS = new int[] { 1, 2, 4, 8, 16, 32, 48, 64 };

	private final TileBlockExtender blockExtender;

	public GuiButtonStackLimiter(int x, int y, int width, int height, TileBlockExtender blockExtender) {
		super(x, y, width, height, "");
		this.blockExtender = blockExtender;
	}

	@Override
	public void actionPerformed(int mouseButton) {
		int limit = blockExtender.getStackLimiterLimit();
		int index = Arrays.binarySearch(PRESETS, limit);
		if(mouseButton == 0) {
			if(index < 0) {
				index = Math.abs(index) - 1;
			} else {
				index++;
			}
		} else if(mouseButton == 1) {
			if(index < 0) {
				index = Math.abs(index) - 2;
			} else {
				index--;
			}
		}
		index = MathHelper.clamp(index, 0, PRESETS.length - 1);
		blockExtender.setStackLimiterLimit(PRESETS[index]);
	}

	@Override
	public void mouseWheelMoved(int mouseX, int mouseY, int delta) {
		int limit = blockExtender.getStackLimiterLimit();
		if(delta > 0) {
			limit++;
		} else if(delta < 0) {
			limit--;
		}
		limit = MathHelper.clamp(limit, 1, 64);
		blockExtender.setStackLimiterLimit(limit);
	}

	@Override
	public void update() {
		super.update();

		text = String.valueOf(blockExtender.getStackLimiterLimit());
	}

	@Override
	public void addTooltip(List<String> list) {
		list.add(I18n.format("gui.refinedrelocation:block_extender.stack_limiter"));
		list.add(TextFormatting.GREEN + I18n.format("gui.refinedrelocation:block_extender.stack_limiter_increase"));
		list.add(TextFormatting.RED + I18n.format("gui.refinedrelocation:block_extender.stack_limiter_decrease"));
	}

}
