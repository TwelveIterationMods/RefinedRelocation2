package net.blay09.mods.refinedrelocation.client.gui.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class GuiTextureSprite implements IDrawable {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/gui.png");

    private final int spriteX;
    private final int spriteY;
    private final int spriteWidth;
    private final int spriteHeight;

    public GuiTextureSprite(int spriteX, int spriteY, int spriteWidth, int spriteHeight) {
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public void bind() {
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    public void draw(PoseStack poseStack, double x, double y, double zLevel) {
        GuiComponent.blit(poseStack, (int) x, (int) y, spriteX, spriteY, spriteWidth, spriteHeight, 256, 256);
    }

    @Override
    public void draw(PoseStack poseStack, double x, double y, double width, double height, double zLevel) {
        GuiComponent.blit(poseStack, (int) x, (int) y, (int) width, (int) height, spriteX, spriteY, spriteWidth, spriteHeight, 256, 256);
    }
}
