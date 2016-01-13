package net.blay09.mods.refinedrelocation2.filter;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RootFilter implements IFilter {

    private final List<IFilter> filterList = Lists.newArrayList();
    private boolean isBlacklist;

    @Override
    public String getTypeName() {
        return "root";
    }

    @Override
    public boolean passesFilter(ItemStack itemStack) {
        boolean passes = false;
        for (IFilter filter : filterList) {
            if (filter.passesFilter(itemStack)) {
                passes = !filter.isBlacklist();
            }
        }
        return passes;
    }

    @Override
    public boolean isBlacklist() {
        return isBlacklist;
    }

    @Override
    public void setBlacklist(boolean isBlacklist) {
        this.isBlacklist = isBlacklist;
    }
}
