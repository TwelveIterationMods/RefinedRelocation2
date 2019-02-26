package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.INameTaggable;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.util.DoorAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSortingChest extends TileMod implements ITickable, IInteractionObject {

    private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            sortingInventory.onSlotChanged(slot);
        }
    };

    private final DoorAnimator doorAnimator = new DoorAnimator(this, 0, 1);

    private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
    private final INameTaggable nameTaggable = Capabilities.getDefaultInstance(Capabilities.NAME_TAGGABLE);

    public TileSortingChest() {
        doorAnimator.setSoundEventOpen(SoundEvents.BLOCK_CHEST_OPEN);
        doorAnimator.setSoundEventClose(SoundEvents.BLOCK_CHEST_CLOSE);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        sortingInventory.onLoad(this);
    }

    @Override
    public void tick() {
        sortingInventory.onUpdate(this);
        doorAnimator.update();
    }

    @Override
    public void remove() {
        super.remove();
        sortingInventory.onInvalidate(this);
    }

    @Override
    public void onChunkUnloaded() {
        sortingInventory.onInvalidate(this);
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    /**
     * TODO remove in 1.13
     */
    public static void fixRootFilterTag(NBTTagCompound compound) {
        if (compound.getTagId("RootFilter") == Constants.NBT.TAG_LIST) {
            NBTTagList tagList = compound.getList("RootFilter", Constants.NBT.TAG_COMPOUND);
            compound.remove("RootFilter");
            NBTTagCompound rootFilter = new NBTTagCompound();
            rootFilter.put("FilterList", tagList);
            compound.put("RootFilter", rootFilter);
        }
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));

        fixRootFilterTag(compound);

        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));
        nameTaggable.deserializeNBT(compound.getCompound("NameTaggable"));
    }

    @Override
    public void readFromNBTSynced(NBTTagCompound compound) {
        nameTaggable.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        compound.put("ItemHandler", itemHandler.serializeNBT());
        compound.put("SortingInventory", sortingInventory.serializeNBT());
        compound.put("RootFilter", rootFilter.serializeNBT());
        compound.put("NameTaggable", nameTaggable.serializeNBT());
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
        compound.putString("CustomName", nameTaggable.getCustomName());
        return compound;
    }


    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
        if (!result.isPresent()) {
            result = Capabilities.SORTING_GRID_MEMBER.orEmpty(cap, LazyOptional.of(() -> sortingInventory));
        }

        if (!result.isPresent()) {
            result = Capabilities.SORTING_INVENTORY.orEmpty(cap, LazyOptional.of(() -> sortingInventory));
        }

        if (!result.isPresent()) {
            result = Capabilities.ROOT_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

        if (!result.isPresent()) {
            result = Capabilities.SIMPLE_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

        if (!result.isPresent()) {
            result = Capabilities.NAME_TAGGABLE.orEmpty(cap, LazyOptional.of(() -> nameTaggable));
        }

        return result;
    }

    @Override
    public ITextComponent getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return nameTaggable.getDisplayName();
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:sorting_chest";
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerSortingChest(entityPlayer, this);
    }

    @Override
    public String getGuiID() {
        return "refinedrelocation:sorting_chest";
    }
}
