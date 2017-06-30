package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;

public class GuiOpenFilterButton extends GuiButton {

	private final TextureAtlasRegion background;
	private final TextureAtlasRegion backgroundHover;
	private final TextureAtlasRegion backgroundDisabled;

	private final TileEntity tileEntity;

	public GuiOpenFilterButton(int id, int x, int y, TileEntity tileEntity) {
		super(id, x, y, "");
		this.tileEntity = tileEntity;
//		if(smallVersion) {
		background = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:small_filter_button");
		backgroundHover = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:small_filter_button_hover");
		backgroundDisabled = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:small_filter_button_disabled");
//		} else {
//		background = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_button");
//		backgroundHover = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_button_hover");
//		backgroundDisabled = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_button_disabled");
//		}
		width = background.getIconWidth();
		height = background.getIconHeight();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if(visible) {
			GlStateManager.color(1f, 1f, 1f, 1f);
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			int hoverState = getHoverState(hovered);
			switch(hoverState) {
				case 0: backgroundDisabled.draw(x, y, zLevel); break;
				case 1: background.draw(x, y, zLevel); break;
				case 2: backgroundHover.draw(x, y, zLevel); break;
			}
		}
	}

	public TileEntity getTileEntity() {
		return tileEntity;
	}

}
