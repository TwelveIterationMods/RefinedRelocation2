package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.tile.TileMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockModTile extends BlockMod {

    public BlockModTile(Material material) {
        super(material);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileMod) {
            ((TileMod) tileEntity).dropItemHandlers();
            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
        super.eventReceived(state, world, pos, id, param);
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null && tileEntity.receiveClientEvent(id, param);
    }

}
