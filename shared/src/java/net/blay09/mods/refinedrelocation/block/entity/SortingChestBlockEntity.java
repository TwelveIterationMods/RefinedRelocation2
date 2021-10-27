package net.blay09.mods.refinedrelocation.block.entity;


import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.grid.SortingInventory;
import net.blay09.mods.refinedrelocation.menu.SortingChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@OnlyIn(value = Dist.CLIENT, _interface = LidBlockEntity.class)
public class SortingChestBlockEntity extends BalmBlockEntity implements BalmMenuProvider,
        Nameable,
        LidBlockEntity,
        Clearable {

    private static final int EVENT_NUM_PLAYERS = 1;

    private final ItemStackHandler itemHandler;

    private final ISortingInventory sortingInventory = new SortingInventory();
    private final IRootFilter rootFilter = new RootFilter();
    private final SortingChestType chestType;

    private float lidAngle;
    private float prevLidAngle;
    private int numPlayersUsing;
    private int ticksSinceSync;

    private Component customName;

    public SortingChestBlockEntity(SortingChestType chestType, BlockPos pos, BlockState state) {
        super(chestType.getTileEntityType(), pos, state);
        this.chestType = chestType;

        itemHandler = new ItemStackHandler(chestType.getInventorySize()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                sortingInventory.onSlotChanged(slot);
            }
        };
    }

    public SortingChestType getChestType() {
        return chestType;
    }

    @Override
    public void balmOnLoad() {
        sortingInventory.onFirstTick(this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SortingChestBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        sortingInventory.onUpdate(this);

        ticksSinceSync++;
        if (numPlayersUsing != 0 && (ticksSinceSync + worldPosition.getX() + worldPosition.getY() + worldPosition.getZ()) % 200 == 0) {
            numPlayersUsing = calculatePlayersUsing();
        }

        prevLidAngle = lidAngle;
        int x = worldPosition.getX();
        int y = worldPosition.getY();
        int z = worldPosition.getZ();
        if (numPlayersUsing > 0 && lidAngle == 0f) {
            level.playSound(null,
                    x + 0.5,
                    y + 0.5,
                    z + 0.5,
                    SoundEvents.CHEST_OPEN,
                    SoundSource.BLOCKS,
                    0.5f,
                    level.random.nextFloat() * 0.1f + 0.9f);
        }

        if (numPlayersUsing == 0 && lidAngle > 0f || numPlayersUsing > 0 && lidAngle < 1f) {
            float oldLidAngle = lidAngle;
            if (numPlayersUsing > 0) {
                lidAngle += 0.1f;
            } else {
                lidAngle -= 0.1f;
            }

            if (lidAngle > 1f) {
                lidAngle = 1f;
            }

            if (lidAngle < 0.5f && oldLidAngle >= 0.5f) {
                level.playSound(null,
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        SoundEvents.CHEST_CLOSE,
                        SoundSource.BLOCKS,
                        0.5f,
                        level.random.nextFloat() * 0.1f + 0.9f);
            }

            if (lidAngle < 0f) {
                lidAngle = 0f;
            }
        }
    }

    private int calculatePlayersUsing() {
        int result = 0;
        float distance = 5f;

        for (Player player : level.getEntitiesOfClass(Player.class,
                new AABB((float) worldPosition.getX() - distance,
                        (float) worldPosition.getY() - distance,
                        (float) worldPosition.getZ() - distance,
                        (float) (worldPosition.getX() + 1) + distance,
                        (float) (worldPosition.getY() + 1) + distance,
                        (float) (worldPosition.getZ() + 1) + distance))) {
            if (player.containerMenu instanceof SortingChestMenu menu) {
                if (menu.getTileEntity() == this) {
                    ++result;
                }
            }
        }

        return result;
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
        CompoundTag itemHandlerCompound = compound.getCompound("ItemHandler");
        itemHandlerCompound.putInt("Size", chestType.getInventorySize());
        this.itemHandler.deserializeNBT(itemHandlerCompound);

        sortingInventory.deserialize(compound.getCompound("SortingInventory"));

        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));

        customName = compound.contains("CustomName")
                ? new TextComponent(compound.getString("CustomName"))
                : null;
    }

    @Override
    public void balmFromClientTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.put("ItemHandler", itemHandler.serializeNBT());
        compound.put("SortingInventory", sortingInventory.serialize());
        compound.put("RootFilter", rootFilter.serializeNBT());
        if (customName != null) {
            compound.putString("CustomName", customName.getString());
        }

        return compound;
    }

    @Override
    public CompoundTag balmToClientTag(CompoundTag tag) {
        return save(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result = super.getCapability(cap, side);
        if (!result.isPresent()) {
            result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
        }

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

    public String getUnlocalizedName() {
        return "container.refinedrelocation:" + chestType.getRegistryName();
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(getUnlocalizedName());
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new SortingChestMenu(i, playerInventory, this);
    }

    public void setCustomName(String customName) {
        this.customName = new TextComponent(customName);
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public Component getName() {
        return customName != null ? customName : new TranslatableComponent(getUnlocalizedName());
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    @Override
    public boolean triggerEvent(int id, int value) {
        if (id == EVENT_NUM_PLAYERS) {
            numPlayersUsing = value;
            return true;
        }

        return super.triggerEvent(id, value);
    }

    public void openChest(Player player) {
        if (!player.isSpectator()) {
            numPlayersUsing = Math.max(0, numPlayersUsing + 1);
            level.blockEvent(worldPosition, Blocks.ENDER_CHEST, EVENT_NUM_PLAYERS, numPlayersUsing);
        }
    }

    public void closeChest(Player player) {
        if (!player.isSpectator()) {
            numPlayersUsing = Math.max(0, numPlayersUsing - 1);
            level.blockEvent(worldPosition, Blocks.ENDER_CHEST, EVENT_NUM_PLAYERS, numPlayersUsing);
        }
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    public int getNumPlayersUsing() {
        return numPlayersUsing;
    }

    public void restoreItems(ListTag items) {
        for (Tag item : items) {
            CompoundTag compound = (CompoundTag) item;
            int slot = compound.getByte("Slot");
            ItemStack itemStack = ItemStack.of(compound);
            ItemStack rest = itemHandler.insertItem(slot, itemStack, false);
            if (!rest.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5f, worldPosition.getY() + 0.5f, worldPosition.getZ() + 0.5, rest));
            }
        }
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
