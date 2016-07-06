package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static BlockSortingChest sortingChest;

	public static void init() {
		sortingChest = new BlockSortingChest();
		registerBlock(sortingChest);
		GameRegistry.registerTileEntity(TileSortingChest.class, sortingChest.getRegistryName().toString());
	}

	private static void registerBlock(Block block) {
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		sortingChest.registerModel(mesher);
	}
}
