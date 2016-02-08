package net.blay09.mods.refinedrelocation2.tile;

import mcmultipart.block.TileCoverable;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.block.BlockBetterHopper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileBetterHopper extends TileCoverable implements ITickable {

    protected String customName;
    private int cooldown;
    protected ItemStackHandler itemHandler;

    public TileBetterHopper() {
        itemHandler = createItemHandler();
    }

    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5);
    }

    @Override
    public void update() {
        if (worldObj != null && !worldObj.isRemote) {
            cooldown--;
            if (cooldown <= 0) {
                EnumFacing facing = worldObj.getBlockState(getPos()).getValue(BlockBetterHopper.FACING);
                EnumFacing opposite = facing.getOpposite();
                TileEntity facingEntity = worldObj.getTileEntity(pos.offset(facing));
                IItemHandler targetItemHandler = facingEntity != null ? facingEntity.getCapability(RefinedRelocation2.ITEM_HANDLER, opposite) : null;
                boolean hasSpace = false;
                if (targetItemHandler != null) {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(i);
                        if (itemStack != null) {
                            pushItem(i, targetItemHandler);
                        }
                        if (!hasSpace && (itemStack == null || itemStack.stackSize < itemStack.getMaxStackSize())) {
                            hasSpace = true;
                        }
                    }
                }

                if (hasSpace) {
                    BlockPos upPos = pos.offset(EnumFacing.UP);
                    TileEntity upEntity = worldObj.getTileEntity(upPos);
                    IItemHandler itemHandler = upEntity != null ? upEntity.getCapability(RefinedRelocation2.ITEM_HANDLER, EnumFacing.DOWN) : null;
                    if (itemHandler != null) {
                        pullItem(itemHandler);
                    } else {
                        float range = 0.75f;
                        worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + 1.5f, pos.getZ() + range), EntitySelectors.selectAnything).forEach(this::pullItem);
                    }
                }

                cooldown = 20;
            }
        }
    }

    public void pushItem(int sourceSlot, IItemHandler targetItemHandler) {
        ItemStack sourceStack = itemHandler.extractItem(sourceSlot, 64, true);
        ItemStack restStack = ItemHandlerHelper.insertItem(targetItemHandler, sourceStack, false);
        itemHandler.extractItem(sourceSlot, restStack == null ? sourceStack.stackSize : sourceStack.stackSize - restStack.stackSize, false);
    }

    private void pullItem(IItemHandler sourceItemHandler) {
        for (int i = 0; i < sourceItemHandler.getSlots(); i++) {
            ItemStack sourceStack = sourceItemHandler.extractItem(i, 64, true);
            if(sourceStack != null) {
                ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
                sourceItemHandler.extractItem(i, restStack == null ? 64 : sourceStack.stackSize - restStack.stackSize, false);
            }
        }
    }

    public boolean pullItem(EntityItem entityItem) {
        ItemStack sourceStack = entityItem.getEntityItem();
        ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
        if(restStack != null) {
            entityItem.setEntityItemStack(restStack);
        } else {
            entityItem.setDead();
        }
        return restStack == null || restStack.stackSize < sourceStack.stackSize;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTBase itemHandlerNBT = RefinedRelocation2.ITEM_HANDLER.getStorage().writeNBT(RefinedRelocation2.ITEM_HANDLER, itemHandler, null);
        if(itemHandlerNBT != null) {
            tagCompound.setTag("ItemHandler", itemHandlerNBT);
        }
        if(hasCustomName()) {
            tagCompound.setString("CustomName", customName);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        RefinedRelocation2.ITEM_HANDLER.getStorage().readNBT(RefinedRelocation2.ITEM_HANDLER, itemHandler, null, tagCompound.getTagList("ItemHandler", Constants.NBT.TAG_COMPOUND));
        customName = tagCompound.getString("CustomName");
    }

    public String getName() {
        return hasCustomName() ? customName : "container." + RefinedRelocation2.MOD_ID + ":better_hopper";
    }

    public IChatComponent getDisplayName() {
        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName());
    }

    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == RefinedRelocation2.ITEM_HANDLER) {
            return (T) itemHandler;
        }
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == RefinedRelocation2.ITEM_HANDLER || super.hasCapability(capability, facing);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(pos) == this && entityPlayer.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }
}
