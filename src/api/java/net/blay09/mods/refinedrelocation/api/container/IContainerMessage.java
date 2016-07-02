package net.blay09.mods.refinedrelocation.api.container;

import net.minecraft.nbt.NBTTagCompound;

public interface IContainerMessage {

	String getKey();
	int getIntValue();
	String getStringValue();
	NBTTagCompound getNBTValue();
	byte[] getByteArrayValue();

}
