package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBlockExtender extends BlockModTile {

	public static final String name = "block_extender";
	public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

	public BlockBlockExtender() {
		super(Material.IRON);
		setUnlocalizedName(registryName.toString());
		setSoundType(SoundType.METAL);
		setHardness(3f);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
		super.onBlockPlacedBy(world, pos, state, placer, itemStack);
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileBlockExtender) {
			TileBlockExtender blockExtender = (TileBlockExtender) tileEntity;
			EnumFacing facing = state.getValue(DIRECTION);
			for(RelativeSide side : RelativeSide.values()) {
				if(side != RelativeSide.FRONT) {
					blockExtender.setSideMapping(side, facing);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_BLOCK_EXTENDER, pos, facing.getIndex()));
		}
		return true;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DIRECTION);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileBlockExtender();
	}

}
