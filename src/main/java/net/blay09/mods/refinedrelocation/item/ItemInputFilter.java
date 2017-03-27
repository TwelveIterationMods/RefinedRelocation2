package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;

public class ItemInputFilter extends ItemMod {

	public ItemInputFilter() {
		setRegistryName("input_filter");
		setUnlocalizedName(getRegistryNameString());
		setCreativeTab(RefinedRelocation.creativeTab);
		setMaxStackSize(1);
	}

}
