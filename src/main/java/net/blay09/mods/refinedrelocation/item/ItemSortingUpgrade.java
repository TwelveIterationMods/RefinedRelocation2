package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.block.BlockSortingChest;
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
import net.minecraft.world.World;

public class ItemSortingUpgrade extends ItemMod {

	public ItemSortingUpgrade() {
		setRegistryName("sortingUpgrade");
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(RefinedRelocation.creativeTab);
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		 if(!world.isRemote) {
			 IBlockState state = world.getBlockState(pos);
			 if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
				 if (upgradeVanillaChest(player, world, pos, state)) {
					 if(!player.capabilities.isCreativeMode) {
						 itemStack.stackSize--;
					 }
					 return EnumActionResult.SUCCESS;
				 }
			 }
			 TileEntity tileEntity = world.getTileEntity(pos);
			 if (tileEntity != null) {
				 // TODO capability upgrading
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
		if(tileEntity.hasCustomName()) {
			tileSortingChest.setCustomName(tileEntity.getDisplayName().getUnformattedText());
		}
		for(int i = 0; i < inventory.length; i++) {
			tileSortingChest.getItemHandler().setStackInSlot(i, inventory[i]);
		}
		return true;
	}

}
