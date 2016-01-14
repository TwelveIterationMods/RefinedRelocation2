package net.blay09.mods.refinedrelocation2.part;

import mcmultipart.MCMultiPartMod;
import mcmultipart.block.TileMultipart;
import mcmultipart.client.multipart.IHitEffectsPart;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class PartSortingConnector extends Multipart implements ISlottedPart, IOccludingPart, IHitEffectsPart, ITickable, IWorldPos {

    private static final AxisAlignedBB BOUNDING_BOX_CENTER = new AxisAlignedBB(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);
    private static final AxisAlignedBB[] BOUNDING_BOX = new AxisAlignedBB[]{
            new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.3125, 0.6875),         // Down
            new AxisAlignedBB(0.3125, 0.6875, 0.3125, 0.6875, 1, 0.6875),         // Up
            new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.3125),         // North
            new AxisAlignedBB(0.3125, 0.3125, 0.6875, 0.6875, 0.6875, 1),         // South
            new AxisAlignedBB(0, 0.3125, 0.3125, 0.3125, 0.6875, 0.6875),         // West
            new AxisAlignedBB(0.6875, 0.3125, 0.3125, 1, 0.6875, 0.6875)          // East
    };

    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool DOWN = PropertyBool.create("down");
    private static final PropertyBool NORTH = PropertyBool.create("north");
    private static final PropertyBool SOUTH = PropertyBool.create("south");
    private static final PropertyBool WEST = PropertyBool.create("west");
    private static final PropertyBool EAST = PropertyBool.create("east");

    private ISortingGridMember sortingMember;
    private byte connectionMap;
    private boolean dirtyNeighbours;

    public PartSortingConnector() {
        sortingMember = RefinedRelocationAPI.createSortingMember(this);
    }

    @Override
    public String getType() {
        return RefinedRelocation2.MOD_ID + ":sorting_connector";
    }

    @Override
    public String getModelPath() {
        return RefinedRelocation2.MOD_ID + ":sorting_connector";
    }

    @Override
    public Material getMaterial() {
        return Material.iron;
    }

    @Override
    public float getHardness(PartMOP hit) {
        return 0.3f;
    }

    public ItemStack getItemStack() {
        return new ItemStack(ModItems.sortingConnector, 1);
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer entityPlayer, PartMOP hit) {
        return getItemStack();
    }

    @Override
    public List<ItemStack> getDrops() {
        return Collections.singletonList(getItemStack());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(IHitEffectsPart.AdvancedEffectRenderer advancedEffectRenderer) {
        advancedEffectRenderer.addBlockDestroyEffects(getPos(), Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(getExtendedState(MultipartRegistry.getDefaultState(this).getBaseState())));
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(PartMOP partMOP, AdvancedEffectRenderer advancedEffectRenderer) {
        return true;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return BOUNDING_BOX_CENTER;
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(BOUNDING_BOX_CENTER);
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (isConnected(facing)) {
                list.add(BOUNDING_BOX[facing.ordinal()]);
            }
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB bb = BOUNDING_BOX_CENTER;
        if (mask.intersectsWith(bb)) {
            list.add(bb);
        }
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (isConnected(facing) && BOUNDING_BOX[facing.ordinal()].intersectsWith(mask)) {
                list.add(BOUNDING_BOX[facing.ordinal()]);
            }
        }
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(AxisAlignedBB.fromBounds(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.CENTER);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {
        return state.withProperty(DOWN, isConnected(EnumFacing.DOWN))
                .withProperty(UP, isConnected(EnumFacing.UP))
                .withProperty(NORTH, isConnected(EnumFacing.NORTH))
                .withProperty(SOUTH, isConnected(EnumFacing.SOUTH))
                .withProperty(WEST, isConnected(EnumFacing.WEST))
                .withProperty(EAST, isConnected(EnumFacing.EAST));
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(MCMultiPartMod.multipart, DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    private boolean isConnected(EnumFacing facing) {
        return (connectionMap & (1 << facing.ordinal())) != 0;
    }

    @Override
    public void update() {
        if (dirtyNeighbours) {
            updateNeighborInfo(true);
            dirtyNeighbours = false;
        }
    }

    @Override
    public void onLoaded() {
        dirtyNeighbours = true;
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        dirtyNeighbours = true;
    }

    @Override
    public void onNeighborTileChange(EnumFacing facing) {
        dirtyNeighbours = true;
    }

    @Override
    public void onPartChanged(IMultipart part) {
        dirtyNeighbours = true;
    }

    @Override
    public void onAdded() {
        updateNeighborInfo(false);
        scheduleRenderUpdate();
    }

    private void scheduleRenderUpdate() {
        getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setByte("ConnectionMap", connectionMap);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        connectionMap = tagCompound.getByte("ConnectionMap");
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        connectionMap = buf.readByte();
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeByte(connectionMap);
    }

    private void updateNeighborInfo(boolean sendPacket) {
        if (!getWorld().isRemote) {
            byte oc = connectionMap;

            for (EnumFacing dir : EnumFacing.VALUES) {
                updateConnections(dir);
            }

            if (sendPacket && connectionMap != oc) {
                sendUpdatePacket();
            }
            scheduleRenderUpdate();
        }
    }

    private void updateConnections(EnumFacing side) {
        if (side != null) {
            connectionMap &= ~(1 << side.ordinal());

            if (canConnectTo(side)) {

                PartSortingConnector connector = getConnector(getWorld(), getPos().offset(side), side.getOpposite());
                if (connector != null && !connector.canConnectTo(side.getOpposite())) {
                    return;
                }

                connectionMap |= 1 << side.ordinal();
            }
        } else {
            for (EnumFacing facing : EnumFacing.VALUES) {
                updateConnections(facing);
            }
        }
    }

    private boolean canConnectTo(EnumFacing side) {
        ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(side));
        if (part instanceof IMicroblock.IFaceMicroblock) {
            if (!((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
                return false;
            }
        }

        if(hasCapabilityInside(RefinedRelocation2.SORTING_GRID_MEMBER, side)) {
            return true;
        }

        if (!OcclusionHelper.occlusionTest(getContainer().getParts(), this, BOUNDING_BOX[side.ordinal()])) {
            return false;
        }

        if (getConnector(getWorld(), getPos().offset(side), side.getOpposite()) != null) {
            return true;
        }

        EnumFacing opposite = side.getOpposite();
        TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
        return tileEntity != null && tileEntity.hasCapability(RefinedRelocation2.SORTING_GRID_MEMBER, opposite);
    }

    private PartSortingConnector getConnector(World world, BlockPos blockPos, EnumFacing side) {
        IMultipartContainer container = MultipartHelper.getPartContainer(world, blockPos);
        if (container == null) {
            return null;
        }

        if (side != null) {
            ISlottedPart part = container.getPartInSlot(PartSlot.getFaceSlot(side));
            if (part instanceof IMicroblock.IFaceMicroblock && !((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
                return null;
            }
        }

        ISlottedPart part = container.getPartInSlot(PartSlot.CENTER);
        if (part instanceof PartSortingConnector) {
            return (PartSortingConnector) part;
        }
        return null;
    }

    public boolean hasCapabilityInside(Capability capability, EnumFacing slotSide) {
        ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(slotSide));
        return part != null && part instanceof ICapabilityProvider && ((ICapabilityProvider) part).hasCapability(capability, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == RefinedRelocation2.SORTING_GRID_MEMBER) {
            return (T) sortingMember;
        }
        return null;
    }
}
