package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.RefinedRelocationUtils;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BlockSortingChest extends BlockContainer {

    public static final String name = "sorting_chest";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);
    public static final EnumProperty<EnumFacing> FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public BlockSortingChest() {
        super(Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(3f));
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public IBlockState rotate(IBlockState state, Rotation rot) {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public IBlockState mirror(IBlockState state, Mirror mirror) {
        return state.rotate(mirror.toRotation(state.get(HORIZONTAL_FACING)));
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext useContext) {
        return getDefaultState().with(HORIZONTAL_FACING, useContext.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack itemStack) {
        if (itemStack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileSortingChest) {
                ((TileSortingChest) tileEntity).setCustomName(itemStack.getDisplayName().getUnformattedComponentText());
            }
        }
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileSortingChest) {
                NetworkHooks.openGui((EntityPlayerMP) player, (TileSortingChest) tileEntity, pos);
            }
        }

        return true;
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean what) {
        if (state.getBlock() != newState.getBlock()) {
            RefinedRelocationUtils.dropItemHandler(world, pos);
            world.updateComparatorOutputLevel(pos, this);
        }

        super.onReplaced(state, world, pos, newState, what);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileSortingChest();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return RefinedRelocationUtils.getComparatorInputOverride(state, world, pos);
    }
}
