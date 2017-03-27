package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;

public class ItemStackLimiter extends ItemMod {

	public ItemStackLimiter() {
		setRegistryName("stack_limiter");
		setUnlocalizedName(getRegistryNameString());
		setCreativeTab(RefinedRelocation.creativeTab);
		setMaxStackSize(1);
	}

}
