package net.blay09.mods.refinedrelocation.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;

public class RenderUtils {

	public static float getFacingAngle(IBlockState state) {
		return getFacingAngle(state.get(BlockStateProperties.FACING));
	}

	public static float getFacingAngle(EnumFacing facing) {
		switch(facing) {
			case NORTH: return 180f;
			case EAST: return -90f;
			case SOUTH: return 0f;
			case WEST: return 90f;
		}
		return 0f;
	}

}
