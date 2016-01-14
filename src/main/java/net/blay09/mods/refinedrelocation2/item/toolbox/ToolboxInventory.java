package net.blay09.mods.refinedrelocation2.item.toolbox;

import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

public class ToolboxInventory implements IInventory {

    private final EntityPlayer entityPlayer;
    private final ItemStack[] inventory = new ItemStack[5];

    public ToolboxInventory(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
        ItemStack parentStack = entityPlayer.getHeldItem();
        if (!parentStack.hasTagCompound()) {
            parentStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagList tagList = parentStack.getTagCompound().getTagList("ItemList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
            int slot = itemCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
            }
        }
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
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        inventory[index] = itemStack;
        markDirty();
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        if(itemStack.getItemUseAction() != EnumAction.NONE) {
            return false;
        }
        for(Item item : ItemToolbox.itemBlacklist) {
            if(itemStack.getItem() == item) {
                return false;
            }
        }
        if(RefinedRelocation2.proxy.isTESRItem(itemStack)) {
           return false;
        }
//            return toolboxRegistry.contains(itemStack.getItem());
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() == ModItems.toolbox;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {}

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        if(!isUseableByPlayer(entityPlayer)) {
            return;
        }
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(itemCompound);
                tagList.appendTag(itemCompound);
            }
        }
        ItemStack parentStack = entityPlayer.getHeldItem();
        parentStack.getTagCompound().setTag("ItemList", tagList);
    }

    @Override
    public void clear() {}

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public String getName() {
        return "container." + RefinedRelocation2.MOD_ID + ":toolbox";
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(getName());
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }
}
