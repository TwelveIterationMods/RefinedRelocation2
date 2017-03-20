package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.network.VanillaPacketHandler;
import net.blay09.mods.refinedrelocation.tile.INameable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class BlockMod extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyDirection DIRECTION = BlockDirectional.FACING;

	public BlockMod(Material material, String blockName) {
		super(material);
		setRegistryName(blockName);
		setUnlocalizedName(getRegistryNameString());
		setCreativeTab(RefinedRelocation.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryNameString(), "inventory"));
	}

	public String getRegistryNameString() {
		//noinspection ConstantConditions
		return getRegistryName().toString();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
		if (itemStack.hasDisplayName()) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity instanceof INameable) {
				((INameable) tileEntity).setCustomName(itemStack.getDisplayName());
			}
		}
	}

	protected boolean tryNameBlock(EntityPlayer player, ItemStack heldItem, IBlockAccess world, BlockPos pos) {
		if (!heldItem.isEmpty() && heldItem.getItem() == Items.NAME_TAG && heldItem.hasDisplayName()) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof INameable) {
				((INameable) tileEntity).setCustomName(heldItem.getDisplayName());
				VanillaPacketHandler.sendTileEntityUpdate(tileEntity);
				if (!player.capabilities.isCreativeMode) {
					heldItem.shrink(1);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null) {
			IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(itemHandler != null) {
				return ItemHandlerHelper.calcRedstoneFromInventory(itemHandler);
			}
		}
		return 0;
	}

}
