package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.tile.TileSortingInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class BlockSortingInterface extends ContainerBlock {

    public static final String name = "sorting_interface";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public BlockSortingInterface() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
            }
        }

        return true;
    }

    // TODO opaqueCube = false

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return getDefaultState().with(FACING, facing.getOpposite());
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
