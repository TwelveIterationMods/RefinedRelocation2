package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTextFieldMultiLine extends GuiElement implements IScrollTarget {

	private static final int ENABLED_COLOR = 0xE0E0E0;
	private static final int DISABLED_COLOR = 0x707070;
	private static final int PADDING = 2;

	private FontRenderer fontRenderer;
	private String text = "";
	private int maxLength = Integer.MAX_VALUE;
	private boolean isEnabled = true;
	private boolean visible = true;

	private boolean canLoseFocus = true;
	private boolean isFocused;
	private int cursorPosition;
	private int cursorCounter;
	private int scrollOffset;
	private int lineScrollOffset;

	private String[] renderCache;
	private int lastRowCount;

	public GuiTextFieldMultiLine(int x, int y, int width, int height) {
		fontRenderer = Minecraft.getMinecraft().fontRenderer;
		setPosition(x, y);
		setSize(width, height);
	}

	@Override
	public void initGui(IParentScreen parentScreen) {
		super.initGui(parentScreen);
		fontRenderer = parentScreen.getFontRenderer();
	}

	private int getStartOfLine(int position, int iterations) {
		int startOfLine = position;
		for (int i = 0; i < iterations; i++) {
			startOfLine = text.lastIndexOf('\n', startOfLine - 1);
		}
		return startOfLine != -1 ? startOfLine + 1 : 0;
	}

	private int getEndOfLine(int position, int iteration) {
		int endOfLine = position - 1;
		for (int i = 0; i < iteration; i++) {
			endOfLine = text.indexOf('\n', endOfLine + 1);
			if (endOfLine == -1) {
				return text.length();
			}
		}
		return endOfLine != -1 ? endOfLine : text.length();
	}

	private int getLineLength(int position) {
		return getEndOfLine(position, 1) - getStartOfLine(position, 1);
	}

	private int getStartOfWord(int position) {
		if(text.isEmpty()) {
			return 0;
		}
		position = Math.max(Math.min(position, text.length() - 1), 0);
		if (text.charAt(position) == '\n') {
			return position;
		}
		boolean foundAlphabetic = false;
		for (int i = position; i >= 0; i--) {
			char c = text.charAt(i);
			if (c == '\n') {
				return i + 1;
			}
			if (Character.isAlphabetic(c)) {
				foundAlphabetic = true;
			} else if (foundAlphabetic) {
				return i + 1;
			}
		}
		return 0;
	}

	private int getStartOfNextWord(int position) {
		position = Math.max(Math.min(position, text.length() - 1), 0);
		if (text.charAt(position) == '\n') {
			return position;
		}
		boolean foundNonAlphabetic = false;
		for (int i = position; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				return i;
			}
			if (!Character.isAlphabetic(c)) {
				foundNonAlphabetic = true;
			} else if (foundNonAlphabetic) {
				return i;
			}
		}
		return text.length();
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = Math.min(Math.max(cursorPosition, 0), text.length());

		int cursorLine = 0;
		for (int i = 0; i < this.cursorPosition; i++) {
			if (text.charAt(i) == '\n') {
				cursorLine++;
			}
		}

		int innerHeight = getHeight() - PADDING;
		int cursorRenderY = (cursorLine - scrollOffset) * fontRenderer.FONT_HEIGHT + PADDING;
		if (cursorRenderY < 0) {
			scroll(0, cursorRenderY / fontRenderer.FONT_HEIGHT - 1);
		} else if (cursorRenderY > innerHeight - fontRenderer.FONT_HEIGHT) {
			scroll(0, (cursorRenderY - innerHeight) / fontRenderer.FONT_HEIGHT + 1);
		}

		int innerWidth = getWidth() - PADDING;
		int startOfLine = getStartOfLine(cursorPosition, 1);
		int endOfLine = getEndOfLine(cursorPosition, 1);
		int cursorPositionX = Math.min(getLineLength(cursorPosition), cursorPosition - startOfLine);
		String lineText = text.substring(startOfLine, endOfLine);
		lineScrollOffset = Math.max(Math.min(lineScrollOffset, lineText.length()), 0);
		String renderText = fontRenderer.trimStringToWidth(lineText, innerWidth);
		if (cursorPositionX == lineScrollOffset) {
			lineScrollOffset -= renderText.length();
		}
		lineScrollOffset = Math.max(Math.min(lineScrollOffset, lineText.length()), 0);
		int offset = renderText.length() + lineScrollOffset;
		if (cursorPositionX > offset) {
			lineScrollOffset += cursorPositionX - offset;
		} else if (cursorPositionX <= lineScrollOffset) {
			lineScrollOffset -= lineScrollOffset - cursorPositionX;
		}
		lineScrollOffset = Math.max(Math.min(lineScrollOffset, lineText.length()), 0);
	}

	public void scroll(int x, int y) {
		lineScrollOffset = Math.max(lineScrollOffset + x, 0);
		scrollOffset = Math.max(scrollOffset + y, 0);
	}

	private void deleteBack(boolean wholeWord) {
		int deleteCount = 1;
		if (wholeWord) {
			deleteCount = cursorPosition - getStartOfWord(cursorPosition);
		}
		if (cursorPosition > 0) {
			text = text.substring(0, cursorPosition - deleteCount) + text.substring(cursorPosition);
			setCursorPosition(cursorPosition - deleteCount);
			renderCache = null;
		}
	}

	private void deleteFront(boolean wholeWord) {
		int deleteCount = 1;
		if (wholeWord) {
			deleteCount = getStartOfNextWord(cursorPosition) - cursorPosition;
		}
		if (cursorPosition < text.length()) {
			text = text.substring(0, cursorPosition) + text.substring(cursorPosition + deleteCount);
			renderCache = null;
		}
	}

	private void writeText(String s) {
		text = (cursorPosition > 0 ? text.substring(0, cursorPosition) : "") + s + (text.length() > cursorPosition ? text.substring(cursorPosition) : "");
		setCursorPosition(cursorPosition + s.length());
		renderCache = null;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean isInside = isInside(mouseX, mouseY);

		if (canLoseFocus) {
			setFocused(isInside);
		}

		if (isFocused && isInside && mouseButton == 0) {
			int relX = mouseX - getAbsoluteX();
			int relY = mouseY - getAbsoluteY() - fontRenderer.FONT_HEIGHT + 5;
			int lineNumber = Math.round((float) relY / (float) fontRenderer.FONT_HEIGHT) + scrollOffset + 1;
			int startOfLine = getStartOfLine(getEndOfLine(0, lineNumber), 1);
			int endOfLine = getEndOfLine(startOfLine, 1);
			if(startOfLine == endOfLine) {
				setCursorPosition(startOfLine);
			} else {
				String renderText = fontRenderer.trimStringToWidth(this.text.substring(Math.max(startOfLine + lineScrollOffset, 0), Math.max(0, endOfLine)), getWidth() - PADDING);
			  	setCursorPosition(startOfLine + fontRenderer.trimStringToWidth(renderText, relX).length() + lineScrollOffset);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char typedChar, int keyCode) {
		if (!isFocused) {
			return false;
		}
		switch (keyCode) {
			case Keyboard.KEY_END:
				if (GuiScreen.isCtrlKeyDown()) {
					setCursorPosition(text.length());
				} else {
					setCursorPosition(getEndOfLine(cursorPosition, 1));
				}
				return true;
			case Keyboard.KEY_HOME:
				if (GuiScreen.isCtrlKeyDown()) {
					setCursorPosition(0);
				} else {
					setCursorPosition(getStartOfLine(cursorPosition, 1));
				}
				return true;
			case Keyboard.KEY_LEFT:
				if (GuiScreen.isCtrlKeyDown()) {
					setCursorPosition(getStartOfWord(cursorPosition - 1));
				} else {
					setCursorPosition(cursorPosition - 1);
				}
				return true;
			case Keyboard.KEY_RIGHT:
				if (GuiScreen.isCtrlKeyDown()) {
					setCursorPosition(getStartOfNextWord(cursorPosition + 1));
				} else {
					setCursorPosition(cursorPosition + 1);
				}
				return true;
			case Keyboard.KEY_UP:
				if (GuiScreen.isCtrlKeyDown()) {
					scroll(0, -1);
				} else {
					int upLine = getStartOfLine(cursorPosition, 2);
					setCursorPosition(upLine + Math.min(getLineLength(upLine), (cursorPosition - getStartOfLine(cursorPosition, 1))));
				}
				return true;
			case Keyboard.KEY_DOWN:
				if (GuiScreen.isCtrlKeyDown()) {
					scroll(0, 1);
				} else {
					int downLine = getEndOfLine(cursorPosition, 2);
					setCursorPosition(getStartOfLine(downLine, 1) + Math.min(getLineLength(downLine), (cursorPosition - getStartOfLine(cursorPosition, 1))));
				}
				return true;
			case Keyboard.KEY_RETURN:
				if (isEnabled) {
					writeText("\n");
				}
				return true;
			case Keyboard.KEY_DELETE:
				if (isEnabled) {
					deleteFront(GuiScreen.isCtrlKeyDown());
				}
				return true;
			case Keyboard.KEY_BACK:
				if (isEnabled) {
					deleteBack(GuiScreen.isCtrlKeyDown());
				}
				return true;
			default:
				if (isEnabled) {
					if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
						writeText(Character.toString(typedChar));
						return true;
					}
				}
		}
		return false;
	}

	@Override
	public void update() {
		super.update();
		cursorCounter++;
	}

	@Override
	public void drawForeground(IParentScreen parentScreen, int mouseX, int mouseY) {
		super.drawForeground(parentScreen, mouseX, mouseY);
		if (visible) {
			drawRect(getAbsoluteX() - 1, getAbsoluteY() - 1, getAbsoluteX() + getWidth() + 1, getAbsoluteY() + getHeight() + 1, 0xFFEEEEEE);
			drawRect(getAbsoluteX(), getAbsoluteY(), getAbsoluteX() + getWidth(), getAbsoluteY() + getHeight(), 0xFF000000);

			if (renderCache == null) {
				renderCache = text.split("\n");
			}

			for (int i = scrollOffset; i < renderCache.length; i++) {
				int y = (i - scrollOffset) * fontRenderer.FONT_HEIGHT;
				if (y + fontRenderer.FONT_HEIGHT >= getHeight()) {
					break;
				}
				if (lineScrollOffset >= renderCache[i].length()) {
					continue;
				}
				String renderText = fontRenderer.trimStringToWidth(renderCache[i].substring(lineScrollOffset), getWidth() - PADDING);
				fontRenderer.drawString(renderText, getAbsoluteX() + PADDING, getAbsoluteY() + PADDING + y, isEnabled ? ENABLED_COLOR : DISABLED_COLOR, true);
			}

			int cursorLine = 0;
			int lastLineIdx = 0;
			for (int i = 0; i < cursorPosition; i++) {
				if (text.charAt(i) == '\n') {
					cursorLine++;
					lastLineIdx = i + 1;
				}
			}
			if (cursorCounter / 6 % 2 == 0 && (cursorLine - scrollOffset) * fontRenderer.FONT_HEIGHT >= 0 && (cursorLine - scrollOffset + 1) * fontRenderer.FONT_HEIGHT < getHeight() + PADDING) {
				drawCursor(getAbsoluteX() + fontRenderer.getStringWidth(text.substring(lastLineIdx + lineScrollOffset, cursorPosition)) + PADDING, getAbsoluteY() + (cursorLine - scrollOffset) * fontRenderer.FONT_HEIGHT + PADDING);
			}
		}
	}

	private void drawCursor(int x, int y) {
		Tessellator tessellator = Tessellator.getInstance();
		GlStateManager.color(0f, 0f, 1f, 1f);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		tessellator.getBuffer().pos(x, y + fontRenderer.FONT_HEIGHT, 0).endVertex();
		tessellator.getBuffer().pos(x + 1, y + fontRenderer.FONT_HEIGHT, 0).endVertex();
		tessellator.getBuffer().pos(x + 1, y, 0).endVertex();
		tessellator.getBuffer().pos(x, y, 0).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	public boolean isCanLoseFocus() {
		return canLoseFocus;
	}

	public void setCanLoseFocus(boolean canLoseFocus) {
		this.canLoseFocus = canLoseFocus;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean isFocused) {
		if (isFocused && !this.isFocused) {
			cursorCounter = 0;
		}
		this.isFocused = isFocused;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text.length() > maxLength) {
			this.text = text.substring(0, maxLength);
		} else {
			this.text = text;
		}
		setCursorPosition(cursorPosition);
		renderCache = null;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public int getVisibleRows() {
		return getHeight() / fontRenderer.FONT_HEIGHT;
	}

	@Override
	public int getRowCount() {
		if(renderCache != null) {
			lastRowCount = renderCache.length;
		}
		return lastRowCount;
	}

	@Override
	public int getCurrentOffset() {
		return scrollOffset;
	}

	@Override
	public void setCurrentOffset(int offset) {
		this.scrollOffset = offset;
	}

}
