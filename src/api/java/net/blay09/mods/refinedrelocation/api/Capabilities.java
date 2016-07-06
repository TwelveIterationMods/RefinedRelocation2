package net.blay09.mods.refinedrelocation.api;

import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {

	@CapabilityInject(IRootFilter.class)
	public static Capability<IRootFilter> ROOT_FILTER;

	@CapabilityInject(ISimpleFilter.class)
	public static Capability<ISimpleFilter> SIMPLE_FILTER;

	@CapabilityInject(ISortingGridMember.class)
	public static Capability<ISortingGridMember> SORTING_GRID_MEMBER;

	@CapabilityInject(ISortingInventory.class)
	public static Capability<ISortingInventory> SORTING_INVENTORY;

	@CapabilityInject(ISortingUpgradable.class)
	public static Capability<ISortingUpgradable> SORTING_UPGRADABLE;

}
