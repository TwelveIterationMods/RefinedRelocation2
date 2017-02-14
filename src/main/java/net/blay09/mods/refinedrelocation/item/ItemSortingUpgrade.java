package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingUpgradable;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ItemSortingUpgrade extends ItemMod {

	public ItemSortingUpgrade() {
		setRegistryName("sorting_upgrade");
		setUnlocalizedName(getRegistryNameString());
		setCreativeTab(RefinedRelocation.creativeTab);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			ItemStack itemStack = player.getHeldItem(hand);
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
				if (upgradeVanillaChest(player, world, pos, state)) {
					if(!player.capabilities.isCreativeMode) {
						itemStack.shrink(1);
					}
					return EnumActionResult.SUCCESS;
				}
			}
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity != null && tileEntity.hasCapability(CapabilitySortingUpgradable.CAPABILITY, facing)) {
				ISortingUpgradable sortingUpgradable = tileEntity.getCapability(CapabilitySortingUpgradable.CAPABILITY, facing);
				if(sortingUpgradable != null && sortingUpgradable.applySortingUpgrade(tileEntity, itemStack, player, world, pos, facing, hitX, hitY, hitZ, hand)) {
					if(!player.capabilities.isCreativeMode) {
						itemStack.shrink(1);
					}
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}

	public static boolean upgradeVanillaChest(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
		TileEntityChest tileEntity = (TileEntityChest) world.getTileEntity(pos);
		if(tileEntity == null) {
			return false;
		}
		if(tileEntity.numPlayersUsing > 0) {
			return false;
		}
		ItemStack[] inventory = new ItemStack[tileEntity.getSizeInventory()];
		for(int i = 0; i < inventory.length; i++) {
			inventory[i] = tileEntity.getStackInSlot(i);
		}
		tileEntity.clear();
		IBlockState newState = ModBlocks.sortingChest.getDefaultState().withProperty(BlockSortingChest.FACING, state.getValue(BlockChest.FACING));
		world.setBlockState(pos, newState);
		TileSortingChest tileSortingChest = (TileSortingChest) world.getTileEntity(pos);
		if(tileSortingChest != null) {
			if (tileEntity.hasCustomName()) {
				ITextComponent displayName = tileEntity.getDisplayName();
				if(displayName != null) {
					tileSortingChest.setCustomName(displayName.getUnformattedText());
				}
			}
			for (int i = 0; i < inventory.length; i++) {
				tileSortingChest.getItemHandler().setStackInSlot(i, inventory[i]);
			}
		}
		return true;
	}

}
