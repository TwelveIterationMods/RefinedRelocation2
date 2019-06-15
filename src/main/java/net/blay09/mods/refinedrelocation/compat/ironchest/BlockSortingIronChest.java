//package net.blay09.mods.refinedrelocation.compat.ironchest;
//
//import com.progwml6.ironchest.common.blocks.IronChestType;
//import net.blay09.mods.refinedrelocation.RefinedRelocation;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockContainer;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.*;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.world.Explosion;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IWorldReader;
//import net.minecraft.world.World;
//
//import javax.annotation.Nullable;
//
//public class BlockSortingIronChest extends BlockContainer {
//
//    public static final String name = "sorting_iron_chest";
//    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);
//
//    private final IronChestType ironChestType;
//    private Block delegateBlock;
//
//    public BlockSortingIronChest(IronChestType ironChestType) {
//        super(Properties.create(Material.IRON).hardnessAndResistance(3f));
//        this.ironChestType = ironChestType;
//    }
//
//    public void setDelegateBlock(Block delegateBlock) {
//        this.delegateBlock = delegateBlock;
//    }
//
//    @Override
//    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
//        return delegateBlock.getShape(state, world, pos);
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public boolean isFullCube(IBlockState state) {
//        return false;
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public EnumBlockRenderType getRenderType(IBlockState state) {
//        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
//    }
//
//    @Override
//    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        return delegateBlock.onBlockActivated(state, world, pos, player, hand, facing, hitX, hitY, hitZ);
//    }
//
//    @Nullable
//    @Override
//    public TileEntity createNewTileEntity(IBlockReader world) {
//        switch (ironChestType) {
//            case GOLD:
//                return new TileSortingIronChest.Gold();
//            case DIAMOND:
//                return new TileSortingIronChest.Diamond();
//            case COPPER:
//                return new TileSortingIronChest.Copper();
//            case SILVER:
//                return new TileSortingIronChest.Silver();
//            case CRYSTAL:
//                return new TileSortingIronChest.Crystal();
//            case OBSIDIAN:
//                return new TileSortingIronChest.Obsidian();
//            case DIRTCHEST9000:
//                return new TileSortingIronChest.Dirt();
//            default:
//                return new TileSortingIronChest.Iron();
//        }
//    }
//
//    @Override
//    public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState) {
//        delegateBlock.onBlockAdded(state, world, pos, oldState);
//    }
//
//    @Override
//    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack itemStack) {
//        delegateBlock.onBlockPlacedBy(world, pos, state, placer, itemStack);
//    }
//
//    @Override
//    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
//        delegateBlock.onReplaced(state, world, pos, newState, isMoving);
//    }
//
//    @Override
//    public float getExplosionResistance(IBlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
//        return delegateBlock.getExplosionResistance(state, world, pos, exploder, explosion);
//    }
//
//    @Override
//    public boolean hasComparatorInputOverride(IBlockState state) {
//        return delegateBlock.hasComparatorInputOverride(state);
//    }
//
//    @Override
//    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
//        return delegateBlock.getComparatorInputOverride(state, world, pos);
//    }
//
//    @Nullable
//    @Override
//    public EnumFacing[] getValidRotations(IBlockState state, IBlockReader world, BlockPos pos) {
//        return delegateBlock.getValidRotations(state, world, pos);
//    }
//
//    @Override
//    public IBlockState rotate(IBlockState state, Rotation rotation) {
//        return delegateBlock.rotate(state, rotation);
//    }
//
//    @Override
//    public IBlockState mirror(IBlockState state, Mirror mirror) {
//        return delegateBlock.mirror(state, mirror);
//    }
//
//}
