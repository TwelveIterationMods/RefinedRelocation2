package net.blay09.mods.refinedrelocation.block.entity;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.config.RefinedRelocationConfig;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.grid.SortingInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class SortingInterfaceBlockEntity extends BalmBlockEntity implements IDroppableItemHandler {

    private final ISortingInventory sortingInventory = new SortingInventory();
    private final IRootFilter rootFilter = new RootFilter();

    private BlockEntity cachedConnectedTile;
    private ItemStack[] lastInventory;
    private int currentDetectionSlot;

    public SortingInterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.sortingInterface.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SortingInterfaceBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (cachedConnectedTile == null) {
            cachedConnectedTile = level.getBlockEntity(worldPosition.relative(getFacing()));
        } else if (cachedConnectedTile.isRemoved()) {
            cachedConnectedTile = null;
            lastInventory = null;
        }

        sortingInventory.onUpdate(this);

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
                for (int j = 0; j < Math.min(RefinedRelocationConfig.getActive().sortingInterfaceSlotsPerTick, inventorySize); j++) {
                    int i = currentDetectionSlot;
                    ItemStack prevStack = lastInventory[i];
                    ItemStack currentStack = itemHandler.getStackInSlot(i);
                    if (!ItemStack.isSame(prevStack, currentStack) || !ItemStack.isSameItemSameTags(prevStack, currentStack)) {
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

    @Override
    public void setRemoved() {
        super.setRemoved();
        sortingInventory.onInvalidate(this);
    }

    @Override
    public void onChunkUnloaded() {
        sortingInventory.onInvalidate(this);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        sortingInventory.deserialize(compound.getCompound("SortingInventory"));
        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.put("SortingInventory", sortingInventory.serialize());
        compound.put("RootFilter", rootFilter.serializeNBT());
        return compound;
    }

    @Override
    public void balmOnLoad() {
        cachedConnectedTile = level.getBlockEntity(worldPosition.relative(getFacing()));

        sortingInventory.onFirstTick(this);
    }

    public Direction getFacing() {
        return getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result = super.getCapability(cap, side);
        if (!result.isPresent()) {
            result = Capabilities.ROOT_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

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

        if (!result.isPresent() && cachedConnectedTile != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return cachedConnectedTile.getCapability(cap, getFacing().getOpposite()).cast();
        }

        return result;
    }

    public String getUnlocalizedName() {
        return "container.refinedrelocation:sorting_interface";
    }

    @Override
    public Collection<IItemHandler> getDroppedItemHandlers() {
        // Do not drop the connected inventory's items.
        return Collections.emptyList();
    }

}
