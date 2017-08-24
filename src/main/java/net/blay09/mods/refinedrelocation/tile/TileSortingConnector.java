package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileSortingConnector extends TileEntity {

	private final ISortingGridMember sortingGridMember = Capabilities.getDefaultInstance(Capabilities.SORTING_GRID_MEMBER);

	@Override
	public void onLoad() {
		super.onLoad();
		sortingGridMember.onLoad(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		sortingGridMember.onInvalidate(this);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		sortingGridMember.onInvalidate(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.SORTING_GRID_MEMBER || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == Capabilities.SORTING_GRID_MEMBER) {
			return Capabilities.SORTING_GRID_MEMBER.cast(sortingGridMember);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

}
