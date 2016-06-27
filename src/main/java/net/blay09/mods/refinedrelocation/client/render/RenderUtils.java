package net.blay09.mods.refinedrelocation.client.render;

import net.blay09.mods.refinedrelocation.block.BlockMod;
import net.minecraft.block.state.IBlockState;

public class RenderUtils {

	public static float getFacingAngle(IBlockState state) {
		switch(state.getValue(BlockMod.FACING)) {
			case NORTH: return 0f;
			case EAST: return 90f;
			case SOUTH: return 180f;
			case WEST: return -90f;
		}
		return 0f;
	}

}
