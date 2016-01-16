package net.blay09.mods.refinedrelocation2.tile;

import mcmultipart.block.TileCoverable;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.balyware.ItemUtils;
import net.blay09.mods.refinedrelocation2.block.BlockBetterHopper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class TileBetterHopper extends TileCoverable implements IInventory, ITickable {

    private ItemStack[] inventory = new ItemStack[5];
    protected String customName;
    private int cooldown;

    @Override
    public void update() {
        if (worldObj != null && !worldObj.isRemote) {
            cooldown--;
            if(cooldown <= 0) {

                EnumFacing facing = worldObj.getBlockState(getPos()).getValue(BlockBetterHopper.FACING);
                EnumFacing opposite = facing.getOpposite();
                TileEntity facingEntity = worldObj.getTileEntity(pos.offset(facing));
                IInventory targetInventory = null;
                if(facingEntity instanceof IInventory) {
                    targetInventory = (IInventory) facingEntity;
                }
                boolean hasSpace = false;
                if(targetInventory != null) {
                    for (int i = 0; i < inventory.length; i++) {
                        if (inventory[i] != null) {
                            pushItem(i, targetInventory, opposite);
                        }
                        if (!hasSpace && (inventory[i] == null || inventory[i].stackSize < inventory[i].getMaxStackSize())) {
                            hasSpace = true;
                        }
                    }
                }

                if(hasSpace) {
                    BlockPos upPos = pos.offset(EnumFacing.UP);
                    TileEntity upEntity = worldObj.getTileEntity(upPos);
                    if (upEntity instanceof IInventory) {
                        IInventory sourceInventory = (IInventory) upEntity;
                        pullItem(sourceInventory);
                    } else {
                        float range = 0.75f;
                        worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + 1.5f, pos.getZ() + range), EntitySelectors.selectAnything).forEach(this::pullItem);
                    }
                }

                cooldown = 20;
            }
        }
    }

    public void pushItem(int sourceSlot, IInventory targetInventory, EnumFacing from) {
        if(targetInventory instanceof ISidedInventory) {
            ISidedInventory sidedInventory = (ISidedInventory) targetInventory;
            for(int i : sidedInventory.getSlotsForFace(from)) {
                if(sidedInventory.canInsertItem(i, inventory[sourceSlot], from) && sidedInventory.isItemValidForSlot(i, inventory[sourceSlot])) {
                    if(pushItem(sourceSlot, targetInventory, i, from)) {
                        return;
                    }
                }
            }
        } else {
            for(int i = 0; i < targetInventory.getSizeInventory(); i++) {
                if(targetInventory.isItemValidForSlot(i, inventory[sourceSlot])) {
                    if(pushItem(sourceSlot, targetInventory, i, from)) {
                        return;
                    }
                }
            }
        }
    }

    public boolean pushItem(int sourceSlot, IInventory targetInventory, int targetSlot, EnumFacing from) {
        ItemStack insertStack;
        ItemStack targetStack = targetInventory.getStackInSlot(targetSlot);
        if(targetStack == null) {
            insertStack = inventory[sourceSlot].splitStack(Math.min(targetInventory.getInventoryStackLimit(), inventory[sourceSlot].stackSize));
            targetInventory.setInventorySlotContents(targetSlot, insertStack);
            if(inventory[sourceSlot].stackSize == 0) {
                setInventorySlotContents(sourceSlot, null);
            }
            return true;
        } else {
            if(!ItemUtils.canMergeItems(inventory[sourceSlot], targetStack)) {
                return false;
            }
            int spaceLeft = Math.min(targetInventory.getInventoryStackLimit(), targetStack.getMaxStackSize() - targetStack.stackSize);
            if(spaceLeft > 0) {
                insertStack = inventory[sourceSlot].splitStack(Math.min(spaceLeft, inventory[sourceSlot].stackSize));
                targetStack.stackSize += insertStack.stackSize;
                if(inventory[sourceSlot].stackSize == 0) {
                    setInventorySlotContents(sourceSlot, null);
                }
                return true;
            }
            return false;
        }
    }

    private void pullItem(IInventory sourceInventory) {
        if(sourceInventory instanceof ISidedInventory) {
            ISidedInventory sidedInventory = (ISidedInventory) sourceInventory;
            for(int i : sidedInventory.getSlotsForFace(EnumFacing.DOWN)) {
                if(sidedInventory.canExtractItem(i, sidedInventory.getStackInSlot(i), EnumFacing.DOWN)) {
                    if(pullItem(sourceInventory, i)) {
                        return;
                    }
                }
            }
        } else {
            for(int i = 0; i < sourceInventory.getSizeInventory(); i++) {
                if(pullItem(sourceInventory, i)) {
                    return;
                }
            }
        }
    }

    private boolean pullItem(IInventory sourceInventory, int sourceSlot) {
        ItemStack itemStack = sourceInventory.getStackInSlot(sourceSlot);
        if(itemStack == null) {
            return false;
        }
        int firstEmptySlot = -1;
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] != null) {
                int spaceLeft = inventory[i].getMaxStackSize() - inventory[i].stackSize;
                if(spaceLeft > 0 && ItemUtils.canMergeItems(itemStack, inventory[i])) {
                    ItemStack insertStack = itemStack.splitStack(Math.min(spaceLeft, itemStack.stackSize));
                    inventory[i].stackSize += insertStack.stackSize;
                    if(itemStack.stackSize <= 0) {
                        sourceInventory.setInventorySlotContents(sourceSlot, null);
                        return true;
                    }
                }
            } else if(firstEmptySlot == -1) {
                firstEmptySlot = i;
            }
        }
        if(firstEmptySlot != -1 && isItemValidForSlot(firstEmptySlot, itemStack)) {
            sourceInventory.setInventorySlotContents(sourceSlot, null);
            setInventorySlotContents(firstEmptySlot, itemStack);
            return true;
        }
        return false;
    }

    public boolean pullItem(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getEntityItem();
        int firstEmptySlot = -1;
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] != null) {
                int spaceLeft = inventory[i].getMaxStackSize() - inventory[i].stackSize;
                if(spaceLeft > 0 && ItemUtils.canMergeItems(itemStack, inventory[i])) {
                    ItemStack insertStack = itemStack.splitStack(Math.min(spaceLeft, itemStack.stackSize));
                    inventory[i].stackSize += insertStack.stackSize;
                    if(itemStack.stackSize <= 0) {
                        entityItem.setDead();
                        return true;
                    }
                }
            } else if(firstEmptySlot == -1) {
                firstEmptySlot = i;
            }
        }
        if(firstEmptySlot != -1 && isItemValidForSlot(firstEmptySlot, itemStack)) {
            setInventorySlotContents(firstEmptySlot, itemStack);
            entityItem.setDead();
            return true;
        }
        return false;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (inventory[index] != null) {
            if (inventory[index].stackSize <= count) {
                ItemStack itemStack = inventory[index];
                inventory[index] = null;
                markDirty();
                return itemStack;
            } else {
                ItemStack itemStack = inventory[index].splitStack(count);
                if (inventory[index].stackSize == 0) {
                    inventory[index] = null;
                }
                markDirty();
                return itemStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = inventory[index];
        inventory[index] = null;
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        inventory[index] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagList tagList = new NBTTagList();
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(itemCompound);
            }
        }
        tagCompound.setTag("Items", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemCompound = (NBTTagCompound) tagList.get(i);
            inventory[itemCompound.getByte("Slot")] = ItemStack.loadItemStackFromNBT(itemCompound);
        }
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container." + RefinedRelocation2.MOD_ID + ":better_hopper";
    }

    @Override
    public IChatComponent getDisplayName() {
        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName());
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == RefinedRelocation2.HOPPER) {
            return (T) RefinedRelocation2.HOPPER.getDefaultInstance();
        }
        return null;
    }
}
