package net.blay09.mods.refinedrelocation.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class RenderUtils {

    public static float getHorizontalFacingAngle(BlockState state) {
        return getFacingAngle(state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

    public static float getFacingAngle(BlockState state) {
        return getFacingAngle(state.get(BlockStateProperties.FACING));
    }

    public static float getFacingAngle(Direction facing) {
        switch (facing) {
            case NORTH:
                return 180f;
            case EAST:
                return -90f;
            case SOUTH:
                return 0f;
            case WEST:
                return 90f;
        }
        return 0f;
    }

}
