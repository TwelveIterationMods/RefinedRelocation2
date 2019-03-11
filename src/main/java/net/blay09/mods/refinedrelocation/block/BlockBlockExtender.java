package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
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
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockBlockExtender extends BlockContainer {

    public static final String name = "block_extender";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public BlockBlockExtender() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, placer, itemStack);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileBlockExtender) {
            TileBlockExtender blockExtender = (TileBlockExtender) tileEntity;
            EnumFacing facing = state.get(BlockStateProperties.FACING);
            for (RelativeSide side : RelativeSide.values()) {
                if (side != RelativeSide.FRONT) {
                    blockExtender.setSideMapping(side, facing);
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileBlockExtender tileEntity = (TileBlockExtender) world.getTileEntity(pos);
            NetworkHooks.openGui((EntityPlayerMP) player, tileEntity, writer -> {
                writer.writeBlockPos(pos);
                writer.writeInt(facing.getIndex());
            });
        }

        return true;
    }

    // TODO opaqueCube = false

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext useContext) {
        return getDefaultState().with(BlockStateProperties.FACING, useContext.getFace().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileBlockExtender();
    }
}
