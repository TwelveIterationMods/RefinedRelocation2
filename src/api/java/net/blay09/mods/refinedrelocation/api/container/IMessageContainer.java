package net.blay09.mods.refinedrelocation.api.container;

import net.minecraft.nbt.NBTTagCompound;

public interface IMessageContainer {

	String getKey();
	int getIntValue();
	String getStringValue();
	NBTTagCompound getNBTValue();

}
