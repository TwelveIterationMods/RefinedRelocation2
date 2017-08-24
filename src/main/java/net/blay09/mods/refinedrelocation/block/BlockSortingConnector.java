package net.blay09.mods.refinedrelocation.block;

import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.tile.TileSortingConnector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockSortingConnector extends BlockContainer {

	public static final String name = "sorting_connector";
	public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

	private static Map<BlockPos, IBlockState> boundingBoxCache = Maps.newHashMap();
	private static final AxisAlignedBB BOUNDING_BOX_CENTER = new AxisAlignedBB(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);
	private static final AxisAlignedBB[] BOUNDING_BOX = new AxisAlignedBB[]{
			new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.3125, 0.6875),         // Down
			new AxisAlignedBB(0.3125, 0.6875, 0.3125, 0.6875, 1, 0.6875),         // Up
			new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.3125),         // North
			new AxisAlignedBB(0.3125, 0.3125, 0.6875, 0.6875, 0.6875, 1),         // South
			new AxisAlignedBB(0, 0.3125, 0.3125, 0.3125, 0.6875, 0.6875),         // West
			new AxisAlignedBB(0.6875, 0.3125, 0.3125, 1, 0.6875, 0.6875)          // East
	};

	private static final PropertyBool DOWN = PropertyBool.create("down");
	private static final PropertyBool UP = PropertyBool.create("up");
	private static final PropertyBool NORTH = PropertyBool.create("north");
	private static final PropertyBool SOUTH = PropertyBool.create("south");
	private static final PropertyBool WEST = PropertyBool.create("west");
	private static final PropertyBool EAST = PropertyBool.create("east");
	private static final PropertyBool CORNER = PropertyBool.create("corner");
	public static final PropertyBool[] CONNECTIONS = new PropertyBool[]{
			DOWN,
			UP,
			NORTH,
			SOUTH,
			WEST,
			EAST
	};

	public BlockSortingConnector() {
		super(Material.IRON);
		setUnlocalizedName(registryName.toString());
		setSoundType(SoundType.METAL);
		setHardness(0.3f);
		setCreativeTab(RefinedRelocation.creativeTab);

		IBlockState state = blockState.getBaseState();
		for (PropertyBool property : CONNECTIONS) {
			state = state.withProperty(property, false);
		}
		setDefaultState(state);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSortingConnector();
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		IBlockState actualState = boundingBoxCache.computeIfAbsent(pos, p -> getActualState(state, source, p));
		AxisAlignedBB boundingBox = BOUNDING_BOX_CENTER;
		for (int i = 0; i < CONNECTIONS.length; i++) {
			PropertyBool property = CONNECTIONS[i];
			if (actualState.getValue(property)) {
				boundingBox = boundingBox.union(BOUNDING_BOX[i]);
			}
		}
		return boundingBox;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DOWN, UP, NORTH, SOUTH, WEST, EAST, CORNER);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
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
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

//	@Override
//	public void onBlockAdded(World world, BlockPos pos, IBlockState state) { // TODO remove
//		super.onBlockAdded(world, pos, state);
//		IBlockState newState = getConnectionState(world, pos, state);
//		if(newState != state) {
//			world.setBlockState(pos, newState, 3);
//		}
//	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		boundingBoxCache.remove(pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		IBlockState newState = getConnectionState(world, pos, state);
		if(newState != state) {
			world.setBlockState(pos, newState, 3);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState actualState = getConnectionState(world, pos, state);
		boundingBoxCache.put(pos, actualState);
		return actualState;
	}

	private IBlockState getConnectionState(IBlockAccess world, BlockPos pos, IBlockState state) {
		EnumFacing.Axis axis = null;
		boolean isCorner = false;
		int connectionCount = 0;
		for (EnumFacing facing : EnumFacing.VALUES) {
			BlockPos neighbourPos = pos.offset(facing);
			TileEntity tileEntity = world.getTileEntity(neighbourPos);
			if(tileEntity != null && tileEntity.hasCapability(Capabilities.SORTING_GRID_MEMBER, facing.getOpposite())) {
				state = state.withProperty(CONNECTIONS[facing.getIndex()], true);
				if (axis != null && axis != facing.getAxis()) {
					isCorner = true;
				}
				axis = facing.getAxis();
				connectionCount++;
			} else {
				state = state.withProperty(CONNECTIONS[facing.getIndex()], false);
			}
		}
		if(connectionCount == 1) {
			isCorner = true;
		}
		return state.withProperty(CORNER, isCorner);
	}
}