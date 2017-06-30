package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemStackLimiter extends Item {

	public static final String name = "stack_limiter";
	public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

	public ItemStackLimiter() {
		setUnlocalizedName(registryName.toString());
		setCreativeTab(RefinedRelocation.creativeTab);
		setMaxStackSize(1);
	}

}
