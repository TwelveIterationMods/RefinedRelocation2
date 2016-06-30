package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IFilter extends ISimpleFilter {
	String getIdentifier();
	String getLangKey();
	@SideOnly(Side.CLIENT)
	@Nullable
	IFilterIcon getFilterIcon();
	boolean isFilterUsable(TileWrapper pos);
}
