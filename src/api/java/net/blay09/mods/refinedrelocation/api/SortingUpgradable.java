package net.blay09.mods.refinedrelocation.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SortingUpgradable implements ISortingUpgradable {
    @Override
    public boolean applySortingUpgrade(TileEntity tileEntity, ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction side, double hitX, double hitY, double hitZ, Hand hand) {
        return false;
    }
}
