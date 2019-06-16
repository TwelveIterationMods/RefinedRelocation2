package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTiles;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.util.DoorAnimator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SortingChestTileEntity extends TileMod implements ITickableTileEntity, INamedContainerProvider, INameable, IChestLid {

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

    private ITextComponent customName;

    public SortingChestTileEntity() {
        super(ModTiles.sortingChest);
        doorAnimator.setSoundEventOpen(SoundEvents.BLOCK_CHEST_OPEN);
        doorAnimator.setSoundEventClose(SoundEvents.BLOCK_CHEST_CLOSE);
    }

    @Override
    public void onLoad() {
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

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));

        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));

        customName = compound.contains("CustomName") ? new StringTextComponent(compound.getString("CustomName")) : null;
    }

    @Override
    public void readFromNBTSynced(CompoundNBT compound) {
        setCustomName(compound.getString("CustomName"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("ItemHandler", itemHandler.serializeNBT());
        compound.put("SortingInventory", sortingInventory.serializeNBT());
        compound.put("RootFilter", rootFilter.serializeNBT());
        if (customName != null) {
            compound.putString("CustomName", customName.getUnformattedComponentText());
        }

        return compound;
    }

    @Override
    public CompoundNBT writeToNBTSynced(CompoundNBT compound) {
        if (customName != null) {
            compound.putString("CustomName", customName.getUnformattedComponentText());
        }
        return compound;
    }


    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
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

        return result;
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:sorting_chest";
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerSortingChest(i, playerInventory, this);
    }

    public void setCustomName(String customName) {
        this.customName = new StringTextComponent(customName);
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public ITextComponent getName() {
        return customName != null ? customName : new TranslationTextComponent(getUnlocalizedName());
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return customName;
    }

    @Override
    public float getLidAngle(float partialTicks) {
        return 0;
    }
}
