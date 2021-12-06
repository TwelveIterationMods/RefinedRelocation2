package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.refinedrelocation.block.entity.BlockExtenderBlockEntity;
import net.blay09.mods.refinedrelocation.block.entity.ModBlockEntities;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockExtenderBlock extends BaseEntityBlock {

    /**
     * We provide a slightly smaller render shape to prevent neighbour blocks from being culled.
     */
    private static final VoxelShape RENDER_SHAPE = Shapes.create(1 / 16f, 1 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 15 / 16f);

    public BlockExtenderBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(3f));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return RENDER_SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, placer, itemStack);
        if (level.getBlockEntity(pos) instanceof BlockExtenderBlockEntity blockExtender) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            for (RelativeSide side : RelativeSide.values()) {
                if (side != RelativeSide.FRONT) {
                    blockExtender.setSideMapping(side, facing);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockExtenderBlockEntity blockEntity = (BlockExtenderBlockEntity) level.getBlockEntity(pos);
            Balm.getNetworking().openGui(player, blockEntity.withClickedFace(hitResult.getDirection()));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext useContext) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, useContext.getClickedFace().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockExtenderBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.blockExtender.get(), BlockExtenderBlockEntity::serverTick);
    }
}
