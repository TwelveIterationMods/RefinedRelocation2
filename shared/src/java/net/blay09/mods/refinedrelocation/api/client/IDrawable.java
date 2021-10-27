package net.blay09.mods.refinedrelocation.api.client;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IDrawable {
    void bind();

    void draw(PoseStack poseStack, double x, double y, double zLevel);

    void draw(PoseStack poseStack, double x, double y, double width, double height, double zLevel);
}
