package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.RefinedRelocationUtils;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.block.entity.BlockExtenderBlockEntity;
import net.blay09.mods.refinedrelocation.block.entity.FastHopperBlockEntity;
import net.blay09.mods.refinedrelocation.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FastHopperBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", facing -> facing != Direction.UP);
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    public static final String name = "fast_hopper";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    private static final VoxelShape INPUT_SHAPE = box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape MIDDLE_SHAPE = box(4.0D, 4.0D, 4.0D, 12.0D, 10.0D, 12.0D);
    private static final VoxelShape INPUT_MIDDLE_SHAPE = Shapes.or(MIDDLE_SHAPE, INPUT_SHAPE);
    private static final VoxelShape HOPPER_BASE = Shapes.join(INPUT_MIDDLE_SHAPE, Hopper.INSIDE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape DOWN_SHAPE = Shapes.or(HOPPER_BASE, box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D));
    private static final VoxelShape EAST_SHAPE = Shapes.or(HOPPER_BASE, box(12.0D, 4.0D, 6.0D, 16.0D, 8.0D, 10.0D));
    private static final VoxelShape NORTH_SHAPE = Shapes.or(HOPPER_BASE, box(6.0D, 4.0D, 0.0D, 10.0D, 8.0D, 4.0D));
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(HOPPER_BASE, box(6.0D, 4.0D, 12.0D, 10.0D, 8.0D, 16.0D));
    private static final VoxelShape WEST_SHAPE = Shapes.or(HOPPER_BASE, box(0.0D, 4.0D, 6.0D, 4.0D, 8.0D, 10.0D));
    private static final VoxelShape DOWN_RAYTRACE_SHAPE = Hopper.INSIDE;
    private static final VoxelShape EAST_RAYTRACE_SHAPE = Shapes.or(Hopper.INSIDE, box(12.0D, 8.0D, 6.0D, 16.0D, 10.0D, 10.0D));
    private static final VoxelShape NORTH_RAYTRACE_SHAPE = Shapes.or(Hopper.INSIDE, box(6.0D, 8.0D, 0.0D, 10.0D, 10.0D, 4.0D));
    private static final VoxelShape SOUTH_RAYTRACE_SHAPE = Shapes.or(Hopper.INSIDE, box(6.0D, 8.0D, 12.0D, 10.0D, 10.0D, 16.0D));
    private static final VoxelShape WEST_RAYTRACE_SHAPE = Shapes.or(Hopper.INSIDE, box(0.0D, 8.0D, 6.0D, 4.0D, 10.0D, 10.0D));

    public FastHopperBlock() {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(3f, 8f));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> HOPPER_BASE;
        };
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return switch (state.getValue(FACING)) {
            case DOWN -> DOWN_RAYTRACE_SHAPE;
            case NORTH -> NORTH_RAYTRACE_SHAPE;
            case SOUTH -> SOUTH_RAYTRACE_SHAPE;
            case WEST -> WEST_RAYTRACE_SHAPE;
            case EAST -> EAST_RAYTRACE_SHAPE;
            default -> Hopper.INSIDE;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext useContext) {
        Direction opposite = useContext.getClickedFace().getOpposite();
        if (opposite == Direction.UP) {
            opposite = Direction.DOWN;
        }

        return defaultBlockState().setValue(FACING, opposite).setValue(ENABLED, true);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FastHopperBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        updateState(state, level, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof FastHopperBlockEntity fastHopper) {
                if (player.isShiftKeyDown()) {
                    RefinedRelocationAPI.openRootFilterGui(player, fastHopper, 0);
                } else {
                    Balm.getNetworking().openGui(player, fastHopper);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean wat) {
        updateState(level.getBlockState(pos), level, pos);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        RefinedRelocationUtils.dropItemHandler(world, pos);
        world.updateNeighbourForOutputSignal(pos, this);
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            Container container = Balm.getProviders().getProvider(blockEntity, Container.class);
            if (container != null) {
                return ItemHandlerHelper.calcRedstoneFromInventory(container);
            }

            return 0;
        }

        return 0;
    }

    private void updateState(BlockState state, Level level, BlockPos pos) {
        boolean isEnabled = !level.hasNeighborSignal(pos);
        if (isEnabled != state.getValue(ENABLED)) {
            level.setBlock(pos, state.setValue(ENABLED, isEnabled), 4);
        }
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FastHopperBlockEntity) {
            ((FastHopperBlockEntity) blockEntity).onEntityCollision(entity);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.fastHopper.get(), FastHopperBlockEntity::serverTick);
    }
}
