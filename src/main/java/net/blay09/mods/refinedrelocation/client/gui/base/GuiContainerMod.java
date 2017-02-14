package net.blay09.mods.refinedrelocation.client.gui.base;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiRootNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.IOException;
import java.util.List;

public abstract class GuiContainerMod<T extends Container> extends GuiContainer implements IParentScreen {

	protected final T container;
	protected final GuiRootNode rootNode = new GuiRootNode(this);
	private final List<String> tooltip = Lists.newArrayList();

	private GuiElement mouseElement;
	protected boolean shouldKeyRepeat;

	public GuiContainerMod(T container) {
		super(container);
		this.container = container;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void initGui() {
		super.initGui();
		rootNode.setPosition(guiLeft, guiTop);
		rootNode.setSize(xSize, ySize);
		rootNode.initGui(this);

		if (shouldKeyRepeat) {
			Keyboard.enableRepeatEvents(true);
		}
	}

	public boolean onGuiAboutToClose() {
		return true;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if (shouldKeyRepeat) {
			Keyboard.enableRepeatEvents(false);
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if (delta != 0) {
			int mouseX = Mouse.getEventX() * width / mc.displayWidth;
			int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			rootNode.getElementAt(mouseX, mouseY).mouseWheelMoved(mouseX, mouseY, delta);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		// TODO Workaround for JEI disabling my repeat events
		if(shouldKeyRepeat) {
			Keyboard.enableRepeatEvents(true);
		}

		if (!rootNode.getElementAt(mouseX, mouseY).mouseClicked(mouseX, mouseY, mouseButton)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		rootNode.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!rootNode.keyTyped(typedChar, keyCode)) {
			if (keyCode == Keyboard.KEY_ESCAPE || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
				if(onGuiAboutToClose()) {
					mc.player.closeScreen();
				}
				return;
			}
			super.keyTyped(typedChar, keyCode);
		}
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
		rootNode.drawBackground(this, mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mouseElement = rootNode.getElementAt(mouseX, mouseY);

		super.drawScreen(mouseX, mouseY, partialTicks);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.color(1f, 1f, 1f, 1f);
		rootNode.drawForeground(this, mouseX, mouseY);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();

		tooltip.clear();
		GuiElement tooltipElement = rootNode.getElementAt(mouseX, mouseY);
		tooltipElement.addTooltip(tooltip);
		if (!tooltip.isEmpty()) {
			drawHoveringText(tooltip, mouseX, mouseY);
		}
	}

	public T getContainer() {
		return container;
	}

	@Override
	public GuiElement getMouseElement() {
		return mouseElement;
	}

	@Override
	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	@Override
	public Minecraft getMinecraft() {
		return mc;
	}

	@Override
	public int getLeft() {
		return guiLeft;
	}

	@Override
	public int getTop() {
		return guiTop;
	}

	@Override
	public int getWidth() {
		return xSize;
	}

	@Override
	public int getHeight() {
		return ySize;
	}

}
