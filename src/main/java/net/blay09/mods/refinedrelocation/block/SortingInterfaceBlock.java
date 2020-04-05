package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.tile.TileSortingInterface;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class SortingInterfaceBlock extends ContainerBlock {

    /**
     * We provide a slightly smaller render shape to prevent neighbour blocks from being culled.
     */
    private static final VoxelShape RENDER_SHAPE = VoxelShapes.create(1 / 16f, 1 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 15 / 16f);

    public static final String name = "sorting_interface";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public SortingInterfaceBlock() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f));
    }

    @Override
    public VoxelShape getRenderShape(BlockState p_196247_1_, IBlockReader p_196247_2_, BlockPos p_196247_3_) {
        return RENDER_SHAPE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                RefinedRelocationAPI.openRootFilterGui(player, tileEntity, 0);
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext useContext ) {
        return getDefaultState().with( BlockStateProperties.FACING, useContext.getFace().getOpposite() );
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileSortingInterface();
    }

}
