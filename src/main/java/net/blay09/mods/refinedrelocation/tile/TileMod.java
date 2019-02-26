package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.util.ItemUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class TileMod extends TileEntity {

    private boolean isFirstTick = true;

    public TileMod(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        readFromNBTSynced(compound);
    }

    public void readFromNBTSynced(NBTTagCompound compound) {
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        writeToNBTSynced(compound);
        return compound;
    }

    public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBTSynced(super.write(new NBTTagCompound()));
    }

    @Override
    public void handleUpdateTag(NBTTagCompound compound) {
        readFromNBTSynced(compound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, writeToNBTSynced(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBTSynced(pkt.getNbtCompound());
    }

    protected void baseTick() {
        if (isFirstTick) {
            onFirstTick();
            isFirstTick = false;
        }
    }

    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getUnlocalizedName());
    }

    public String getUnlocalizedName() {
        return "container.refinedrelocation.unnamed";
    }

    protected void onFirstTick() {

    }
}
