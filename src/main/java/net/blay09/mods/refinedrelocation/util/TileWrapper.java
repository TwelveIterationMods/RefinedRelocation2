package net.blay09.mods.refinedrelocation.util;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileWrapper implements TileOrMultipart {

	private final TileEntity tileEntity;

	public TileWrapper(TileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public TileEntity getTileEntity() {
		return tileEntity;
	}

//	@Nullable
//	@Override
//	public Multipart getMultipart() {
//		return null;  // @McMultipart
//	}

	@Override
	public World getWorld() {
		return tileEntity.getWorld();
	}

	@Override
	public BlockPos getPos() {
		return tileEntity.getPos();
	}

	@Override
	public boolean isMultipart() {
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return tileEntity.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return tileEntity.getCapability(capability, facing);
	}

	@Override
	public void markDirty() {
		tileEntity.markDirty();
	}

	@Override
	public String getDisplayName() {
		ITextComponent displayName = tileEntity.getDisplayName();
		return displayName != null ? displayName.getUnformattedText() : "";
	}
}
