package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface IConfigurableFilter {
	@Nullable
	Container createContainer(EntityPlayer player, TileEntity tileEntity);
	@OnlyIn(Dist.CLIENT)
	@Nullable
	GuiScreen createGuiScreen(EntityPlayer player, TileEntity tileEntity);
}
