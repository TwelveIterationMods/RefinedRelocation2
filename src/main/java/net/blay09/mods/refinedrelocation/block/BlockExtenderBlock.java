package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockExtenderBlock extends ContainerBlock {

    /**
     * We provide a slightly smaller render shape to prevent neighbour blocks from being culled.
     */
    private static final VoxelShape RENDER_SHAPE = VoxelShapes.create(1 / 16f, 1 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 15 / 16f);

    public static final String name = "block_extender";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public BlockExtenderBlock() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f));
    }

    @Override
    public VoxelShape getRenderShape(BlockState p_196247_1_, IBlockReader p_196247_2_, BlockPos p_196247_3_) {
        return RENDER_SHAPE;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, placer, itemStack);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileBlockExtender) {
            TileBlockExtender blockExtender = (TileBlockExtender) tileEntity;
            Direction facing = state.get(BlockStateProperties.FACING);
            for (RelativeSide side : RelativeSide.values()) {
                if (side != RelativeSide.FRONT) {
                    blockExtender.setSideMapping(side, facing);
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote) {
            TileBlockExtender tileEntity = (TileBlockExtender) world.getTileEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, tileEntity, writer -> {
                writer.writeBlockPos(pos);
                writer.writeInt(rayTraceResult.getFace().getIndex());
            });
        }

        return true;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext useContext) {
        return getDefaultState().with(BlockStateProperties.FACING, useContext.getFace().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileBlockExtender();
    }
}
