package net.blay09.mods.refinedrelocation.client.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public interface IParentScreen {

	Minecraft getMinecraft();
	FontRenderer getFontRenderer();

	int getLeft();
	int getTop();
	int getWidth();
	int getHeight();

}
