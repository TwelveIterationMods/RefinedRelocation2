package net.blay09.mods.refinedrelocation.client.util;

import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextureAtlasRegion extends TextureAtlasSprite implements IFilterIcon {

	private TextureAtlas atlas;

	public TextureAtlasRegion(TextureAtlas atlas, String name) {
		super(name);
		this.atlas = atlas;
	}

	public TextureAtlasRegion(TextureAtlas atlas, ResourceLocation location) {
		this(atlas, location.toString());
	}

	@Override
	public void draw(double x, double y, double zLevel) {
		draw(x, y, getIconWidth(), getIconHeight(), zLevel);
	}

	public void draw(double x, double y, double width, double height, double zLevel) {
		GlStateManager.enableAlpha();
		GlStateManager.bindTexture(atlas.getGlTextureId());
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x, y + height, zLevel).tex(getMinU(), getMaxV()).endVertex();
		renderer.pos(x + width, y + height, zLevel).tex(getMaxU(), getMaxV()).endVertex();
		renderer.pos(x + width, y, zLevel).tex(getMaxU(), getMinV()).endVertex();
		renderer.pos(x, y, zLevel).tex(getMinU(), getMinV()).endVertex();
		tessellator.draw();
		GlStateManager.disableAlpha();
	}

}
