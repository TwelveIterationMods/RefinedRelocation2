package net.blay09.mods.refinedrelocation.api.container;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.entity.player.EntityPlayer;

public interface ITileGuiHandler {
	void openGui(EntityPlayer player, TileOrMultipart tileEntity);
}
