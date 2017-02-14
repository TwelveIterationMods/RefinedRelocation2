package net.blay09.mods.refinedrelocation.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public interface TileOrMultipart {
	boolean isMultipart();
	TileEntity getTileEntity();
//	@Nullable
//	Multipart getMultipart();
	World getWorld();
	BlockPos getPos();
	boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing);
	@Nullable
	<T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing);
	void markDirty();
	String getDisplayName();
}
