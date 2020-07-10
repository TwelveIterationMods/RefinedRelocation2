package net.blay09.mods.refinedrelocation.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IDrawable {
    void bind();

    void draw(MatrixStack matrixStack, double x, double y, double zLevel);

    void draw(MatrixStack matrixStack, double x, double y, double width, double height, double zLevel);
}
