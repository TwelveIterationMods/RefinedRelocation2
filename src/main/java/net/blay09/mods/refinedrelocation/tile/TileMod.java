package net.blay09.mods.refinedrelocation.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class TileMod extends TileEntity {

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readFromNBTSynced(compound);
	}

	public void readFromNBTSynced(NBTTagCompound compound) {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeToNBTSynced(compound);
		return compound;
	}

	public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBTSynced(super.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void handleUpdateTag(NBTTagCompound compound) {
		readFromNBTSynced(compound);
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), writeToNBTSynced(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBTSynced(pkt.getNbtCompound());
	}

}
