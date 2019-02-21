package net.blay09.mods.refinedrelocation.api.grid;

import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public interface ISortingInventory extends ISortingGridMember, INBTSerializable<NBTTagCompound> {

	LazyOptional<IItemHandler> getItemHandler();
	LazyOptional<? extends ISimpleFilter> getFilter();

	void setPriority(int priority);
	int getPriority();

	void onSlotChanged(int slotIndex);

}
