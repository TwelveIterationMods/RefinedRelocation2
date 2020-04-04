package net.blay09.mods.refinedrelocation.tile;


import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.container.SortingChestContainer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SortingChestTileEntity extends TileMod implements ITickableTileEntity,
        INamedContainerProvider,
        INameable,
        IChestLid {

    private static final int EVENT_NUM_PLAYERS = 1;

    private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
            sortingInventory.onSlotChanged(slot);
        }
    };

    private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);

    private float lidAngle;
    private float prevLidAngle;
    private int numPlayersUsing;
    private int ticksSinceSync;

    private ITextComponent customName;

    public SortingChestTileEntity() {
        super(ModTileEntities.sortingChest);
    }

    @Override
    public void onFirstTick() {
        sortingInventory.onFirstTick(this);
    }

    @Override
    public void tick() {
        baseTick();
        sortingInventory.onUpdate(this);

        if (!world.isRemote && numPlayersUsing != 0 && (ticksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0) {
            numPlayersUsing = calculatePlayersUsing();
        }

        prevLidAngle = lidAngle;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (numPlayersUsing > 0 && lidAngle == 0f) {
            world.playSound(null,
                    x + 0.5,
                    y + 0.5,
                    z + 0.5,
                    SoundEvents.BLOCK_CHEST_OPEN,
                    SoundCategory.BLOCKS,
                    0.5f,
                    world.rand.nextFloat() * 0.1f + 0.9f);
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
                world.playSound(null,
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        SoundEvents.BLOCK_CHEST_CLOSE,
                        SoundCategory.BLOCKS,
                        0.5f,
                        world.rand.nextFloat() * 0.1f + 0.9f);
            }

            if (lidAngle < 0f) {
                lidAngle = 0f;
            }
        }
    }

    private int calculatePlayersUsing() {
        int result = 0;
        float distance = 5f;

        for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class,
                new AxisAlignedBB((float) pos.getX() - distance,
                        (float) pos.getY() - distance,
                        (float) pos.getZ() - distance,
                        (float) (pos.getX() + 1) + distance,
                        (float) (pos.getY() + 1) + distance,
                        (float) (pos.getZ() + 1) + distance))) {
            if (player.openContainer instanceof SortingChestContainer) {
                if (((SortingChestContainer) player.openContainer).getTileEntity() == this) {
                    ++result;
                }
            }
        }

        return result;
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
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        sortingInventory.deserializeNBT(compound.getCompound("SortingInventory"));

        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));

        customName = compound.contains("CustomName")
                ? new StringTextComponent(compound.getString("CustomName"))
                : null;
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

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:sorting_chest";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(getUnlocalizedName());
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SortingChestContainer(i, playerInventory, this);
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
    public boolean receiveClientEvent(int id, int value) {
        if (id == EVENT_NUM_PLAYERS) {
            numPlayersUsing = value;
            return true;
        }

        return super.receiveClientEvent(id, value);
    }

    public void openChest(PlayerEntity player) {
        if (!player.isSpectator()) {
            numPlayersUsing = Math.max(0, numPlayersUsing + 1);
            world.addBlockEvent(pos, Blocks.ENDER_CHEST, EVENT_NUM_PLAYERS, numPlayersUsing);
        }
    }

    public void closeChest(PlayerEntity player) {
        if (!player.isSpectator()) {
            numPlayersUsing = Math.max(0, numPlayersUsing - 1);
            world.addBlockEvent(pos, Blocks.ENDER_CHEST, EVENT_NUM_PLAYERS, numPlayersUsing);
        }
    }

    @Override
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }
}
