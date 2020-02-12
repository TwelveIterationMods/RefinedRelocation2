package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.tile.TileSortingConnector;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SortingConnectorBlock extends ContainerBlock {

    public static final String name = "sorting_connector";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    private static Map<BlockPos, BlockState> boundingBoxCache = new HashMap<>();
    private static final VoxelShape CENTER_SHAPE = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
    private static final VoxelShape[] FACING_SHAPES = new VoxelShape[]{
            Block.makeCuboidShape(5, 0, 5, 11, 5, 11),         // Down
            Block.makeCuboidShape(5, 11, 5, 11, 16, 11),         // Up
            Block.makeCuboidShape(5, 5, 0, 11, 11, 5),         // North
            Block.makeCuboidShape(5, 5, 11, 11, 11, 16),         // South
            Block.makeCuboidShape(0, 5, 5, 5, 11, 11),         // West
            Block.makeCuboidShape(11, 5, 5, 16, 11, 11)          // East
    };

    private static final BooleanProperty DOWN = BooleanProperty.create("down");
    private static final BooleanProperty UP = BooleanProperty.create("up");
    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty WEST = BooleanProperty.create("west");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty CORNER = BooleanProperty.create("corner");
    public static final BooleanProperty[] CONNECTIONS = new BooleanProperty[]{
            DOWN,
            UP,
            NORTH,
            SOUTH,
            WEST,
            EAST
    };

    public SortingConnectorBlock() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(0.3f));

        BlockState state = getDefaultState();
        for (BooleanProperty property : CONNECTIONS) {
            state = state.with(property, false);
        }
        setDefaultState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        BlockState actualState = boundingBoxCache.computeIfAbsent(pos, it -> getActualState(state, world, it));
        VoxelShape boundingBox = CENTER_SHAPE;
        for (int i = 0; i < CONNECTIONS.length; i++) {
            BooleanProperty property = CONNECTIONS[i];
            if (actualState.get(property)) {
                boundingBox = VoxelShapes.or(boundingBox, FACING_SHAPES[i]);
            }
        }

        return boundingBox;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST, CORNER);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean what) {
        super.onReplaced(state, world, pos, newState, what);
        boundingBoxCache.remove(pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean wat) {
        super.neighborChanged(state, world, pos, block, fromPos, wat);
        BlockState newState = getConnectionState(world, pos, state);
        if (newState != state) {
            world.setBlockState(pos, newState, 3);
        }
    }

    public BlockState getActualState(BlockState state, IBlockReader world, BlockPos pos) {
        return getConnectionState(world, pos, state);
    }

    private BlockState getConnectionState(IBlockReader world, BlockPos pos, BlockState state) {
        Direction.Axis axis = null;
        boolean isCorner = false;
        int connectionCount = 0;
        for (Direction facing : Direction.values()) {
            BlockPos neighbourPos = pos.offset(facing);
            TileEntity tileEntity = world.getTileEntity(neighbourPos);
            if (tileEntity != null && tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, facing.getOpposite()).isPresent()) {
                state = state.with(CONNECTIONS[facing.getIndex()], true);
                if (axis != null && axis != facing.getAxis()) {
                    isCorner = true;
                }
                axis = facing.getAxis();
                connectionCount++;
            } else {
                state = state.with(CONNECTIONS[facing.getIndex()], false);
            }
        }

        if (connectionCount == 1) {
            isCorner = true;
        }

        return state.with(CORNER, isCorner);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileSortingConnector();
    }
}
