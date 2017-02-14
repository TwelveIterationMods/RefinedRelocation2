package net.blay09.mods.refinedrelocation.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileBlockExtender extends TileMod implements ITickable {

	private TileEntity connectedTile;

	private boolean isFirstTick = true;

	public boolean hasVisibleConnection(EnumFacing side) {
		if(side == getFacing()) {
			return false;
		}
		BlockPos sidePos = pos.offset(side);
		TileEntity tileEntity = world.getTileEntity(sidePos);
		return tileEntity != null && isCompatibleTile(tileEntity, side.getOpposite());
	}

	private boolean isCompatibleTile(TileEntity tileEntity, EnumFacing side) {
		return tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
	}

	@Override
	public void update() {
		if(isFirstTick) {
			connectedTile = world.getTileEntity(pos.offset(getFacing()));
			isFirstTick = false;
		}
	}

	@Nullable
	private EnumFacing getIOSide(@Nullable EnumFacing from) {
		return getFacing().getOpposite();
	}

	private EnumFacing getFacing() {
		return EnumFacing.getFront(getBlockMetadata());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(connectedTile != null) {
			return connectedTile.hasCapability(capability, getIOSide(facing));
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(connectedTile != null) {
			return connectedTile.getCapability(capability, getIOSide(facing));
		}
		return super.getCapability(capability, facing);
	}
}
