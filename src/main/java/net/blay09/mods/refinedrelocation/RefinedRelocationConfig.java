package net.blay09.mods.refinedrelocation;

import net.minecraftforge.common.config.Config;

@Config(modid = RefinedRelocation.MOD_ID)
public class RefinedRelocationConfig {

	public static boolean renderChestNameTags = true;

	@Config.Comment("The amount of slots each sorting interface should check for changes per tick. Setting this too high while having many interfaces on large inventories can cause performance issues.")
	public static int sortingInterfaceSlotsPerTick = 9;

}
