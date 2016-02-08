package net.blay09.mods.refinedrelocation2.tile;

import com.google.common.collect.Sets;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.container.ContainerSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Iterator;
import java.util.Set;

public class TileSortingChest extends TileEntity implements ITickable, IWorldPos {

    private final ItemStackHandler itemHandler;
    private final ISortingInventory sortingInventory;
    private final Set<EntityPlayer> chestPlayers = Sets.newHashSet();

    public float lidAngle;
    public float prevLidAngle;
    private int ticksSinceSync;
    private int numPlayersUsing;

    private String customName;

    public TileSortingChest() {
        itemHandler = new ItemStackHandler(27);
        sortingInventory = RefinedRelocationAPI.createSortingInventory(this, itemHandler, true);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public String getName() {
        return hasCustomName() ? customName : "container." + RefinedRelocation2.MOD_ID + ":sorting_chest";
    }

    public IChatComponent getDisplayName() {
        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName());
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        RefinedRelocation2.ITEM_HANDLER.getStorage().readNBT(RefinedRelocation2.ITEM_HANDLER, itemHandler, null, tagCompound.getTagList("ItemHandler", Constants.NBT.TAG_COMPOUND));
        customName = tagCompound.getString("CustomName");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTBase itemHandlerNBT = RefinedRelocation2.ITEM_HANDLER.getStorage().writeNBT(RefinedRelocation2.ITEM_HANDLER, itemHandler, null);
        if(itemHandlerNBT != null) {
            tagCompound.setTag("ItemHandler", itemHandlerNBT);
        }
        if (hasCustomName()) {
            tagCompound.setString("CustomName", customName);
        }
    }

    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(pos) == this && entityPlayer.getDistanceSq(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d) <= 64d;
    }

    public void openInventory(EntityPlayer entityPlayer) {
        if (!entityPlayer.isSpectator()) {
            chestPlayers.add(entityPlayer);
            numPlayersUsing = chestPlayers.size();
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }
    }

    public void closeInventory(EntityPlayer entityPlayer) {
        if (!entityPlayer.isSpectator()) {
            chestPlayers.remove(entityPlayer);
            numPlayersUsing = chestPlayers.size();
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        if (id == 1) {
            numPlayersUsing = value;
            return true;
        }
        return super.receiveClientEvent(id, value);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        sortingInventory.invalidate();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
    }

    private boolean firstTick = true;
    @Override
    public void update() {
        // onLoad is *NOT* a viable replacement for firstTick stuff, as it is called before the chunk is fully loaded, meaning that any world access will result in infinite-loading of the same chunk
        if(firstTick) {
            sortingInventory.onFirstTick();
            firstTick = false;
        }

        ticksSinceSync++;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (!worldObj.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            Iterator<EntityPlayer> it = chestPlayers.iterator();
            while(it.hasNext()) {
                EntityPlayer entityPlayer = it.next();
                if(entityPlayer.isDead || ((ContainerSortingChest) entityPlayer.openContainer).getTileEntity() != this) {
                    it.remove();
                }
            }
            numPlayersUsing = chestPlayers.size();
            worldObj.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }

        prevLidAngle = lidAngle;
        float lidAngleSpeed = 0.1f;
        if (numPlayersUsing > 0 && lidAngle == 0f) {
            worldObj.playSoundEffect(x + 0.5d, y + 0.5d, z + 0.5d, "random.chestopen", 0.5f, worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            float oldLidAngle = lidAngle;
            if (numPlayersUsing > 0) {
                lidAngle += lidAngleSpeed;
            } else {
                lidAngle -= lidAngleSpeed;
            }

            if (lidAngle > 1f) {
                lidAngle = 1f;
            }

            float closeSoundAngle = 0.5f;
            if (lidAngle < closeSoundAngle && oldLidAngle >= closeSoundAngle) {
                worldObj.playSoundEffect(x + 0.5d, y + 0.5d, z + 0.5d, "random.chestclosed", 0.5f, worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }

            if (lidAngle < 0f) {
                lidAngle = 0f;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == RefinedRelocation2.SORTING_INVENTORY || capability == RefinedRelocation2.SORTING_GRID_MEMBER) {
            return (T) sortingInventory;
        }
        if(capability == RefinedRelocation2.ITEM_HANDLER) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }
}
