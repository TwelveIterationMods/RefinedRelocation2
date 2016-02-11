package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.capability.IFilterProvider;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;

public class FilterProviderDefaultImpl implements IFilterProvider {

    private IFilter filter;

    @Override
    public IFilter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(IFilter filter) {
        this.filter = filter;
    }

}
