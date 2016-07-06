package net.blay09.mods.refinedrelocation.api.container;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface ITileGuiHandler {
	void openGui(EntityPlayer player, TileOrMultipart tileEntity);
}
