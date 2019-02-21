package net.blay09.mods.refinedrelocation.compat.ironchest;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Locale;

public class ItemBlockSortingIronChest extends ItemBlock {
	public ItemBlockSortingIronChest(Block block) {
		super(block);
		//noinspection ConstantConditions
		setRegistryName(block.getRegistryName());
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return "tile." + RefinedRelocation.MOD_ID + ":ironchest.sorting_chest_" + IronChestType.VALUES[itemStack.getMetadata()].name().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
