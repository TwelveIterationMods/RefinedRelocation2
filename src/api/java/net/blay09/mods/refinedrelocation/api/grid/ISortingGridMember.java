package net.blay09.mods.refinedrelocation.api.grid;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface ISortingGridMember {
	TileEntity getTileEntity();

	boolean isInvalid();

	void setSortingGrid(@Nullable ISortingGrid grid);
	@Nullable
	ISortingGrid getSortingGrid();

	/**
	 * Implementing tile entities MUST call this from onLoad
	 */
	void onLoad(TileEntity tileEntity);

	/**
	 * Implementing tile entities CAN call this from update, if they are tickable. Required for sorting inventories.
	 */
	void onUpdate(TileEntity tileEntity);

	/**
	 * Implementing tile entities MUST call this from invalidate() and onChunkUnload()
	 */
	void onInvalidate(TileEntity tileEntity);
}
