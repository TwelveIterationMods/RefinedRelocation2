package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.VanillaPacketHandler;
import net.blay09.mods.refinedrelocation.util.ItemHandlerHelper2;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockSortingChest extends BlockModTile {

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.875, 0.9375);

	public BlockSortingChest() {
		super(Material.WOOD, "sorting_chest");
		setSoundType(SoundType.WOOD);
		setHardness(3f);
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
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}


	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double) (placer.rotationYaw * 4f / 360f) + 0.5) & 3).getOpposite();
		state = state.withProperty(FACING, facing);
		if (itemStack.hasDisplayName()) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileSortingChest) {
				((TileSortingChest) tileEntity).setCustomName(itemStack.getDisplayName());
			}
		}
		world.setBlockState(pos, state, 1|2);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ItemStack heldItem = player.getHeldItem(hand);
			if (!heldItem.isEmpty() && heldItem.getItem() == Items.NAME_TAG && heldItem.hasDisplayName()) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity instanceof TileSortingChest) {
					((TileSortingChest) tileEntity).setCustomName(heldItem.getDisplayName());
					VanillaPacketHandler.sendTileEntityUpdate(tileEntity);
					if (!player.capabilities.isCreativeMode) {
						heldItem.shrink(1);
					}
				}
				return true;
			}

			if(player.isSneaking()) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity instanceof TileSortingChest) {
					RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
				}
			} else {
				RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_SORTING_CHEST, pos));
			}
			return true;
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity != null) {
			IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(itemHandler != null) {
				ItemHandlerHelper2.dropItemHandlerItems(world, pos, itemHandler);
			}
			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSortingChest();
	}

}
