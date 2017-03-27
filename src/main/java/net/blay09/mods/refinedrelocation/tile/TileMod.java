package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.util.ItemHandlerHelper2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMod extends TileEntity {

	private boolean isFirstTick = true;

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

	public void dropItemHandlers() {
		IItemHandler itemHandler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(itemHandler != null) {
			ItemHandlerHelper2.dropItemHandlerItems(world, pos, itemHandler);
		}
	}

	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		return world.getTileEntity(pos) == this && entityPlayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
	}

	protected void baseUpdate() {
		if(isFirstTick) {
			onFirstTick();
			isFirstTick = false;
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getUnlocalizedName());
	}

	public String getUnlocalizedName() {
		return "container.refinedrelocation.unnamed";
	}

	protected void onFirstTick() {

	}
}
