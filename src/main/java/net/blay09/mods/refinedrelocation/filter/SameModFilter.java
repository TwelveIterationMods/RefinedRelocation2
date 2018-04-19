package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SameModFilter implements IFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":same_mod_filter";

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public boolean isFilterUsable(TileEntity tileEntity) {
        return tileEntity.hasCapability(Capabilities.SORTING_INVENTORY, null);
    }

    @Override
    public boolean passes(TileEntity tileEntity, ItemStack itemStack) {
        IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (itemHandler != null) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack otherStack = itemHandler.getStackInSlot(i);
                if (!otherStack.isEmpty()) {
                    if (itemStack.getItem() == otherStack.getItem()) {
                        return true;
                    }

                    String modId = itemStack.getItem().getCreatorModId(itemStack);
                    String otherModId = otherStack.getItem().getCreatorModId(otherStack);
                    if (modId == null && otherModId == null) {
                        return true;
                    }

                    if (modId != null && modId.equals(otherModId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public NBTBase serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
    }


    @Override
    public String getLangKey() {
        return "filter.refinedrelocation:same_mod_filter";
    }

    @Override
    public String getDescriptionLangKey() {
        return "filter.refinedrelocation:same_mod_filter.description";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IFilterIcon getFilterIcon() {
        return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_same_mod_filter");
    }

}
