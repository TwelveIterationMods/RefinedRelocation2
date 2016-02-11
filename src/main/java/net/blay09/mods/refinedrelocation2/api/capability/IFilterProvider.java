package net.blay09.mods.refinedrelocation2.api.capability;

import net.blay09.mods.refinedrelocation2.api.filter.IFilter;

public interface IFilterProvider {
    IFilter getFilter();
    void setFilter(IFilter filter);
}
