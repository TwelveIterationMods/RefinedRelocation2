package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.item.ItemInputFilter;
import net.blay09.mods.refinedrelocation.item.ItemOutputFilter;
import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.blay09.mods.refinedrelocation.item.ItemStackLimiter;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(RefinedRelocation.MOD_ID)
public class ModItems {

	@GameRegistry.ObjectHolder(ItemSortingUpgrade.name)
	public static final Item sortingUpgrade = Items.AIR;

	@GameRegistry.ObjectHolder(ItemStackLimiter.name)
	public static final Item stackLimiter = Items.AIR;

	@GameRegistry.ObjectHolder(ItemInputFilter.name)
	public static final Item inputFilter = Items.AIR;

	@GameRegistry.ObjectHolder(ItemOutputFilter.name)
	public static final Item outputFilter = Items.AIR;

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemSortingUpgrade().setRegistryName(ItemSortingUpgrade.name),
				new ItemStackLimiter().setRegistryName(ItemStackLimiter.name),
				new ItemInputFilter().setRegistryName(ItemInputFilter.name),
				new ItemOutputFilter().setRegistryName(ItemOutputFilter.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(sortingUpgrade, 0, new ModelResourceLocation(ItemSortingUpgrade.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(stackLimiter, 0, new ModelResourceLocation(ItemStackLimiter.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(inputFilter, 0, new ModelResourceLocation(ItemInputFilter.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(outputFilter, 0, new ModelResourceLocation(ItemOutputFilter.registryName, "inventory"));
	}

}
