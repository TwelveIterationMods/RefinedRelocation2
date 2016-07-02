package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiOpenFilterButton extends GuiButton {

	private final TextureAtlasRegion background;
	private final TextureAtlasRegion backgroundHover;
	private final TextureAtlasRegion backgroundDisabled;

	private final TileOrMultipart tileEntity;

	public GuiOpenFilterButton(int id, int x, int y, TileOrMultipart tileEntity) {
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
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(visible) {
			GlStateManager.color(1f, 1f, 1f, 1f);
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int hoverState = getHoverState(hovered);
			switch(hoverState) {
				case 0: backgroundDisabled.draw(xPosition, yPosition, zLevel); break;
				case 1: background.draw(xPosition, yPosition, zLevel); break;
				case 2: backgroundHover.draw(xPosition, yPosition, zLevel); break;
			}
		}
	}

	public TileOrMultipart getTileEntity() {
		return tileEntity;
	}

}
