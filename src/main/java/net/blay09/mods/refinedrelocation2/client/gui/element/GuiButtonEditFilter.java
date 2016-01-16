package net.blay09.mods.refinedrelocation2.client.gui.element;

import net.blay09.mods.refinedrelocation2.balyware.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation2.client.gui.GuiRefinedRelocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonEditFilter extends GuiButton {

    private final TextureAtlasRegion background;

    public GuiButtonEditFilter(int id, int x, int y) {
        super(id, x, y, "");
        background = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:edit_filter_button");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(visible) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int hoverState = getHoverState(hovered);
            background.draw(xPosition, yPosition, zLevel);
        }
    }

}
