package net.blay09.mods.refinedrelocation.api.grid;

import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface ISortingInventory extends ISortingGridMember, INBTSerializable<CompoundNBT> {

    LazyOptional<IItemHandler> getItemHandler();

    LazyOptional<? extends ISimpleFilter> getFilter();

    void setPriority(int priority);

    int getPriority();

    void onSlotChanged(int slotIndex);

}
