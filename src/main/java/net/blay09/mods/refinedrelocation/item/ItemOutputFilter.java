package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;

public class ItemOutputFilter extends ItemMod {

	public ItemOutputFilter() {
		setRegistryName("output_filter");
		setUnlocalizedName(getRegistryNameString());
		setCreativeTab(RefinedRelocation.creativeTab);
		setMaxStackSize(1);
	}

}
