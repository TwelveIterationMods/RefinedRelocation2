package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemSortingUpgrade sortingUpgrade;

	public static void init() {
		sortingUpgrade = new ItemSortingUpgrade();
		GameRegistry.register(sortingUpgrade);
	}

	public static void registerModels() {
		sortingUpgrade.registerModels();
	}

}
