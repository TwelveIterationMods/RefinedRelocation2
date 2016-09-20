package net.blay09.mods.refinedrelocation;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	public static void init() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.sortingChest), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', "chest", 'H', Blocks.HOPPER));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.sortingUpgrade), " B ", "RPR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'P', "plankWood", 'H', Blocks.HOPPER));
	}

}
