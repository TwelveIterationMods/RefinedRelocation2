package net.blay09.mods.refinedrelocation.api.grid;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface ISortingGridMember {
	TileOrMultipart getTileEntity();

	boolean isInvalid();

	void setSortingGrid(ISortingGrid grid);
	@Nullable
	ISortingGrid getSortingGrid();

	void onUpdate(TileEntity tileEntity);
	void onInvalidate(TileEntity tileEntity);
}
