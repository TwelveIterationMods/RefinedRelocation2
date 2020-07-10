package net.blay09.mods.refinedrelocation.client.gui.base;

import com.mojang.blaze3d.matrix.MatrixStack;

public class GuiTextureSpriteButton {

    private final GuiTextureSprite texture;
    private final GuiTextureSprite hoveredTexture;
    private final GuiTextureSprite disabledTexture;

    public GuiTextureSpriteButton(GuiTextureSprite texture, GuiTextureSprite hoveredTexture, GuiTextureSprite disabledTexture) {
        this.texture = texture;
        this.hoveredTexture = hoveredTexture;
        this.disabledTexture = disabledTexture;
    }

    public void bind() {
        texture.bind();
    }

    public GuiTextureSprite asHovered() {
        return hoveredTexture;
    }

    public GuiTextureSprite asDisabled() {
        return disabledTexture;
    }

    public void draw(MatrixStack matrixStack, int x, int y, int width, int height, float zLevel) {
        texture.draw(matrixStack, x, y, width, height, zLevel);
    }
}
