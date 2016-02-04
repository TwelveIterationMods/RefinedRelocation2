package net.blay09.mods.refinedrelocation2.tile;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.container.ContainerSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class TileSortingChest extends TileEntityLockable implements ITickable, IInventory, IWorldPos {

    private ISortingInventory sortingInventory;

    private ItemStack[] inventory = new ItemStack[27];
    public float lidAngle;
    public float prevLidAngle;
    private int numPlayersUsing;
    private int ticksSinceSync;
    private String customName;

    public TileSortingChest() {
        sortingInventory = RefinedRelocationAPI.createSortingInventory(this, this, true);
    }

    @Override
    public int getSizeInventory() {
        return 27;
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
        sortingInventory.slotChanged(index);
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container." + RefinedRelocation2.MOD_ID + ":sorting_chest";
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            int slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING)) {
            customName = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        compound.setTag("Items", tagList);
        if (hasCustomName()) {
            compound.setString("CustomName", customName);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d) <= 64d;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (numPlayersUsing < 0) {
                numPlayersUsing = 0;
            }
            numPlayersUsing++;
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            numPlayersUsing--;
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        if (id == 1) {
            numPlayersUsing = value;
            return true;
        }
        return super.receiveClientEvent(id, value);
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
    public void invalidate() {
        super.invalidate();
        sortingInventory.invalidate();
    }

    @Override
    public String getGuiID() {
        return null;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
    }

    private boolean firstTick = true;
    @Override
    public void update() {
        // onLoad is *NOT* a viable replacement for firstTick stuff, as it is called before the chunk is fully loaded, meaning that any world access will result in infinite-loading of the same chunk
        if(firstTick) {
            sortingInventory.onFirstTick();
            firstTick = false;
        }

        ticksSinceSync++;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (!worldObj.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = 0;
            float range = 5f;
            worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - range, y - range, z - range, x + 1 + range, y + 1 + range, z + 1 + range)).stream().filter(entityPlayer -> entityPlayer.openContainer instanceof ContainerSortingChest).forEach(entityPlayer -> {
                IInventory inventory = ((ContainerSortingChest) entityPlayer.openContainer).getChestInventory();
                if (inventory == this) {
                    numPlayersUsing++;
                }
            });
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }

        prevLidAngle = lidAngle;
        float lidAngleSpeed = 0.1f;
        if (numPlayersUsing > 0 && lidAngle == 0f) {
            worldObj.playSoundEffect(x + 0.5d, y + 0.5d, z + 0.5d, "random.chestopen", 0.5f, worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            float oldLidAngle = lidAngle;
            if (numPlayersUsing > 0) {
                lidAngle += lidAngleSpeed;
            } else {
                lidAngle -= lidAngleSpeed;
            }

            if (lidAngle > 1f) {
                lidAngle = 1f;
            }

            float closeSoundAngle = 0.5f;
            if (lidAngle < closeSoundAngle && oldLidAngle >= closeSoundAngle) {
                worldObj.playSoundEffect(x + 0.5d, y + 0.5d, z + 0.5d, "random.chestclosed", 0.5f, worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }

            if (lidAngle < 0f) {
                lidAngle = 0f;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == RefinedRelocation2.SORTING_INVENTORY || capability == RefinedRelocation2.SORTING_GRID_MEMBER) {
            return (T) sortingInventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }
}
