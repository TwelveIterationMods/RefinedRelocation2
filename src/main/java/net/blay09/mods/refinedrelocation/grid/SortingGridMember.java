package net.blay09.mods.refinedrelocation.grid;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SortingGridMember implements ISortingGridMember {

	private TileEntity tileEntity;
	private boolean isInvalid;
	private ISortingGrid sortingGrid;

	@Override
	public TileEntity getTileEntity() {
		return tileEntity;
	}

	@Override
	public boolean isInvalid() {
		return isInvalid;
	}

	@Override
	public void setSortingGrid(@Nullable ISortingGrid grid) {
		this.sortingGrid = grid;
	}

	@Override
	public ISortingGrid getSortingGrid() {
		return sortingGrid;
	}

	@Override
	public final void onInvalidate(TileEntity tileEntity) {
		onInvalidate();
		isInvalid = true;
		RefinedRelocationAPI.removeFromSortingGrid(this);
	}

	@Override
	public final void onUpdate(TileEntity tileEntity) {
		onUpdate();
	}

	@Override
	public final void onLoad(TileEntity tileEntity) {
		this.tileEntity = tileEntity;
		RefinedRelocationAPI.addToSortingGrid(this);
		onLoad();
	}

	protected void onLoad() {}
	protected void onInvalidate() {}
	protected void onUpdate() {}

	public final boolean isRemote() {
		World world = tileEntity.getWorld();
		return world != null && world.isRemote;
	}
}
