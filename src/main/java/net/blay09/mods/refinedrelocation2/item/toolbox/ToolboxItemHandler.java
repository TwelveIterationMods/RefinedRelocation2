package net.blay09.mods.refinedrelocation2.item.toolbox;

import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class ToolboxItemHandler extends ItemStackHandler {

    public ToolboxItemHandler(ItemStack parentStack) {
        super(5);
        if (!parentStack.hasTagCompound()) {
            parentStack.setTagCompound(new NBTTagCompound());
        }
        RefinedRelocation2.ITEM_HANDLER.getStorage().readNBT(RefinedRelocation2.ITEM_HANDLER, this, null, parentStack.getTagCompound().getTagList("ItemHandler", Constants.NBT.TAG_COMPOUND));
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
        if(!isItemValid(itemStack)) {
            return itemStack;
        }
        return super.insertItem(slot, itemStack, simulate);
    }

    public boolean isItemValid(ItemStack itemStack) {
        if(itemStack.getItemUseAction() != EnumAction.NONE) {
            return false;
        }
        for(Item item : ItemToolbox.itemBlacklist) {
            if(itemStack.getItem() == item) {
                return false;
            }
        }
        return !RefinedRelocation2.proxy.isTESRItem(itemStack);
    }

    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() == ModItems.toolbox;
    }

    public String getName() {
        return "container." + RefinedRelocation2.MOD_ID + ":toolbox";
    }

    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(getName());
    }

    public void closeInventory(EntityPlayer entityPlayer) {
        if(!isUseableByPlayer(entityPlayer)) {
            return;
        }
        NBTBase itemHandlerNBT = RefinedRelocation2.ITEM_HANDLER.getStorage().writeNBT(RefinedRelocation2.ITEM_HANDLER, this, null);
        if(itemHandlerNBT != null) {
            ItemStack parentStack = entityPlayer.getHeldItem();
            if(!parentStack.hasTagCompound()) {
                parentStack.setTagCompound(new NBTTagCompound());
            }
            parentStack.getTagCompound().setTag("ItemHandler", itemHandlerNBT);
        }
    }
}
