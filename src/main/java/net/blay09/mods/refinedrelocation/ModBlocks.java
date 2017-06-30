package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.BlockBlockExtender;
import net.blay09.mods.refinedrelocation.block.BlockFastHopper;
import net.blay09.mods.refinedrelocation.block.BlockFilteredHopper;
import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.blay09.mods.refinedrelocation.tile.TileFilteredHopper;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(RefinedRelocation.MOD_ID)
public class ModBlocks {

	@GameRegistry.ObjectHolder(BlockSortingChest.name)
	public static final Block sortingChest = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockBlockExtender.name)
	public static final Block blockExtender = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockFastHopper.name)
	public static final Block fastHopper = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockFilteredHopper.name)
	public static final Block filteredHopper = Blocks.AIR;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockSortingChest().setRegistryName(BlockSortingChest.name),
				new BlockBlockExtender().setRegistryName(BlockBlockExtender.name),
				new BlockFastHopper().setRegistryName(BlockFastHopper.name),
				new BlockFilteredHopper().setRegistryName(BlockFilteredHopper.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlock(sortingChest).setRegistryName(BlockSortingChest.name),
				new ItemBlock(blockExtender).setRegistryName(BlockBlockExtender.name),
				new ItemBlock(fastHopper).setRegistryName(BlockFastHopper.name),
				new ItemBlock(filteredHopper).setRegistryName(BlockFilteredHopper.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sortingChest), 0, new ModelResourceLocation(BlockSortingChest.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockExtender), 0, new ModelResourceLocation(BlockBlockExtender.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fastHopper), 0, new ModelResourceLocation(BlockFastHopper.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(filteredHopper), 0, new ModelResourceLocation(BlockFilteredHopper.registryName, "inventory"));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileSortingChest.class, BlockSortingChest.registryName.toString());
		GameRegistry.registerTileEntity(TileBlockExtender.class, BlockBlockExtender.registryName.toString());
		GameRegistry.registerTileEntity(TileFastHopper.class, BlockFastHopper.registryName.toString());
		GameRegistry.registerTileEntity(TileFilteredHopper.class, BlockFilteredHopper.registryName.toString());
	}

}
