package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SameItemFilter implements IFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":same_item_filter";

    private boolean ignoreNBT = true;

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public boolean isFilterUsable(TileEntity tileEntity) {
        return tileEntity.getCapability(Capabilities.SORTING_INVENTORY).isPresent();
    }

    @Override
    public boolean passes(TileEntity tileEntity, ItemStack itemStack) {
        LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        return itemHandlerCap.map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack otherStack = itemHandler.getStackInSlot(i);
                if (!otherStack.isEmpty()) {
                    if (itemStack.getItem() != otherStack.getItem()) {
                        continue;
                    }

                    if (!ignoreNBT && !ItemStack.areItemStackTagsEqual(itemStack, otherStack)) {
                        continue;
                    }

                    return true;
                }
            }
            return true;
        }).orElse(false);
    }

    @Override
    public INBTBase serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("IgnoreNBT", ignoreNBT);
        return compound;
    }

    @Override
    public void deserializeNBT(INBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        ignoreNBT = compound.getBoolean("IgnoreNBT");
    }


    @Override
    public String getLangKey() {
        return "filter.refinedrelocation:same_item_filter";
    }

    @Override
    public String getDescriptionLangKey() {
        return "filter.refinedrelocation:same_item_filter.description";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IFilterIcon getFilterIcon() {
        return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_same_item_filter");
    }

}
