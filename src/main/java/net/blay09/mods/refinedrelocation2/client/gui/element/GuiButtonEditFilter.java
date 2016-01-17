package net.blay09.mods.refinedrelocation2.client.gui.element;

import net.blay09.mods.refinedrelocation2.balyware.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation2.client.gui.GuiRefinedRelocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonEditFilter extends GuiButton {

    private final TextureAtlasRegion background;
    private final TextureAtlasRegion backgroundHover;
    private final TextureAtlasRegion backgroundDisabled;

    public GuiButtonEditFilter(int id, int x, int y, boolean smallVersion) {
        super(id, x, y, "");
        if(smallVersion) {
            background = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:small_filter_button");
            backgroundHover = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:small_filter_button_hover");
            backgroundDisabled = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:small_filter_button_disabled");
        } else {
            background = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:filter_button");
            backgroundHover = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:filter_button_hover");
            backgroundDisabled = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:filter_button_disabled");
        }
        width = background.getIconWidth();
        height = background.getIconHeight();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(visible) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int hoverState = getHoverState(hovered);
            switch(hoverState) {
                case 0: backgroundDisabled.draw(xPosition, yPosition, zLevel); break;
                case 1: background.draw(xPosition, yPosition, zLevel); break;
                case 2: backgroundHover.draw(xPosition, yPosition, zLevel); break;
            }
        }
    }

}
