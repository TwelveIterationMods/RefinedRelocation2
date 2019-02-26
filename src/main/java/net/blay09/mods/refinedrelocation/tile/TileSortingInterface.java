package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTiles;
import net.blay09.mods.refinedrelocation.RefinedRelocationConfig;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class TileSortingInterface extends TileMod implements ITickable, IDroppableItemHandler {

    private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);

    private TileEntity cachedConnectedTile;
    private ItemStack[] lastInventory;
    private int currentDetectionSlot;

    public TileSortingInterface() {
        super(ModTiles.sortingInterface);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        sortingInventory.onLoad(this);
    }

    @Override
    public void tick() {
        baseTick();

        if (cachedConnectedTile == null) {
            cachedConnectedTile = world.getTileEntity(pos.offset(getFacing()));
        } else if (cachedConnectedTile.isRemoved()) {
            cachedConnectedTile = null;
            lastInventory = null;
        }

        sortingInventory.onUpdate(this);

        if (!world.isRemote) {
            LazyOptional<IItemHandler> itemHandlerCap = sortingInventory.getItemHandler();
            itemHandlerCap.ifPresent(itemHandler -> {
                int inventorySize = itemHandler.getSlots();

                // Create a copy of the target inventory so we can compare and detect changes
                if (lastInventory == null || inventorySize != lastInventory.length) {
                    lastInventory = new ItemStack[itemHandler.getSlots()];
                    for (int i = 0; i < inventorySize; i++) {
                        ItemStack currentStack = itemHandler.getStackInSlot(i);
                        lastInventory[i] = currentStack.isEmpty() ? ItemStack.EMPTY : currentStack.copy();
                    }
                    currentDetectionSlot = 0;
                }

                // Detect changes in the target inventory, nine slots at a time
                for (int j = 0; j < Math.min(RefinedRelocationConfig.COMMON.sortingInterfaceSlotsPerTick.get(), inventorySize); j++) {
                    int i = currentDetectionSlot;
                    ItemStack prevStack = lastInventory[i];
                    ItemStack currentStack = itemHandler.getStackInSlot(i);
                    if (!ItemStack.areItemsEqual(prevStack, currentStack) || !ItemStack.areItemStackTagsEqual(prevStack, currentStack)) {
                        sortingInventory.onSlotChanged(i);
                        lastInventory[i] = currentStack.isEmpty() ? ItemStack.EMPTY : currentStack.copy();
                    }

                    currentDetectionSlot++;
                    if (currentDetectionSlot >= inventorySize) {
                        currentDetectionSlot = 0;
                    }
                }
            });
        }
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

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));
        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));
    }

    @Override
    public void readFromNBTSynced(NBTTagCompound compound) {
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        compound.put("SortingInventory", sortingInventory.serializeNBT());
        compound.put("RootFilter", rootFilter.serializeNBT());
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
        return compound;
    }

    @Override
    protected void onFirstTick() {
        cachedConnectedTile = world.getTileEntity(pos.offset(getFacing()));
    }

    public EnumFacing getFacing() {
        return getBlockState().get(BlockStateProperties.FACING);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        LazyOptional<T> result = Capabilities.ROOT_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        if (!result.isPresent()) {
            result = Capabilities.SIMPLE_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

        if (!result.isPresent()) {
            result = Capabilities.SIMPLE_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

        if (!result.isPresent()) {
            result = Capabilities.SORTING_INVENTORY.orEmpty(cap, LazyOptional.of(() -> sortingInventory));
        }

        if (!result.isPresent()) {
            result = Capabilities.SORTING_GRID_MEMBER.orEmpty(cap, LazyOptional.of(() -> sortingInventory));
        }

        if (!result.isPresent() && cachedConnectedTile != null) {
            return cachedConnectedTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getFacing().getOpposite()).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:sorting_interface";
    }

    @Override
    public Collection<IItemHandler> getDroppedItemHandlers() {
        // Do not drop the connected inventory's items.
        return Collections.emptyList();
    }

}
