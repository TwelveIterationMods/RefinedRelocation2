package net.blay09.mods.refinedrelocation.api;

import net.blay09.mods.refinedrelocation.api.filter.IMultiRootFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {

    @CapabilityInject(IRootFilter.class)
    public static Capability<IRootFilter> ROOT_FILTER;

    @CapabilityInject(IMultiRootFilter.class)
    public static Capability<IMultiRootFilter> MULTI_ROOT_FILTER;

    @CapabilityInject(ISimpleFilter.class)
    public static Capability<ISimpleFilter> SIMPLE_FILTER;

    /**
     * Note that instances of this capability are being cached without validity checks.
     */
    @CapabilityInject(ISortingGridMember.class)
    public static Capability<ISortingGridMember> SORTING_GRID_MEMBER;

    @CapabilityInject(ISortingInventory.class)
    public static Capability<ISortingInventory> SORTING_INVENTORY;

    @CapabilityInject(ISortingUpgradable.class)
    public static Capability<ISortingUpgradable> SORTING_UPGRADABLE;

    /**
     * Ignore nullable warning here because I know what I'm doing and IntelliJ should just stop annoying me already.
     */
    public static <T> T getDefaultInstance(Capability<T> capability) {
        //noinspection ConstantConditions
        return capability.getDefaultInstance();
    }

    public static boolean isSortingGridCapability(Capability<?> capability) {
        return capability == SORTING_GRID_MEMBER || capability == SORTING_INVENTORY;
    }

    public static boolean isFilterCapability(Capability<?> capability) {
        return capability == ROOT_FILTER || capability == SIMPLE_FILTER;
    }

}
