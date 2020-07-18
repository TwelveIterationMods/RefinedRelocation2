package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
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
        return tileEntity.getCapability(Capabilities.SORTING_INVENTORY).isPresent();
    }

    @Override
    public boolean passes(TileEntity tileEntity, ItemStack itemStack, ItemStack originalStack) {
        LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        return itemHandlerCap.map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack otherStack = itemHandler.getStackInSlot(i);
                if (!otherStack.isEmpty()) {
                    if (otherStack == originalStack || itemStack.getItem() == otherStack.getItem()) {
                        return true;
                    }

                    String modId = itemStack.getItem().getCreatorModId(itemStack);
                    String otherModId = otherStack.getItem().getCreatorModId(otherStack);
                    if (modId == null || otherModId == null || "minecraft".equals(modId) || "minecraft".equals(otherModId)) {
                        return false;
                    }

                    if (modId.equals(otherModId)) {
                        return true;
                    }
                }
            }

            return false;
        }).orElse(false);
    }

    @Override
    public INBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(INBT nbt) {
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
    @OnlyIn(Dist.CLIENT)
    public IDrawable getFilterIcon() {
        return GuiTextures.SAME_MOD_FILTER_ICON;
    }

}
