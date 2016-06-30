package net.blay09.mods.refinedrelocation.client.gui.base;

import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class GuiScreenMod extends GuiScreen {

	protected final GuiElement rootNode = new GuiElement();

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initGui() {
		super.initGui();
		rootNode.removeAllChildren();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void updateScreen() {
		super.updateScreen();
		rootNode.update();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		rootNode.drawBackground(mc, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		rootNode.drawForeground(mc, mouseX, mouseY);
	}

}
