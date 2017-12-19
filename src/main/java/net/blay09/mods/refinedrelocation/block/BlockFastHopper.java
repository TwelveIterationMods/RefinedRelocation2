package net.blay09.mods.refinedrelocation.block;

import com.google.common.base.Predicate;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFastHopper extends BlockModTile {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
	{
		public boolean apply(@Nullable EnumFacing facing)
		{
			return facing != EnumFacing.UP;
		}
	});

	private static final PropertyBool ENABLED = PropertyBool.create("enabled");

	private static final AxisAlignedBB[] BOUNDING_BOX = new AxisAlignedBB[]{
			new AxisAlignedBB(0.0625f, 0f, 0.0625f, 0.9375f, 1f, 0.9375f),
			new AxisAlignedBB(0.0625, 0.625, 0.0625, 0.9375, 1, 0.9375),
			new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.625, 0.75),
	};

	private static final AxisAlignedBB[] BOUNDING_BOX_FACING = new AxisAlignedBB[]{
			new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.25, 0.625),     // Down
			null,                                                       // Up
			new AxisAlignedBB(0.375, 0.25, 0, 0.625, 0.5, 0.25),        // North
			new AxisAlignedBB(0.375, 0.25, 0.75, 0.625, 0.5, 1),        // South
			new AxisAlignedBB(0, 0.25, 0.375, 0.25, 0.5, 0.625),        // West
			new AxisAlignedBB(0.75, 0.25, 0.375, 1, 0.5, 0.625),        // East
	};

	private int rayTracePass;

	public BlockFastHopper() {
		this("fast_hopper");
	}

	public BlockFastHopper(String registryName) {
		super(Material.IRON, registryName);
		setSoundType(SoundType.METAL);
		setHardness(3f);
		setResistance(8f);
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
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if(rayTracePass == 3) {
			return BOUNDING_BOX_FACING[state.getValue(FACING).ordinal()];
		}
		return BOUNDING_BOX[rayTracePass];
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing opposite = facing.getOpposite();
		if (opposite == EnumFacing.UP) {
			opposite = EnumFacing.DOWN;
		}
		return getDefaultState().withProperty(FACING, opposite).withProperty(ENABLED, true);
	}



	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFastHopper();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ENABLED);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(ENABLED, (meta & 8) != 8);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = (state.getValue(FACING)).getIndex();
		if (!state.getValue(ENABLED)) {
			meta |= 8;
		}
		return meta;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Nullable
	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos)
	{
		EnumFacing[] result = {
			EnumFacing.NORTH,
			EnumFacing.EAST,
			EnumFacing.SOUTH,
			EnumFacing.WEST,
			EnumFacing.DOWN,
		};
		return result;
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
		RayTraceResult[] mops = new RayTraceResult[8];
		for (int i = 1; i <= 3; i++) {
			rayTracePass = i;
			mops[i] = super.collisionRayTrace(state, world, pos, start, end);
		}
		rayTracePass = 0;
		RayTraceResult maxMop = null;
		double maxDist = 0.0D;
		for (RayTraceResult mop : mops) {
			if (mop != null) {
				double dist = mop.hitVec.squareDistanceTo(end);
				if (dist > maxDist) {
					maxMop = mop;
					maxDist = dist;
				}
			}
		}
		return maxMop;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		updateState(state, world, pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ItemStack heldItem = player.getHeldItem(hand);
			if(tryNameBlock(player, heldItem, world, pos)) {
				return true;
			}

			if(player.isSneaking()) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null) {
					RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
				}
			} else {
				RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_FAST_HOPPER, pos));
			}
			return true;
		}
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		updateState(world.getBlockState(pos), world, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	private void updateState(IBlockState state, World world, BlockPos pos) {
		boolean isEnabled = !world.isBlockPowered(pos);
		if (isEnabled != state.getValue(ENABLED)) {
			world.setBlockState(pos, state.withProperty(ENABLED, isEnabled), 4);
		}
	}
}
