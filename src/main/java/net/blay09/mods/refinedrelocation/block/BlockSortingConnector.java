package net.blay09.mods.refinedrelocation.block;

import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.tile.TileSortingConnector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockSortingConnector extends BlockContainer {

    public static final String name = "sorting_connector";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    private static Map<BlockPos, IBlockState> boundingBoxCache = Maps.newHashMap();
    private static final VoxelShape CENTER_SHAPE = Block.makeCuboidShape(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);
    private static final VoxelShape[] FACING_SHAPES = new VoxelShape[]{
            Block.makeCuboidShape(0.3125, 0, 0.3125, 0.6875, 0.3125, 0.6875),         // Down
            Block.makeCuboidShape(0.3125, 0.6875, 0.3125, 0.6875, 1, 0.6875),         // Up
            Block.makeCuboidShape(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.3125),         // North
            Block.makeCuboidShape(0.3125, 0.3125, 0.6875, 0.6875, 0.6875, 1),         // South
            Block.makeCuboidShape(0, 0.3125, 0.3125, 0.3125, 0.6875, 0.6875),         // West
            Block.makeCuboidShape(0.6875, 0.3125, 0.3125, 1, 0.6875, 0.6875)          // East
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

    public BlockSortingConnector() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(0.3f));

        IBlockState state = getDefaultState();
        for (BooleanProperty property : CONNECTIONS) {
            state = state.with(property, false);
        }
        setDefaultState(state);
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
        IBlockState actualState = boundingBoxCache.computeIfAbsent(pos, it -> getActualState(state, world, it));
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
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST, CORNER);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    // TODO opaqueCube false

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean what) {
        super.onReplaced(state, world, pos, newState, what);
        boundingBoxCache.remove(pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        IBlockState newState = getConnectionState(world, pos, state);
        if (newState != state) {
            world.setBlockState(pos, newState, 3);
        }
    }

    public IBlockState getActualState(IBlockState state, IBlockReader world, BlockPos pos) {
        IBlockState actualState = getConnectionState(world, pos, state);
        boundingBoxCache.put(pos, actualState);
        return actualState;
    }

    private IBlockState getConnectionState(IBlockReader world, BlockPos pos, IBlockState state) {
        EnumFacing.Axis axis = null;
        boolean isCorner = false;
        int connectionCount = 0;
        for (EnumFacing facing : EnumFacing.values()) {
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
