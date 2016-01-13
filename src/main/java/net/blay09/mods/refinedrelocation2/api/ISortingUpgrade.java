package net.blay09.mods.refinedrelocation2.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface ISortingUpgrade {
    boolean applySortingUpgrade(EntityPlayer entityPlayer, World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ);
}
