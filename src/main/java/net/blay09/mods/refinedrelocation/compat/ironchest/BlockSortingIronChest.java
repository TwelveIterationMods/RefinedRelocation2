package net.blay09.mods.refinedrelocation.compat.ironchest;

import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.block.BlockModTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSortingIronChest extends BlockModTile {

	private final Block baseBlock;

	public BlockSortingIronChest(Block baseBlock) {
		super(Material.IRON, "sorting_iron_chest");
		this.baseBlock = baseBlock;
		setHardness(3f);
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return baseBlock.getBoundingBox(state, source, pos);
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
		if (player.isSneaking()) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileSortingIronChest) {
				RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
			}
		}
		return baseBlock.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
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
	public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (IronChestType type : IronChestType.VALUES) {
			if (type.isValidForCreativeMode()) {
				list.add(new ItemStack(item, 1, type.ordinal()));
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
		baseBlock.onBlockAdded(world, pos, blockState);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack itemStack) {
		baseBlock.onBlockPlacedBy(world, pos, state, entityLiving, itemStack);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return baseBlock.damageDropped(state);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		baseBlock.breakBlock(world, pos, state);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		return baseBlock.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(IBlockState state) {
		return baseBlock.hasComparatorInputOverride(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return baseBlock.getComparatorInputOverride(state, world, pos);
	}

	@Override
	public EnumFacing[] getValidRotations(World worldObj, BlockPos pos) {
		return baseBlock.getValidRotations(worldObj, pos);
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return baseBlock.rotateBlock(world, pos, axis);
	}

}
