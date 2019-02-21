package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.tile.TileFilteredHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFilteredHopper extends BlockFastHopper {

	public static final String name = "filtered_hopper";
	public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFilteredHopper();
	}

}
