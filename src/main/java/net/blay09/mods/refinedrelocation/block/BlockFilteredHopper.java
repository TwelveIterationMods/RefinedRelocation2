package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.tile.TileFilteredHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFilteredHopper extends BlockFastHopper {

	public BlockFilteredHopper() {
		super("filtered_hopper");
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFilteredHopper();
	}

}
