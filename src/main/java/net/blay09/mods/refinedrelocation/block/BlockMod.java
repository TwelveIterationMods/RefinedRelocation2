package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockMod extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockMod(Material material, String blockName) {
		super(material);
		setRegistryName(blockName);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(RefinedRelocation.creativeTab);
	}
}
