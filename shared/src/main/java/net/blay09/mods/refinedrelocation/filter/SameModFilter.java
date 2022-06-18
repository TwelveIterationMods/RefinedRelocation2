package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SameModFilter implements IFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":same_mod_filter";

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public boolean isFilterUsable(BlockEntity blockEntity) {
        return blockEntity.getCapability(Capabilities.SORTING_INVENTORY).isPresent();
    }

    @Override
    public boolean passes(BlockEntity blockEntity, ItemStack itemStack, ItemStack originalStack) {
        LazyOptional<IItemHandler> itemHandlerCap = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
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
