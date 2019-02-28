package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTiles;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.util.DoorAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSortingChest extends TileMod implements ITickable, IInteractionObject, IChestLid {

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

    public TileSortingChest() {
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
    public void read(NBTTagCompound compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));

        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));

        customName = compound.contains("CustomName") ? new TextComponentString(compound.getString("CustomName")) : null;
    }

    @Override
    public void readFromNBTSynced(NBTTagCompound compound) {
        setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
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
    public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
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

        return result;
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

    public void setCustomName(String customName) {
        this.customName = new TextComponentString(customName);
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public ITextComponent getName() {
        return customName != null ? customName : new TextComponentTranslation(getUnlocalizedName());
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
