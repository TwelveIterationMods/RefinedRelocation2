//package net.blay09.mods.refinedrelocation.compat.ironchest;
//
//import com.progwml6.ironchest.common.blocks.IronChestType;
//import com.progwml6.ironchest.common.tileentity.TileEntityIronChest;
//import net.blay09.mods.refinedrelocation.api.Capabilities;
//import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
//import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
//import net.minecraft.block.Block;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.ITickable;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.items.CapabilityItemHandler;
//import net.minecraftforge.items.wrapper.InvWrapper;
//
//import javax.annotation.Nullable;
//import java.util.Locale;
//
//public class TileSortingIronChest extends TileEntityIronChest implements ITickable {
//
//    private final InvWrapper invWrapper = new InvWrapper(this);
//    private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
//    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
//
//    public TileSortingIronChest(TileEntityType<?> type, IronChestType chestType, Block blockToUse) {
//        super(type, chestType, blockToUse);
//    }
//
//    public void onContentsChanged(int slot) {
//        markDirty();
//        sortingInventory.onSlotChanged(slot);
//    }
//
//    @Override
//    public void onLoad() {
//        super.onLoad();
//        sortingInventory.onLoad(this);
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
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
//    public NBTTagCompound write(NBTTagCompound compound) {
//        super.write(compound);
//        compound.put("SortingInventory", sortingInventory.serializeNBT());
//        compound.put("RootFilter", rootFilter.serializeNBT());
//        return compound;
//    }
//
//    @Override
//    public void read(NBTTagCompound compound) {
//        super.read(compound);
//        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));
//
//        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));
//    }
//
//    @Override
//    public ITextComponent getName() {
//        return hasCustomName() ? super.getName() : "container.refinedrelocation:ironchest.sorting_chest_" + getIronChestType().name().toLowerCase(Locale.ENGLISH);
//    }
//
//    @Override
//    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
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
//    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable EnumFacing side) {
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
//    public static class Iron extends TileSortingIronChest {
//        public Iron() {
//            super(IronChestType.IRON);
//        }
//    }
//
//    public static class Dirt extends TileSortingIronChest {
//        public Dirt() {
//            super(IronChestType.DIRTCHEST9000);
//        }
//    }
//
//    public static class Obsidian extends TileSortingIronChest {
//        public Obsidian() {
//            super(IronChestType.OBSIDIAN);
//        }
//    }
//
//    public static class Crystal extends TileSortingIronChest {
//        public Crystal() {
//            super(IronChestType.CRYSTAL);
//        }
//    }
//
//    public static class Diamond extends TileSortingIronChest {
//        public Diamond() {
//            super(IronChestType.DIAMOND);
//        }
//    }
//
//    public static class Copper extends TileSortingIronChest {
//        public Copper() {
//            super(IronChestType.COPPER);
//        }
//    }
//
//    public static class Gold extends TileSortingIronChest {
//        public Gold() {
//            super(IronChestType.GOLD);
//        }
//    }
//
//    public static class Silver extends TileSortingIronChest {
//        public Silver() {
//            super(IronChestType.SILVER);
//        }
//    }
//
//}
