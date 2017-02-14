package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockSortingChest sortingChest;

	public static void init() {
		sortingChest = new BlockSortingChest();
		registerBlock(sortingChest);
		GameRegistry.registerTileEntity(TileSortingChest.class, sortingChest.getRegistryNameString());
	}

	private static void registerBlock(Block block) {
		GameRegistry.register(block);
		//noinspection ConstantConditions
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	public static void registerModels() {
		sortingChest.registerModel();
	}

}
