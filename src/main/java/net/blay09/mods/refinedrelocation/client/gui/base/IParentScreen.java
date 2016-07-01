package net.blay09.mods.refinedrelocation.client.gui.base;

import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public interface IParentScreen {

	GuiElement getMouseElement();
	Minecraft getMinecraft();
	FontRenderer getFontRenderer();

}
