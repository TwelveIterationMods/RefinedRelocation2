package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class SizableButton extends Button {
    public SizableButton(int x, int y, int width, int height, ITextComponent text, IPressable pressable) {
        super(x, y, width, height, text, pressable);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, Minecraft p_renderBg_1_, int p_renderBg_2_, int p_renderBg_3_) {
        int textureY = getYImage(isHovered());
        blit(matrixStack, x, y, 0, 46 + textureY * 20, width / 2, height / 2);
        blit(matrixStack, x, y + height / 2, 0, 46 + textureY * 20 + 12, width / 2, height / 2);
        blit(matrixStack, x + width / 2, y, 200 - width / 2, 46 + textureY * 20, width / 2, height / 2);
        blit(matrixStack, x + width / 2, y + height / 2, 200 - width / 2, 46 + textureY * 20 + 12, width / 2, height / 2);
    }
}
