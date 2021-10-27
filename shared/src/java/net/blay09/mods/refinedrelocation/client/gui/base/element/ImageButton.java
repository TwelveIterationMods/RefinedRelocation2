package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiTextureSpriteButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class ImageButton extends Button {

    private GuiTextureSpriteButton texture;

    public ImageButton(int x, int y, int width, int height, GuiTextureSpriteButton texture, OnPress onPress) {
        super(x, y, width, height, new TextComponent(""), onPress);
        this.texture = texture;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            texture.bind();
            if (!active) {
                texture.asDisabled().draw(poseStack, x, y, width, height, getBlitOffset());
            } else {
                if (isHovered) {
                    texture.asHovered().draw(poseStack, x, y, width, height, getBlitOffset());
                } else {
                    texture.draw(poseStack, x, y, width, height, getBlitOffset());
                }
            }
        }
    }

    public void setTexture(GuiTextureSpriteButton texture) {
        this.texture = texture;
    }
}
