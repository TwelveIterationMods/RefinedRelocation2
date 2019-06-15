package net.blay09.mods.refinedrelocation.api.container;

import net.minecraft.nbt.CompoundNBT;

public interface IContainerMessage {

    String getKey();

    default int getIndex() {
        return 0;
    }

    default int getIntValue() {
        return 0;
    }

    default String getStringValue() {
        return "";
    }

    default CompoundNBT getNBTValue() {
        return new CompoundNBT();
    }

    default byte[] getByteArrayValue() {
        return new byte[0];
    }

}
