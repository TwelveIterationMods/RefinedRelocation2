package net.blay09.mods.refinedrelocation2.api.grid;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IWorldPos {
    World getWorld();
    BlockPos getPos();
}
