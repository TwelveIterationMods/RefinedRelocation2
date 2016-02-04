package net.blay09.mods.refinedrelocation2.block;

import mcmultipart.block.BlockCoverable;
import mcmultipart.block.BlockMultipart;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.balyware.ItemUtils;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.blay09.mods.refinedrelocation2.tile.TileBetterHopper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBetterHopper extends BlockCoverable {

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    private static final PropertyBool ENABLED = PropertyBool.create("enabled");

    private static final AxisAlignedBB[] BOUNDING_BOX = new AxisAlignedBB[]{
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

    public BlockBetterHopper() {
        this("better_hopper");
    }

    public BlockBetterHopper(String registryName) {
        super(Material.iron);
        setRegistryName(registryName);
        setUnlocalizedName(getRegistryName());
        setCreativeTab(RefinedRelocation2.creativeTab);
        setHardness(3f);
        setResistance(8f);
        GameRegistry.registerBlock(this);
    }

    @Override
    public void setBlockBoundsBasedOnStateDefault(IBlockAccess world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        if(blockState.getBlock() == this) {
            IExtendedBlockState extendedState = getExtendedState(blockState, world, pos);
            switch (rayTracePass) {
                case 0:
                    setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 1f, 0.9375f);
                    break;
                case 1:
                    setBlockBounds(BOUNDING_BOX[0]);
                    break;
                case 2:
                    setBlockBounds(BOUNDING_BOX[1]);
                    break;
                case 3:
                    setBlockBounds(BOUNDING_BOX_FACING[extendedState.getValue(FACING).ordinal()]);
                    break;
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing opposite = facing.getOpposite();
        if (opposite == EnumFacing.UP) {
            opposite = EnumFacing.DOWN;
        }
        return getDefaultState().withProperty(FACING, opposite).withProperty(ENABLED, true);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBetterHopper();
    }

    @Override
    protected BlockState createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] {FACING, ENABLED}, BlockMultipart.properties);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(ENABLED, (meta & 8) != 8);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta = meta | (state.getValue(FACING)).getIndex();
        if (!state.getValue(ENABLED)) {
            meta |= 8;
        }
        return meta;
    }

    private void setBlockBounds(AxisAlignedBB bb) {
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public MovingObjectPosition collisionRayTraceDefault(World world, BlockPos pos, Vec3 start, Vec3 end) {
        MovingObjectPosition[] mops = new MovingObjectPosition[8];
        for (int i = 1; i <= 3; i++) {
            rayTracePass = i;
            mops[i] = super.collisionRayTraceDefault(world, pos, start, end);
        }
        rayTracePass = 0;
        setBlockBoundsBasedOnStateDefault(world, pos);
        MovingObjectPosition maxMop = null;
        double maxDist = 0.0D;
        for (MovingObjectPosition mop : mops) {
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
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        if (itemStack.hasDisplayName()) {
            TileBetterHopper tileEntity = (TileBetterHopper) world.getTileEntity(pos);
            tileEntity.setCustomName(itemStack.getDisplayName());
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        updateState(world, pos, state);
    }

    @Override
    public boolean onBlockActivatedDefault(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        player.openGui(RefinedRelocation2.instance, GuiHandler.GUI_HOPPER, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void onNeighborBlockChangeDefault(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        updateState(world, pos, state);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileBetterHopper) {
            ItemUtils.dropInventoryItems(world, pos, ((TileBetterHopper) tileEntity).getItemHandler());
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    private void updateState(World world, BlockPos pos, IBlockState state) {
        boolean isEnabled = !world.isBlockPowered(pos);
        if (isEnabled != state.getValue(ENABLED)) {
            world.setBlockState(pos, state.withProperty(ENABLED, isEnabled), 4);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        mesher.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
