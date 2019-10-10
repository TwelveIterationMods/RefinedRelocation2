package net.blay09.mods.refinedrelocation.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public abstract class FilterContainer extends BaseContainer {
    protected FilterContainer(@Nullable ContainerType<?> type, int windowId) {
        super(type, windowId);
    }

    public abstract TileEntity getTileEntity();
}
