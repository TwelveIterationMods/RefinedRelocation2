package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBlockExtender extends BlockModTile {

	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");

	public BlockBlockExtender() {
		super(Material.IRON, "block_extender");
		setSoundType(SoundType.METAL);
		setHardness(3f);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DIRECTION, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) { // TileBlockExtender depends on this being an ordinal of EnumFacing
		return state.getValue(DIRECTION).ordinal();
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(DIRECTION, facing.getOpposite());
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileBlockExtender) {
			TileBlockExtender tileBlockExtender = (TileBlockExtender) tileEntity;
			return state.withProperty(DOWN, tileBlockExtender.hasVisibleConnection(EnumFacing.DOWN))
					.withProperty(UP, tileBlockExtender.hasVisibleConnection(EnumFacing.UP))
					.withProperty(NORTH, tileBlockExtender.hasVisibleConnection(EnumFacing.NORTH))
					.withProperty(SOUTH, tileBlockExtender.hasVisibleConnection(EnumFacing.SOUTH))
					.withProperty(WEST, tileBlockExtender.hasVisibleConnection(EnumFacing.WEST))
					.withProperty(EAST, tileBlockExtender.hasVisibleConnection(EnumFacing.EAST));
		}
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DIRECTION, DOWN, UP, NORTH, SOUTH, WEST, EAST);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileBlockExtender();
	}

}
