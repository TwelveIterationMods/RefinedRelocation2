package net.blay09.mods.refinedrelocation.api.grid;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface ISortingGridMember {
	TileEntity getTileEntity();

	boolean isInvalid();

	void setSortingGrid(@Nullable ISortingGrid grid);
	@Nullable
	ISortingGrid getSortingGrid();

	void onUpdate(TileEntity tileEntity);
	void onInvalidate(TileEntity tileEntity);
}
