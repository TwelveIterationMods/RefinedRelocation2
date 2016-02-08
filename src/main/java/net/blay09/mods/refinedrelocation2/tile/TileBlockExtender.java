package net.blay09.mods.refinedrelocation2.tile;

import net.blay09.mods.refinedrelocation2.Compatibility;
import net.blay09.mods.refinedrelocation2.ModBlocks;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.block.BlockExtender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;

public class TileBlockExtender extends TileEntity implements ITickable {

    private TileEntity connectedTileEntity;
    private EnumFacing facing;

    private boolean firstTick = true;

    @Override
    public void update() {
        if(firstTick) {
            facing = worldObj.getBlockState(getPos()).getValue(BlockExtender.FACING);
            connectedTileEntity = worldObj.getTileEntity(getPos().offset(facing));
            firstTick = false;
        }
    }

    public EnumFacing getIOSide(EnumFacing side) {
        return facing.getOpposite();
    }

    public boolean hasVisibleConnection(EnumFacing side) {
        if (side == facing) {
            return false;
        }
        BlockPos sidePos = getPos().offset(side);
        IBlockState blockState = worldObj.getBlockState(sidePos);
//        if (blockState.getBlock().canProvidePower()) {
//            return true;
//        }
        TileEntity tileEntity = worldObj.getTileEntity(sidePos);
        return tileEntity != null && Compatibility.canTileInsert(tileEntity, side.getOpposite());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(connectedTileEntity != null) {
            if(capability == RefinedRelocation2.ITEM_HANDLER) {
                return connectedTileEntity.hasCapability(capability, facing);
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(connectedTileEntity != null) {
            if (capability == RefinedRelocation2.ITEM_HANDLER) {
                return connectedTileEntity.getCapability(capability, getIOSide(this.facing));
            }
        }
        return super.getCapability(capability, facing);
    }
}
