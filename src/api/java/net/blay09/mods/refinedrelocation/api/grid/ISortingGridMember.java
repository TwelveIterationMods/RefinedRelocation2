package net.blay09.mods.refinedrelocation.api.grid;

import net.blay09.mods.refinedrelocation.util.GridContainer;
import net.minecraft.tileentity.TileEntity;

public interface ISortingGridMember {
	GridContainer getGridContainer();

	boolean isInvalid();

	void setSortingGrid(ISortingGrid grid);
	ISortingGrid getSortingGrid();

	void onUpdate(TileEntity tileEntity);
	void onInvalidate(TileEntity tileEntity);
}
