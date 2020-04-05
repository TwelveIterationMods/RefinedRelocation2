//package net.blay09.mods.refinedrelocation.compat.ironchest;
//
//import com.progwml6.ironchest.common.block.IronChestsTypes;
//import net.blay09.mods.refinedrelocation.RefinedRelocation;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.ContainerBlock;
//import net.minecraft.block.material.Material;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.*;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.BlockRayTraceResult;
//import net.minecraft.util.math.shapes.ISelectionContext;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.world.Explosion;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IWorldReader;
//import net.minecraft.world.World;
//
//import javax.annotation.Nullable;
//
//public class BlockSortingIronChest extends ContainerBlock {
//
//    public static final String name = "sorting_iron_chest";
//    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);
//
//    private final IronChestsTypes ironChestType;
//    private Block delegateBlock;
//
//    public BlockSortingIronChest(IronChestsTypes ironChestType) {
//        super(Properties.create(Material.IRON).hardnessAndResistance(3f));
//        this.ironChestType = ironChestType;
//    }
//
//    public void setDelegateBlock(Block delegateBlock) {
//        this.delegateBlock = delegateBlock;
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
//        return delegateBlock.getShape(state, world, pos, context);
//    }
//
//    @Override
//    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
//        return delegateBlock.onBlockActivated(state, world, pos, player, hand, hit);
//    }
//
//    @Nullable
//    @Override
//    public TileEntity createNewTileEntity(IBlockReader world) {
//        switch (ironChestType) {
//            case GOLD:
//                return new SortingIronChestTileEntity.Gold();
//            case DIAMOND:
//                return new SortingIronChestTileEntity.Diamond();
//            case COPPER:
//                return new SortingIronChestTileEntity.Copper();
//            case SILVER:
//                return new SortingIronChestTileEntity.Silver();
//            case CRYSTAL:
//                return new SortingIronChestTileEntity.Crystal();
//            case OBSIDIAN:
//                return new SortingIronChestTileEntity.Obsidian();
//            case DIRT:
//                return new SortingIronChestTileEntity.Dirt();
//            default:
//                return new SortingIronChestTileEntity.Iron();
//        }
//    }
//
//    @Override
//    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
//        delegateBlock.onBlockAdded(state, world, pos, oldState, isMoving);
//    }
//
//    @Override
//    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
//        delegateBlock.onBlockPlacedBy(world, pos, state, placer, itemStack);
//    }
//
//    @Override
//    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
//        delegateBlock.onReplaced(state, world, pos, newState, isMoving);
//    }
//
//    @Override
//    public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
//        return delegateBlock.getExplosionResistance(state, world, pos, exploder, explosion);
//    }
//
//    @Override
//    public boolean hasComparatorInputOverride(BlockState state) {
//        return delegateBlock.hasComparatorInputOverride(state);
//    }
//
//    @Override
//    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
//        return delegateBlock.getComparatorInputOverride(state, world, pos);
//    }
//
//    @Nullable
//    @Override
//    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos) {
//        return delegateBlock.getValidRotations(state, world, pos);
//    }
//
//    @Override
//    public BlockState rotate(BlockState state, Rotation rotation) {
//        return delegateBlock.rotate(state, rotation);
//    }
//
//    @Override
//    public BlockState mirror(BlockState state, Mirror mirror) {
//        return delegateBlock.mirror(state, mirror);
//    }
//
//}
