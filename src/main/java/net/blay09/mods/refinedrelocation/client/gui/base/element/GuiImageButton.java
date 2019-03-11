package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.GuiTextureSpriteButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiImageButton extends GuiButton {

    private GuiTextureSpriteButton texture;

    public GuiImageButton(int buttonId, int x, int y, int width, int height, GuiTextureSpriteButton texture) {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            texture.bind();
            if (!enabled) {
                texture.asDisabled().draw(x, y, width, height, zLevel);
            } else {
                if (hovered) {
                    texture.asHovered().draw(x, y, width, height, zLevel);
                } else {
                    texture.draw(x, y, width, height, zLevel);
                }
            }
        }
    }

    public void setTexture(GuiTextureSpriteButton texture) {
        this.texture = texture;
    }
}
