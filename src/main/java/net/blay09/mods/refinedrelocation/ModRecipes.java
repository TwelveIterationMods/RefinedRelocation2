package net.blay09.mods.refinedrelocation;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

	public static void init() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.sortingChest), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', "dustRedstone", 'C', "chestWood", 'H', Blocks.HOPPER));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.sortingUpgrade), " B ", "RPR", " H ", 'B', Items.WRITABLE_BOOK, 'R', "dustRedstone", 'P', "plankWood", 'H', Blocks.HOPPER));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.fastHopper), "RHR", 'R', "dustRedstone", 'H', Blocks.HOPPER));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.filteredHopper), " B ", "RHR", 'B', Items.WRITABLE_BOOK, 'R', "dustRedstone", 'H', Blocks.HOPPER));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.filteredHopper), ModBlocks.fastHopper, Items.WRITABLE_BOOK));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockExtender), "IGI", "GHG", "IGI", 'I', "ingotIron", 'G', "paneGlass", 'H', ModBlocks.fastHopper));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.stackLimiter), "plankWood", "nuggetGold", "paper"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.inputFilter), "plankWood", "nuggetGold", Items.WRITABLE_BOOK));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.outputFilter), "plankWood", "dustRedstone", Items.WRITABLE_BOOK));
	}

}
