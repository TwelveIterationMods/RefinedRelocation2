package net.blay09.mods.refinedrelocation2.item;

import net.blay09.mods.refinedrelocation2.ModBlocks;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.ISortingUpgrade;
import net.blay09.mods.refinedrelocation2.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSortingUpgrade extends Item {

    public ItemSortingUpgrade() {
        setRegistryName("sorting_upgrade");
        setUnlocalizedName(getRegistryName());
        setCreativeTab(RefinedRelocation2.creativeTab);
        GameRegistry.registerItem(this);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState blockState = world.getBlockState(pos);
        if(blockState.getBlock() instanceof ISortingUpgrade) {
            if(((ISortingUpgrade) blockState.getBlock()).applySortingUpgrade(entityPlayer, world, pos, blockState, side, hitX, hitY, hitZ)) {
                itemStack.stackSize--;
            }
            return !world.isRemote;
        }

        if(blockState.getBlock() == Blocks.chest || blockState.getBlock() == Blocks.trapped_chest) {
            if(upgradeVanillaChest(entityPlayer, world, pos, blockState, side, hitX, hitY, hitZ)) {
                itemStack.stackSize--;
            }
            return !world.isRemote;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public static boolean upgradeVanillaChest(EntityPlayer entityPlayer, World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntityChest tileEntity = (TileEntityChest) world.getTileEntity(pos);
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
        for(int i = 0; i < inventory.length; i++) {
            tileSortingChest.setInventorySlotContents(i, inventory[i]);
        }
        return true;
    }
}
