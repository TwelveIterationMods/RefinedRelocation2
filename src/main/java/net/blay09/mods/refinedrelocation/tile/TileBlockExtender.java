package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileBlockExtender extends TileMod implements ITickable {

	private TileEntity connectedTile;

	private final EnumFacing[] sideMappings = new EnumFacing[5];

	private final EnumFacing[] cachedFacingToFacingMappings = new EnumFacing[6];

	@Nullable
	public EnumFacing getSideMapping(RelativeSide side) {
		return sideMappings[side.ordinal()];
	}

	public void setSideMapping(RelativeSide side, @Nullable EnumFacing facing) {
		sideMappings[side.ordinal()] = facing;
		cachedFacingToFacingMappings[side.toFacing(getFacing()).ordinal()] = facing;
		markDirty();
	}

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
	protected void onFirstTick() {
		connectedTile = world.getTileEntity(pos.offset(getFacing()));
	}

	@Nullable
	public EnumFacing getSideMapping(@Nullable EnumFacing facing) {
		if(facing == null) {
			return getFacing().getOpposite();
		}
		return cachedFacingToFacingMappings[facing.ordinal()];
	}

	public EnumFacing getFacing() {
		return EnumFacing.getFront(getBlockMetadata());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		byte[] mappings = new byte[5];
		for(int i = 0; i < sideMappings.length; i++) {
			mappings[i] = sideMappings[i] != null ? -1 : (byte) sideMappings[i].getIndex();
		}
		compound.setByteArray("SideMappings", mappings);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		byte[] mappings = compound.getByteArray("SideMappings");
		if(mappings.length == 5) {
			for(int i = 0; i < mappings.length; i++) {
				if(mappings[i] != -1) {
					sideMappings[i] = EnumFacing.getFront(mappings[i]);
				}
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(connectedTile != null) {
			EnumFacing ioSide = getSideMapping(facing);
			if(ioSide != null) {
				return connectedTile.hasCapability(capability, ioSide);
			}
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(connectedTile != null) {
			EnumFacing ioSide = getSideMapping(facing);
			if(ioSide != null) {
				return connectedTile.getCapability(capability, ioSide);
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public String getUnlocalizedName() {
		return "container.refinedrelocation:block_extender";
	}
}
