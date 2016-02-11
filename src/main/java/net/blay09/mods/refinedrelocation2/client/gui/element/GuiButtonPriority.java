package net.blay09.mods.refinedrelocation2.client.gui.element;

import net.blay09.mods.refinedrelocation2.api.grid.Priority;
import net.blay09.mods.refinedrelocation2.balyware.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation2.client.gui.GuiRefinedRelocation;
import net.blay09.mods.refinedrelocation2.container.handler.IPriorityHandler;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.blay09.mods.refinedrelocation2.network.container.MessageContainerInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonPriority extends GuiButton implements IButtonHandler {

    private final TextureAtlasRegion background;
    private final TextureAtlasRegion backgroundHover;
    private final TextureAtlasRegion backgroundDisabled;

    private final IPriorityHandler handler;
    private int lastPriority;

    public GuiButtonPriority(int id, int x, int y, IPriorityHandler handler) {
        super(id, x, y, "0");
        this.handler = handler;
        background = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:small_button");
        backgroundHover = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:small_button_hover");
        backgroundDisabled = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:small_button_disabled");
        width = background.getIconWidth();
        height = background.getIconHeight();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            if(handler.getPriority() != lastPriority) {
                displayString = Priority.fromValue(handler.getPriority()).getButtonString();
                lastPriority = handler.getPriority();
            }
            GlStateManager.color(1f, 1f, 1f, 1f);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int hoverState = getHoverState(hovered);
            switch (hoverState) {
                case 0:
                    backgroundDisabled.draw(xPosition, yPosition, zLevel);
                    break;
                case 1:
                    background.draw(xPosition, yPosition, zLevel);
                    break;
                case 2:
                    backgroundHover.draw(xPosition, yPosition, zLevel);
                    break;
            }
            int color = 14737632;
            if (packedFGColour != 0) {
                color = packedFGColour;
            } else if (!this.enabled) {
                color = 10526880;
            } else if (this.hovered) {
                color = 16777120;
            }
            drawCenteredString(mc.fontRendererObj, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, color);
        }
    }

    @Override
    public void handleClick(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            handler.setPriority(handler.getPriority() + 1);
        } else if(mouseButton == 1) {
            handler.setPriority(handler.getPriority() - 1);
        }
    }

}
