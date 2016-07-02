package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IConfigurableFilter {
	@Nullable
	Container createContainer(EntityPlayer player, TileOrMultipart tileEntity);
	@SideOnly(Side.CLIENT)
	@Nullable
	GuiScreen createGuiScreen(EntityPlayer player, TileOrMultipart tileEntity);
}
