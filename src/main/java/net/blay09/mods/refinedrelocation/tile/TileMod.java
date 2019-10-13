package net.blay09.mods.refinedrelocation.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TileMod extends TileEntity {

    private boolean isFirstTick = true;

    public TileMod(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        readFromNBTSynced(compound);
    }

    public void readFromNBTSynced(CompoundNBT compound) {
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        writeToNBTSynced(compound);
        return compound;
    }

    public CompoundNBT writeToNBTSynced(CompoundNBT compound) {
        return compound;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return writeToNBTSynced(super.write(new CompoundNBT()));
    }

    @Override
    public void handleUpdateTag(CompoundNBT compound) {
        readFromNBTSynced(compound);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, writeToNBTSynced(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        readFromNBTSynced(pkt.getNbtCompound());
    }

    protected void baseTick() {
        if (isFirstTick) {
            onFirstTick();
            isFirstTick = false;
        }
    }

    public String getUnlocalizedName() {
        return "container.refinedrelocation.unnamed";
    }

    protected void onFirstTick() {

    }
}
