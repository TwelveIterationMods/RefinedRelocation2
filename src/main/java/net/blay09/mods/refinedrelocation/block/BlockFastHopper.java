package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.RefinedRelocationUtils;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockFastHopper extends BlockContainer {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", facing -> facing != EnumFacing.UP);

    public static final String name = "fast_hopper";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    private static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    private static final VoxelShape INPUT_SHAPE = Block.makeCuboidShape(0.0625f, 0f, 0.0625f, 0.9375f, 1f, 0.9375f);
    private static final VoxelShape MIDDLE_SHAPE = Block.makeCuboidShape(0.0625, 0.625, 0.0625, 0.9375, 1, 0.9375);
    private static final VoxelShape INSIDE_BOWL_SHAPE = Block.makeCuboidShape(0.25, 0.25, 0.25, 0.75, 0.625, 0.75);
    private static final VoxelShape INPUT_MIDDLE_SHAPE = VoxelShapes.or(MIDDLE_SHAPE, INPUT_SHAPE);
    private static final VoxelShape HOPPER_BASE = VoxelShapes.combineAndSimplify(INPUT_MIDDLE_SHAPE, INSIDE_BOWL_SHAPE, IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape DOWN_SHAPE = VoxelShapes.or(HOPPER_BASE, Block.makeCuboidShape(0.375, 0, 0.375, 0.625, 0.25, 0.625));
    private static final VoxelShape EAST_SHAPE = VoxelShapes.or(HOPPER_BASE, Block.makeCuboidShape(0.75, 0.25, 0.375, 1, 0.5, 0.625));
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(HOPPER_BASE, Block.makeCuboidShape(0.375, 0.25, 0, 0.625, 0.5, 0.25));
    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(HOPPER_BASE, Block.makeCuboidShape(0.375, 0.25, 0.75, 0.625, 0.5, 1));
    private static final VoxelShape WEST_SHAPE = VoxelShapes.or(HOPPER_BASE, Block.makeCuboidShape(0, 0.25, 0.375, 0.25, 0.5, 0.625));
    private static final VoxelShape DOWN_RAYTRACE_SHAPE = DOWN_SHAPE;
    private static final VoxelShape EAST_RAYTRACE_SHAPE = EAST_SHAPE;
    private static final VoxelShape NORTH_RAYTRACE_SHAPE = NORTH_SHAPE;
    private static final VoxelShape SOUTH_RAYTRACE_SHAPE = SOUTH_SHAPE;
    private static final VoxelShape WEST_RAYTRACE_SHAPE = WEST_SHAPE;

    public BlockFastHopper() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f, 8f));
    }

    // TODO opqaueCube fullBlock false

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
        switch (state.get(FACING)) {
            case DOWN:
                return DOWN_SHAPE;
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case EAST:
                return EAST_SHAPE;
            default:
                return HOPPER_BASE;
        }
    }

    @Override
    public VoxelShape getRaytraceShape(IBlockState state, IBlockReader world, BlockPos pos) {
        switch (state.get(FACING)) {
            case DOWN:
                return DOWN_RAYTRACE_SHAPE;
            case NORTH:
                return NORTH_RAYTRACE_SHAPE;
            case SOUTH:
                return SOUTH_RAYTRACE_SHAPE;
            case WEST:
                return WEST_RAYTRACE_SHAPE;
            case EAST:
                return EAST_RAYTRACE_SHAPE;
            default:
                return HOPPER_BASE;
        }
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext useContext) {
        EnumFacing opposite = useContext.getFace().getOpposite();
        if (opposite == EnumFacing.UP) {
            opposite = EnumFacing.DOWN;
        }
        return getDefaultState().with(FACING, opposite).with(ENABLED, true);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileFastHopper();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(FACING, ENABLED);
    }

    @Nullable
    @Override
    public EnumFacing[] getValidRotations(IBlockState state, IBlockReader world, BlockPos pos) {
        return new EnumFacing[]{
                EnumFacing.NORTH,
                EnumFacing.EAST,
                EnumFacing.SOUTH,
                EnumFacing.WEST,
                EnumFacing.DOWN,
        };
    }

    // TODO shouldSideBeRendered = true

    @Override
    public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState) {
        updateState(state, world, pos);
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileFastHopper) {
                if (player.isSneaking()) {
                    RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
                } else {
                    NetworkHooks.openGui((EntityPlayerMP) player, (TileFastHopper) tileEntity, it -> it.writeBlockPos(pos));
                }
            }

            return true;
        }

        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        updateState(world.getBlockState(pos), world, pos);
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
        RefinedRelocationUtils.dropItemHandler(world, pos);
        world.updateComparatorOutputLevel(pos, this);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            return itemHandlerCap.map(ItemHandlerHelper::calcRedstoneFromInventory).orElse(0);
        }

        return 0;
    }

    private void updateState(IBlockState state, World world, BlockPos pos) {
        boolean isEnabled = !world.isBlockPowered(pos);
        if (isEnabled != state.get(ENABLED)) {
            world.setBlockState(pos, state.with(ENABLED, isEnabled), 4);
        }
    }
}
