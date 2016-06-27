package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.util.GridContainer;

public interface IFilter extends ISimpleFilter {
	String getIdentifier();
	boolean isFilterUsable(GridContainer pos);
}
