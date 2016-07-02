package net.blay09.mods.refinedrelocation.api;

import mcmultipart.multipart.Multipart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public interface TileOrMultipart {
	boolean isMultipart();
	@Nullable
	TileEntity getTileEntity();
	@Nullable
	Multipart getMultipart();
	World getWorld();
	BlockPos getPos();
	boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing);
	<T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing);
}
