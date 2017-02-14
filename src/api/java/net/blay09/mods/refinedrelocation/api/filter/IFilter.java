package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
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
}
