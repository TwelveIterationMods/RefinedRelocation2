package net.blay09.mods.refinedrelocation.util;

import mcmultipart.multipart.Multipart;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PartWrapper implements TileOrMultipart {

	private final Multipart part;

	public PartWrapper(Multipart part) {
		this.part = part;
	}

	@Override
	public boolean isMultipart() {
		return true;
	}

	@Override
	public TileEntity getTileEntity() {
		if(part.getContainer() instanceof TileEntity) {
			return (TileEntity) part.getContainer();
		}
		return part.getWorld().getTileEntity(part.getPos());
	}

	@Nullable
	@Override
	public Multipart getMultipart() {
		return part;
	}

	@Override
	public World getWorld() {
		return part.getWorld();
	}

	@Override
	public BlockPos getPos() {
		return part.getPos();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return part.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return part.getCapability(capability, facing);
	}

}
