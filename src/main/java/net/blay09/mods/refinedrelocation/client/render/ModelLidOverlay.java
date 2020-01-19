package net.blay09.mods.refinedrelocation.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelLidOverlay extends Model {

    public static class ModelQuad extends ModelRenderer.ModelBox {
        private final TexturedQuad actualQuad;

        public ModelQuad(ModelRenderer renderer, int texOffsetX, int texOffsetY, float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor, boolean mirror) {
            super(renderer, texOffsetX, texOffsetY, offX, offY, offZ, width, height, depth, scaleFactor, mirror);

            float x1 = offX - scaleFactor;
            float x2 = offX + (float) width + scaleFactor;
            float y = offY + scaleFactor;
            float z1 = offZ - scaleFactor;
            float z2 = offZ + (float) depth + scaleFactor;

            PositionTextureVertex[] vertices = new PositionTextureVertex[]{
                    new PositionTextureVertex(x1, y, z1, 0f, 0f),
                    new PositionTextureVertex(x2, y, z1, 8f, 0f),
                    new PositionTextureVertex(x2, y, z2, 8f, 8f),
                    new PositionTextureVertex(x1, y, z2, 0f, 8f),
            };
            int u = 0;
            int v = 0;
            int u2 = 16;
            int v2 = 16;
            actualQuad = new TexturedQuad(vertices, u, v, u2, v2, renderer.textureWidth, renderer.textureHeight);
        }

        @Override
        public void render(BufferBuilder renderer, float scale) {
            actualQuad.draw(renderer, scale);
        }
    }

    public final ModelRenderer chestLid;

    public ModelLidOverlay() {
        chestLid = new ModelRenderer(this, 0, 0);
        chestLid.setTextureSize(16, 16);
        chestLid.cubeList.add(new ModelQuad(chestLid, 3, 1, 0f, -5.001f, -14f, 14, 5, 14, 0f, chestLid.mirror));
        chestLid.rotationPointX = 1f;
        chestLid.rotationPointY = 7f;
        chestLid.rotationPointZ = 15f;
    }

    public void renderAll() {
        chestLid.render(0.0625f);
    }

}
