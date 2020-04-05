//package net.blay09.mods.refinedrelocation.compat.ironchest;
//
//import com.progwml6.ironchest.common.block.IronChestBlock;
//import com.progwml6.ironchest.common.block.IronChestsTypes;
//import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
//import net.blay09.mods.refinedrelocation.api.Capabilities;
//import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
//import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
//import net.minecraft.block.Block;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.ITickableTileEntity;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraft.util.Direction;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.items.CapabilityItemHandler;
//import net.minecraftforge.items.wrapper.InvWrapper;
//
//import javax.annotation.Nullable;
//import java.util.Locale;
//
//public class SortingIronChestTileEntity extends GenericIronChestTileEntity implements ITickableTileEntity {
//
//    private final InvWrapper invWrapper = new InvWrapper(this);
//    private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
//    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
//    private boolean isFirstTick;
//
//    public SortingIronChestTileEntity(TileEntityType<?> type, IronChestsTypes chestType, Block blockToUse) {
//        super(type, chestType, () -> blockToUse);
//    }
//
//    public void onContentsChanged(int slot) {
//        markDirty();
//        sortingInventory.onSlotChanged(slot);
//    }
//
//    @Override
//    public void tick() {
//        if (isFirstTick) {
//            sortingInventory.onFirstTick(this);
//            isFirstTick = false;
//        }
//
//        sortingInventory.onUpdate(this);
//    }
//
//    @Override
//    public void remove() {
//        super.remove();
//        sortingInventory.onInvalidate(this);
//    }
//
//    @Override
//    public void onChunkUnloaded() {
//        super.onChunkUnloaded();
//        sortingInventory.onInvalidate(this);
//    }
//
//    @Override
//    public CompoundNBT write(CompoundNBT compound) {
//        super.write(compound);
//        compound.put("SortingInventory", sortingInventory.serializeNBT());
//        compound.put("RootFilter", rootFilter.serializeNBT());
//        return compound;
//    }
//
//    @Override
//    public void read(CompoundNBT compound) {
//        super.read(compound);
//        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));
//
//        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));
//    }
//
//    @Override
//    public ITextComponent getName() {
//        return hasCustomName()
//                ? super.getName()
//                : new TranslationTextComponent("container.refinedrelocation:ironchest.sorting_chest_" + getChestType().name().toLowerCase(Locale.ENGLISH));
//    }
//
//    @Override
//    public void setInventorySlotContents(int index, ItemStack stack) {
//        super.setInventorySlotContents(index, stack);
//        onContentsChanged(index);
//    }
//
//    @Override
//    public ItemStack decrStackSize(int index, int count) {
//        ItemStack itemStack = super.decrStackSize(index, count);
//        onContentsChanged(index);
//        return itemStack;
//    }
//
//    @Nullable
//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
//        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> invWrapper));
//        if (!result.isPresent()) {
//            result = Capabilities.SORTING_GRID_MEMBER.orEmpty(cap, LazyOptional.of(() -> sortingInventory));
//        }
//
//        if (!result.isPresent()) {
//            result = Capabilities.SORTING_INVENTORY.orEmpty(cap, LazyOptional.of(() -> sortingInventory));
//        }
//
//        if (!result.isPresent()) {
//            result = Capabilities.SIMPLE_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
//        }
//
//        if (!result.isPresent()) {
//            result = Capabilities.ROOT_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
//        }
//
//        return super.getCapability(cap, side);
//    }
//
//    public static class Iron extends SortingIronChestTileEntity {
//        public Iron() {
//            super(IronChestAddon.sortingIronChestTile, IronChestsTypes.IRON, IronChestAddon.sortingIronChest);
//        }
//    }
//
//    /*public static class Dirt extends SortingIronChestTileEntity {
//        public Dirt() {
//            super(IronChestsTypes.DIRT);
//        }
//    }
//
//    public static class Obsidian extends SortingIronChestTileEntity {
//        public Obsidian() {
//            super(IronChestsTypes.OBSIDIAN);
//        }
//    }
//
//    public static class Crystal extends SortingIronChestTileEntity {
//        public Crystal() {
//            super(IronChestsTypes.CRYSTAL);
//        }
//    }
//
//    public static class Diamond extends SortingIronChestTileEntity {
//        public Diamond() {
//            super(IronChestsTypes.DIAMOND);
//        }
//    }
//
//    public static class Copper extends SortingIronChestTileEntity {
//        public Copper() {
//            super(IronChestsTypes.COPPER);
//        }
//    }
//
//    public static class Gold extends SortingIronChestTileEntity {
//        public Gold() {
//            super(IronChestsTypes.GOLD);
//        }
//    }
//
//    public static class Silver extends SortingIronChestTileEntity {
//        public Silver() {
//            super(IronChestsTypes.SILVER);
//        }
//    }*/
//
//}
