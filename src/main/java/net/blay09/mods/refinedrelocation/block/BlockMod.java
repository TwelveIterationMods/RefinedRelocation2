package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMod extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockMod(Material material, String blockName) {
		super(material);
		setRegistryName(blockName);
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(RefinedRelocation.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void registerModel(ItemModelMesher mesher) {
		Item item = Item.getItemFromBlock(this);
		if(item != null) {
			mesher.register(item, 0, new ModelResourceLocation(getRegistryName().toString(), "inventory"));
		}
	}
}
