package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class SizableButton extends Button {
    public SizableButton(int x, int y, int width, int height, Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress);
    }

    @Override
    protected void renderBg(PoseStack poseSTack, Minecraft mc, int mouseX, int mouseY) {
        int textureY = getYImage(isHovered());
        blit(poseSTack, x, y, 0, 46 + textureY * 20, width / 2, height / 2);
        blit(poseSTack, x, y + height / 2, 0, 46 + textureY * 20 + 12, width / 2, height / 2);
        blit(poseSTack, x + width / 2, y, 200 - width / 2, 46 + textureY * 20, width / 2, height / 2);
        blit(poseSTack, x + width / 2, y + height / 2, 200 - width / 2, 46 + textureY * 20 + 12, width / 2, height / 2);
    }
}
