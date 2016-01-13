package net.blay09.mods.refinedrelocation2.part;

import mcmultipart.MCMultiPartMod;
import mcmultipart.client.multipart.IHitEffectsPart;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class PartSortingInterface extends CapablePart implements IHitEffectsPart, IOccludingPart, ISlottedPart, IWorldPos {

    private static final AxisAlignedBB[] BOUNDING_BOX = new AxisAlignedBB[]{
            new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875),         // Down
            new AxisAlignedBB(0.125, 0.75, 0.125, 0.875, 1, 0.875),         // Up
            new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.25),         // North
            new AxisAlignedBB(0.125, 0.125, 0.75, 0.875, 0.875, 1),         // South
            new AxisAlignedBB(0, 0.125, 0.125, 0.25, 0.875, 0.875),         // West
            new AxisAlignedBB(0.75, 0.125, 0.125, 1, 0.875, 0.875)          // East
    };
    private static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    private ISortingGridMember sortingMember;
    private EnumFacing facing;

    public PartSortingInterface() {
        sortingMember = RefinedRelocationAPI.createSortingMember(this);
    }

    public PartSortingInterface(EnumFacing facing) {
        this();
        this.facing = facing;
    }

    @Override
    public String getType() {
        return RefinedRelocation2.MOD_ID + ":sorting_interface";
    }

    @Override
    public String getModelPath() {
        return RefinedRelocation2.MOD_ID + ":sorting_interface";
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
        return new ItemStack(ModItems.sortingInterface, 1);
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
    public boolean addHitEffects(PartMOP partMOP, IHitEffectsPart.AdvancedEffectRenderer advancedEffectRenderer) {
        return true;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return BOUNDING_BOX[facing.ordinal()];
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(BOUNDING_BOX[facing.ordinal()]);
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB bb = BOUNDING_BOX[facing.ordinal()];
        if (mask.intersectsWith(bb)) {
            list.add(bb);
        }
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(BOUNDING_BOX[facing.ordinal()]);
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.getFaceSlot(facing));
    }

    @Override
    public IBlockState getExtendedState(IBlockState state) {
        return state.withProperty(FACING, facing);
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(MCMultiPartMod.multipart, FACING);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setByte("Facing", (byte) facing.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        facing = EnumFacing.getFront(tagCompound.getByte("Facing"));
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        facing = EnumFacing.getFront(buf.readByte());
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);
        buf.writeByte(facing.ordinal());
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
