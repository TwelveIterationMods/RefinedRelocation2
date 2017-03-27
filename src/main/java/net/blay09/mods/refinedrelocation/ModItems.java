package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.blay09.mods.refinedrelocation.item.ItemStackLimiter;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemSortingUpgrade sortingUpgrade;
	public static ItemStackLimiter stackLimiter;

	public static void init() {
		sortingUpgrade = new ItemSortingUpgrade();
		GameRegistry.register(sortingUpgrade);

		stackLimiter = new ItemStackLimiter();
		GameRegistry.register(stackLimiter);
	}

	public static void registerModels() {
		sortingUpgrade.registerModels();
		stackLimiter.registerModels();
	}

}
