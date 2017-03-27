package net.blay09.mods.refinedrelocation.api.container;

import net.minecraft.nbt.NBTTagCompound;

public interface IContainerMessage {

	String getKey();
	int getIntValue();
	int getSecondaryIntValue();
	String getStringValue();
	NBTTagCompound getNBTValue();
	byte[] getByteArrayValue();

}
