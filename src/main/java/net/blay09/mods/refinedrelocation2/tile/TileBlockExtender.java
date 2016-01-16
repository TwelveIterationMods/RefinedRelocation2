package net.blay09.mods.refinedrelocation2.tile;

import net.blay09.mods.refinedrelocation2.Compatibility;
import net.blay09.mods.refinedrelocation2.block.BlockExtender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;

public class TileBlockExtender extends TileEntity implements ITickable, ISidedInventory {

    private TileEntity connectedTileEntity;
    private EnumFacing facing;

    private boolean firstTick = true;

    @Override
    public void update() {
        if(firstTick) {
            facing = worldObj.getBlockState(getPos()).getValue(BlockExtender.FACING);
            connectedTileEntity = worldObj.getTileEntity(getPos().offset(facing));
            firstTick = false;
        }
    }

    public IInventory getInventory() {
        if(connectedTileEntity instanceof IInventory) {
            return (IInventory) connectedTileEntity;
        }
        return null;
    }

    private ISidedInventory getSidedInventory(EnumFacing side) {
        if(connectedTileEntity instanceof ISidedInventory) {
            return (ISidedInventory) connectedTileEntity;
        }
        return null;
    }

    @Override
    public int getSizeInventory() {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.getStackInSlot(index);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.decrStackSize(index, count);
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.removeStackFromSlot(index);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            inventory.setInventorySlotContents(index, itemStack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        IInventory inventory = getInventory();
        return inventory != null && inventory.isUseableByPlayer(entityPlayer);
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            inventory.openInventory(entityPlayer);
        }
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            inventory.openInventory(entityPlayer);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        IInventory inventory = getInventory();
        return inventory != null && inventory.isItemValidForSlot(index, itemStack);
    }

    @Override
    public int getField(int id) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            inventory.getField(id);
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        IInventory inventory = getInventory();
        if(inventory != null) {
            inventory.setField(id, value);
        }
    }

    @Override
    public int getFieldCount() {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.getFieldCount();
        }
        return 0;
    }

    @Override
    public void clear() {
        IInventory inventory = getInventory();
        if(inventory != null) {
            inventory.clear();
        }
    }

    @Override
    public String getName() {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.getName();
        }
        return null;
    }

    @Override
    public boolean hasCustomName() {
        IInventory inventory = getInventory();
        return inventory != null && inventory.hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        IInventory inventory = getInventory();
        if(inventory != null) {
            return inventory.getDisplayName();
        }
        return null;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        ISidedInventory inventory = getSidedInventory(side);
        if(inventory != null) {
            return inventory.getSlotsForFace(getIOSide(side));
        }
        return null;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, EnumFacing side) {
        ISidedInventory inventory = getSidedInventory(side);
        return inventory != null && inventory.canInsertItem(index, itemStack, getIOSide(side));
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, EnumFacing side) {
        ISidedInventory inventory = getSidedInventory(side);
        return inventory != null && inventory.canExtractItem(index, itemStack, getIOSide(side));
    }

    public EnumFacing getIOSide(EnumFacing side) {
        return facing.getOpposite();
    }

    public boolean hasVisibleConnection(EnumFacing side) {
        if(side == facing) {
            return false;
        }
        BlockPos sidePos = getPos().offset(side);
        IBlockState blockState = worldObj.getBlockState(sidePos);
        if(blockState.getBlock().canProvidePower()) {
            return true;
        }
        TileEntity tileEntity = worldObj.getTileEntity(sidePos);
        if(tileEntity != null) {
            for(Capability capability : Compatibility.blockExtenderConnections) {
                if(tileEntity.hasCapability(capability, side.getOpposite())) {
                    return true;
                }
            }
        }
        return false;
    }
}
