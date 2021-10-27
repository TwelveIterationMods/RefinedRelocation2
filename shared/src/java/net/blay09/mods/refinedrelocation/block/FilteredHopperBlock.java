package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.block.entity.FilteredHopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FilteredHopperBlock extends FastHopperBlock {

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FilteredHopperBlockEntity(pos, state);
    }

}
