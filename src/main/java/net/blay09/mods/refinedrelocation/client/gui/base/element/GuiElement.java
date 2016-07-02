package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.gui.Gui;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;

public class GuiElement extends Gui {

	private final List<GuiElement> children = Lists.newArrayList();
	private GuiElement parent;
	private int relX;
	private int relY;
	private int absX;
	private int absY;
	private int width;
	private int height;
	private boolean isDirty;
	private boolean visible = true;

	public GuiElement() {}

	public GuiElement getParent() {
		return parent;
	}

	public void addChild(GuiElement element) {
		children.add(element);
		element.parent = this;
	}

	public void setPosition(int x, int y) {
		this.relX = x;
		this.relY = y;
		markDirty();
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getAbsoluteX() {
		if(isDirty) {
			updatePosition();
		}
		return absX;
	}

	public int getAbsoluteY() {
		if(isDirty) {
			updatePosition();
		}
		return absY;
	}

	public void updatePosition() {
		if(parent != null) {
			absX = parent.getAbsoluteX() + relX;
			absY = parent.getAbsoluteY() + relY;
		} else {
			absX = relX;
			absY = relY;
		}
		isDirty = false;

		for(GuiElement child : children) {
			child.markDirty();
		}
	}

	public void markDirty() {
		isDirty = true;
		for(GuiElement element : children) {
			element.markDirty();
		}
	}

	public void initGui(IParentScreen parentScreen) {
		for(GuiElement child : children) {
			child.initGui(parentScreen);
		}
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return parent != null && parent.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		for(GuiElement child : children) {
			child.mouseReleased(mouseX, mouseY, state);
		}
	}

	public boolean keyTyped(char typedChar, int keyCode) {
		for(GuiElement child : children) {
			if(child.keyTyped(typedChar, keyCode)) {
				return true;
			}
		}
		return false;
	}

	public void mouseWheelMoved(int mouseX, int mouseY, int delta) {
		if(parent != null) {
			parent.mouseWheelMoved(mouseX, mouseY, delta);
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void update() {
		for(GuiElement child : children) {
			child.update();
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY) {
		for(GuiElement child : children) {
			child.drawBackground(parentScreen, mouseX, mouseY);
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void drawForeground(IParentScreen parentScreen, int mouseX, int mouseY) {
		for(GuiElement child : children) {
			child.drawForeground(parentScreen, mouseX, mouseY);
		}
	}

	public boolean isInside(int x, int y) {
		if(!visible) {
			return false;
		}
		if(isDirty) {
			updatePosition();
		}
		if(x >= absX && x < absX + width) {
			if(y >= absY && y < absY + height) {
				return true;
			}
		}
		return false;
	}

	public GuiElement getElementAt(int x, int y) {
		GuiElement foundChild = getChildAt(x, y);
		if(foundChild != null) {
			return foundChild.getElementAt(x, y);
		}
		return this;
	}

	@Nullable
	private GuiElement getChildAt(int x, int y) {
		if(!visible) {
			return null;
		}
		for(int i = children.size() - 1; i >= 0; i--) {
			GuiElement child = children.get(i);
			if(child.visible && child.isInside(x, y)) {
				return child;
			}
		}
		return null;
	}

	public void removeAllChildren() {
		for(GuiElement child : children) {
			child.parent = null;
		}
		children.clear();
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void addTooltip(List<String> list) {
		if(parent != null) {
			parent.addTooltip(list);
		}
	}

	@Nullable
	public IParentScreen getParentScreen() {
		return parent != null ? parent.getParentScreen() : null;
	}

	public int getRelativeX() {
		return relX;
	}

	public int getRelativeY() {
		return relY;
	}
}
