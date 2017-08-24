package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.BlockBlockExtender;
import net.blay09.mods.refinedrelocation.block.BlockFastHopper;
import net.blay09.mods.refinedrelocation.block.BlockFilteredHopper;
import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation.block.BlockSortingConnector;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.blay09.mods.refinedrelocation.tile.TileFilteredHopper;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingConnector;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(RefinedRelocation.MOD_ID)
public class ModBlocks {

	@GameRegistry.ObjectHolder(BlockSortingChest.name)
	public static Block sortingChest;

	@GameRegistry.ObjectHolder(BlockBlockExtender.name)
	public static Block blockExtender;

	@GameRegistry.ObjectHolder(BlockFastHopper.name)
	public static Block fastHopper;

	@GameRegistry.ObjectHolder(BlockFilteredHopper.name)
	public static Block filteredHopper;

	@GameRegistry.ObjectHolder(BlockSortingConnector.name)
	public static BlockSortingConnector sortingConnector;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockSortingChest().setRegistryName(BlockSortingChest.name),
				new BlockBlockExtender().setRegistryName(BlockBlockExtender.name),
				new BlockFastHopper().setRegistryName(BlockFastHopper.name),
				new BlockFilteredHopper().setRegistryName(BlockFilteredHopper.name),
				new BlockSortingConnector().setRegistryName(BlockSortingConnector.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlock(sortingChest).setRegistryName(BlockSortingChest.name),
				new ItemBlock(blockExtender).setRegistryName(BlockBlockExtender.name),
				new ItemBlock(fastHopper).setRegistryName(BlockFastHopper.name),
				new ItemBlock(filteredHopper).setRegistryName(BlockFilteredHopper.name),
				new ItemBlock(sortingConnector).setRegistryName(BlockSortingConnector.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sortingChest), 0, new ModelResourceLocation(BlockSortingChest.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockExtender), 0, new ModelResourceLocation(BlockBlockExtender.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fastHopper), 0, new ModelResourceLocation(BlockFastHopper.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(filteredHopper), 0, new ModelResourceLocation(BlockFilteredHopper.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sortingConnector), 0, new ModelResourceLocation(BlockSortingConnector.registryName, "inventory"));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileSortingChest.class, BlockSortingChest.registryName.toString());
		GameRegistry.registerTileEntity(TileBlockExtender.class, BlockBlockExtender.registryName.toString());
		GameRegistry.registerTileEntity(TileFastHopper.class, BlockFastHopper.registryName.toString());
		GameRegistry.registerTileEntity(TileFilteredHopper.class, BlockFilteredHopper.registryName.toString());
		GameRegistry.registerTileEntity(TileSortingConnector.class, BlockSortingConnector.registryName.toString());
	}

}
