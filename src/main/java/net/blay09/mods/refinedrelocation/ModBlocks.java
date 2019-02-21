package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.*;
import net.blay09.mods.refinedrelocation.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	public static Block sortingChest;
	public static Block blockExtender;
	public static Block fastHopper;
	public static Block filteredHopper;
	public static Block sortingConnector;
	public static Block sortingInterface;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				sortingChest = new BlockSortingChest().setRegistryName(BlockSortingChest.name),
				blockExtender = new BlockBlockExtender().setRegistryName(BlockBlockExtender.name),
				fastHopper = new BlockFastHopper().setRegistryName(BlockFastHopper.name),
				filteredHopper = new BlockFilteredHopper().setRegistryName(BlockFilteredHopper.name),
				sortingConnector = new BlockSortingConnector().setRegistryName(BlockSortingConnector.name),
				sortingInterface = new BlockSortingInterface().setRegistryName(BlockSortingInterface.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlock(sortingChest).setRegistryName(BlockSortingChest.name),
				new ItemBlock(blockExtender).setRegistryName(BlockBlockExtender.name),
				new ItemBlock(fastHopper).setRegistryName(BlockFastHopper.name),
				new ItemBlock(filteredHopper).setRegistryName(BlockFilteredHopper.name),
				new ItemBlock(sortingConnector).setRegistryName(BlockSortingConnector.name),
				new ItemBlock(sortingInterface).setRegistryName(BlockSortingInterface.name)
		);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileSortingChest.class, BlockSortingChest.registryName.toString());
		GameRegistry.registerTileEntity(TileBlockExtender.class, BlockBlockExtender.registryName.toString());
		GameRegistry.registerTileEntity(TileFastHopper.class, BlockFastHopper.registryName.toString());
		GameRegistry.registerTileEntity(TileFilteredHopper.class, BlockFilteredHopper.registryName.toString());
		GameRegistry.registerTileEntity(TileSortingConnector.class, BlockSortingConnector.registryName.toString());
		GameRegistry.registerTileEntity(TileSortingInterface.class, BlockSortingInterface.registryName.toString());
	}

}
