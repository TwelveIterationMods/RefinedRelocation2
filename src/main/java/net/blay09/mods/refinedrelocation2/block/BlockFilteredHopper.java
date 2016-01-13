package net.blay09.mods.refinedrelocation2.block;

import net.blay09.mods.refinedrelocation2.tile.TileFilteredHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFilteredHopper extends BlockBetterHopper {

    public BlockFilteredHopper() {
        super("filtered_hopper");
        setUnlocalizedName(getRegistryName());
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileFilteredHopper();
    }

}
