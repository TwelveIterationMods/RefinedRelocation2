package net.blay09.mods.refinedrelocation.compat.ironchest;

import cpw.mods.ironchest.common.blocks.chest.BlockIronChest;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.block.BlockModTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSortingIronChest extends BlockModTile {

    public static final String name = "sorting_iron_chest";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    private Block delegateBlock;

    public BlockSortingIronChest() {
        super(Material.IRON);
        setHardness(3f);
    }

    public void setDelegateBlock(Block delegateBlock) {
        this.delegateBlock = delegateBlock;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return delegateBlock.getBoundingBox(state, source, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return delegateBlock.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        switch (state.getValue(BlockIronChest.VARIANT_PROP)) {
            case GOLD:
                return new TileSortingIronChest.Gold();
            case DIAMOND:
                return new TileSortingIronChest.Diamond();
            case COPPER:
                return new TileSortingIronChest.Copper();
            case SILVER:
                return new TileSortingIronChest.Silver();
            case CRYSTAL:
                return new TileSortingIronChest.Crystal();
            case OBSIDIAN:
                return new TileSortingIronChest.Obsidian();
            case DIRTCHEST9000:
                return new TileSortingIronChest.Dirt();
            default:
                return new TileSortingIronChest();
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (IronChestType type : IronChestType.VALUES) {
            if (type.isValidForCreativeMode()) {
                items.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockIronChest.VARIANT_PROP, IronChestType.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockIronChest.VARIANT_PROP).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockIronChest.VARIANT_PROP);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState blockState) {
        delegateBlock.onBlockAdded(world, pos, blockState);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack itemStack) {
        delegateBlock.onBlockPlacedBy(world, pos, state, entityLiving, itemStack);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return delegateBlock.damageDropped(state);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        delegateBlock.breakBlock(world, pos, state);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return delegateBlock.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(IBlockState state) {
        return delegateBlock.hasComparatorInputOverride(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return delegateBlock.getComparatorInputOverride(state, world, pos);
    }

    @Override
    public EnumFacing[] getValidRotations(World worldObj, BlockPos pos) {
        return delegateBlock.getValidRotations(worldObj, pos);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return delegateBlock.rotateBlock(world, pos, axis);
    }

}
