package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiTextureSpriteButton;
import net.minecraft.client.gui.widget.button.Button;

public class GuiImageButton extends Button {

    private GuiTextureSpriteButton texture;

    public GuiImageButton(int x, int y, int width, int height, GuiTextureSpriteButton texture, IPressable pressable) {
        super(x, y, width, height, "", pressable);
        this.texture = texture;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            texture.bind();
            if (!active) {
                texture.asDisabled().draw(x, y, width, height, blitOffset);
            } else {
                if (isHovered) {
                    texture.asHovered().draw(x, y, width, height, blitOffset);
                } else {
                    texture.draw(x, y, width, height, blitOffset);
                }
            }
        }
    }

    public void setTexture(GuiTextureSpriteButton texture) {
        this.texture = texture;
    }
}
