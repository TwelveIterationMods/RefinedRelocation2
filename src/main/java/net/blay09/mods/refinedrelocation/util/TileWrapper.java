package net.blay09.mods.refinedrelocation.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileWrapper {

	private final TileEntity tileEntity;

	public TileWrapper(TileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	public TileEntity getTileEntity() {
		return tileEntity;
	}

	public World getWorld() {
		return tileEntity.getWorld();
	}

	public BlockPos getPos() {
		return tileEntity.getPos();
	}

	public boolean isMultipart() {
		return false;
	}

	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return tileEntity.hasCapability(capability, facing);
	}

	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return tileEntity.getCapability(capability, facing);
	}

}
