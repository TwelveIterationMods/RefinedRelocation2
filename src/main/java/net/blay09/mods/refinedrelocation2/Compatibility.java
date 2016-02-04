package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.block.BlockBetterHopper;
import net.blay09.mods.refinedrelocation2.tile.TileBetterHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class Compatibility {

    public static void postInit() {
        MinecraftForge.EVENT_BUS.register(new Compatibility());
    }

    public static boolean canTileInsert(TileEntity tileEntity, EnumFacing facing) {
        World world = tileEntity.getWorld();
        BlockPos pos = tileEntity.getPos();
        IBlockState blockState = world.getBlockState(pos);
        if(blockState.getBlock() == Blocks.hopper) {
            return facing == blockState.getValue(BlockHopper.FACING);
        }
        if(blockState.getBlock() instanceof BlockBetterHopper) {
            return facing == blockState.getValue(BlockBetterHopper.FACING);
        }
        return false;
    }

}
