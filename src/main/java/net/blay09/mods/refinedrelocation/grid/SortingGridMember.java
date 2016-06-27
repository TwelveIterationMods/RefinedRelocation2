package net.blay09.mods.refinedrelocation.grid;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.util.GridContainer;
import net.minecraft.tileentity.TileEntity;

public class SortingGridMember implements ISortingGridMember {

	private GridContainer gridContainer;
	private boolean isInvalid;
	private ISortingGrid sortingGrid;

	private boolean isFirstTick = true;

	@Override
	public GridContainer getGridContainer() {
		return gridContainer;
	}

	@Override
	public boolean isInvalid() {
		return isInvalid;
	}

	@Override
	public void setSortingGrid(ISortingGrid grid) {
		this.sortingGrid = grid;
	}

	@Override
	public ISortingGrid getSortingGrid() {
		return sortingGrid;
	}

	@Override
	public final void onInvalidate(TileEntity tileEntity) {
		onInvalidate();
	}

	@Override
	public final void onUpdate(TileEntity tileEntity) {
		if(isFirstTick) {
			gridContainer = new GridContainer(tileEntity);
			onFirstTick();
			isFirstTick = false;
		}
		onUpdate();
	}

	protected void onFirstTick() {
		RefinedRelocationAPI.addToSortingGrid(this);
	}

	protected void onInvalidate() {
		isInvalid = true;
		RefinedRelocationAPI.removeFromSortingGrid(this);
	}

	protected void onUpdate() {

	}
}
