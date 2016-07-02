package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IFilter extends ISimpleFilter {
	String getIdentifier();
	String getLangKey();
	String getDescriptionLangKey();
	boolean isFilterUsable(TileOrMultipart tileEntity);
	@SideOnly(Side.CLIENT)
	@Nullable
	IFilterIcon getFilterIcon();

	boolean isConfigurable();
	@Nullable
	Container createContainer(EntityPlayer player, TileOrMultipart tileEntity);
	@SideOnly(Side.CLIENT)
	@Nullable
	GuiScreen createGuiScreen(EntityPlayer player, TileOrMultipart tileEntity);
}
