package net.blay09.mods.refinedrelocation.client.gui.base;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Mouse;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.IOException;
import java.util.List;

public abstract class GuiContainerMod<T extends Container> extends GuiContainer {

	protected final T container;
	protected final GuiElement rootNode = new GuiElement();
	private final List<String> tooltip = Lists.newArrayList();

	public GuiContainerMod(T container) {
		super(container);
		this.container = container;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initGui() {
		super.initGui();
		rootNode.removeAllChildren();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		rootNode.getElementAt(mouseX, mouseY).mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if(delta != 0) {
			int mouseX = Mouse.getEventX() * width / mc.displayWidth;
			int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			rootNode.getElementAt(mouseX, mouseY).mouseWheelMoved(mouseX, mouseY, delta);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		rootNode.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void updateScreen() {
		super.updateScreen();
		rootNode.update();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		rootNode.drawBackground(mc, mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.color(1f, 1f, 1f, 1f);
		rootNode.drawForeground(mc, mouseX, mouseY);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();

		tooltip.clear();
		GuiElement tooltipElement = rootNode.getElementAt(mouseX, mouseY);
		tooltipElement.addTooltip(tooltip);
		if(!tooltip.isEmpty()) {
			drawHoveringText(tooltip, mouseX, mouseY);
		}
	}

	public T getContainer() {
		return container;
	}

}
