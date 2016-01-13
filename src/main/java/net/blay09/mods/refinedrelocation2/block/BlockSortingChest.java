package net.blay09.mods.refinedrelocation2.block;

import net.blay09.mods.refinedrelocation2.ModBlocks;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSortingChest extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockSortingChest() {
        super(Material.wood);
        setRegistryName("sorting_chest");
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setUnlocalizedName(getRegistryName());
        setCreativeTab(RefinedRelocation2.creativeTab);
        setHardness(2.5f);
        setStepSound(soundTypeWood);
        setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        GameRegistry.registerBlock(this);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSortingChest();
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(FACING, facing);
        world.setBlockState(pos, state, 1 | 2);

        if (itemStack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileSortingChest) {
                ((TileSortingChest) tileEntity).setCustomName(itemStack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        if (isBlocked(world, pos)) {
            return true;
        }
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileSortingChest) {
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_SORTING_CHEST, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileSortingChest) {
            return Container.calcRedstoneFromInventory((TileSortingChest) tileEntity);
        }
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    private boolean isBlocked(World world, BlockPos pos) {
        return isBelowSolidBlock(world, pos) || isOcelotSittingOnChest(world, pos);
    }

    private boolean isBelowSolidBlock(World world, BlockPos pos) {
        return world.isSideSolid(pos.up(), EnumFacing.DOWN, false);
    }

    private boolean isOcelotSittingOnChest(World world, BlockPos pos) {
        for (Entity entity : world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double) pos.getX(), (double) (pos.getY() + 1), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1)))) {
            EntityOcelot entityocelot = (EntityOcelot) entity;
            if (entityocelot.isSitting()) {
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerSpecialModels() {
        Item sortingChestItem = Item.getItemFromBlock(ModBlocks.sortingChest);
        ForgeHooksClient.registerTESRItemStack(sortingChestItem, 0, TileSortingChest.class);
        ModelLoader.setCustomModelResourceLocation(sortingChestItem, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
