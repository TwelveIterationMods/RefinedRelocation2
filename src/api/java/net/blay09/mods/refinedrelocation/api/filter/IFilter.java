package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IFilter extends ISimpleFilter {
	String getIdentifier();
	String getLangKey();
	String getDescriptionLangKey();
	boolean isFilterUsable(TileEntity tileEntity);
	@SideOnly(Side.CLIENT)
	@Nullable
	IFilterIcon getFilterIcon();
	void openSettingsGui(EntityPlayer player, TileEntity tileEntity, int filterIndex);
}
