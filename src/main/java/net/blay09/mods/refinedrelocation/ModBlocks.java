package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.BlockBlockExtender;
import net.blay09.mods.refinedrelocation.block.BlockBuffer;
import net.blay09.mods.refinedrelocation.block.BlockFastHopper;
import net.blay09.mods.refinedrelocation.block.BlockFilteredHopper;
import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileBuffer;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.blay09.mods.refinedrelocation.tile.TileFilteredHopper;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static BlockSortingChest sortingChest;
	public static BlockBlockExtender blockExtender;
	public static BlockFastHopper fastHopper;
	public static BlockFilteredHopper filteredHopper;
	public static BlockBuffer buffer;

	public static void init() {
		sortingChest = new BlockSortingChest();
		registerBlock(sortingChest);
		GameRegistry.registerTileEntity(TileSortingChest.class, sortingChest.getRegistryNameString());

		blockExtender = new BlockBlockExtender();
		registerBlock(blockExtender);
		GameRegistry.registerTileEntity(TileBlockExtender.class, blockExtender.getRegistryNameString());

		fastHopper = new BlockFastHopper();
		registerBlock(fastHopper);
		GameRegistry.registerTileEntity(TileFastHopper.class, fastHopper.getRegistryNameString());

		filteredHopper = new BlockFilteredHopper();
		registerBlock(filteredHopper);
		GameRegistry.registerTileEntity(TileFilteredHopper.class, filteredHopper.getRegistryNameString());

//		buffer = new BlockBuffer();
//		registerBlock(buffer);
//		GameRegistry.registerTileEntity(TileBuffer.class, buffer.getRegistryNameString());
	}

	private static void registerBlock(Block block) {
		GameRegistry.register(block);
		//noinspection ConstantConditions
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		sortingChest.registerModel();
		blockExtender.registerModel();
		fastHopper.registerModel();
		filteredHopper.registerModel();
//		buffer.registerModel();
	}

}
