package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.util.TileWrapper;

public interface IFilter extends ISimpleFilter {
	String getIdentifier();
	boolean isFilterUsable(TileWrapper pos);
}
