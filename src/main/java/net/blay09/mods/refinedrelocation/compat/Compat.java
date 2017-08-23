package net.blay09.mods.refinedrelocation.compat;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Compat {
	@GameRegistry.ObjectHolder("ironchest:iron_chest")
	public static Block ironChest;

	@GameRegistry.ObjectHolder("refinedrelocation:sorting_iron_chest")
	public static Block sortingIronChest;

	public static final String IRONCHEST = "ironchest";
}
