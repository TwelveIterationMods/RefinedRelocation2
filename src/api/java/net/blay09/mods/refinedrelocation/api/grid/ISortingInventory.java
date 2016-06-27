package net.blay09.mods.refinedrelocation.api.grid;

import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public interface ISortingInventory extends ISortingGridMember, INBTSerializable<NBTTagCompound> {

	IItemHandler getItemHandler();
	ISimpleFilter getFilter();

	void setPriority(int priority);
	int getPriority();

	void onSlotChanged(int slotIndex);

}
