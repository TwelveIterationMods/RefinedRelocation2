package net.blay09.mods.refinedrelocation.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileBuffer extends TileMod implements ITickable {

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {

		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

		return super.getCapability(capability, facing);
	}

	@Override
	public String getUnlocalizedName() {
		return "container.refinedrelocation:buffer";
	}

	@Override
	public void update() {

	}
}
