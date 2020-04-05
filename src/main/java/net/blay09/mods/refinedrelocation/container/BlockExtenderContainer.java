package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.SlotItemHandler;

public class BlockExtenderContainer extends BaseContainer {

    public static final String KEY_TOGGLE_SIDE = "ToggleSide";
    public static final String KEY_SIDE_INDEX = "SideIndex";
    public static final String KEY_SIDE_MAPPING = "SideMapping";
    public static final String KEY_STACK_LIMITER = "StackLimiter";
    public static final String KEY_CONFIGURE_INPUT_FILTER = "ConfigureInputFilter";
    public static final String KEY_CONFIGURE_OUTPUT_FILTER = "ConfigureOutputFilter";

    private final PlayerEntity player;
    private final TileBlockExtender tileEntity;
    private Direction clickedFace;

    private final Direction[] lastSideMapping = new Direction[5];
    private int lastStackLimiterLimit;

    public BlockExtenderContainer(int windowId, PlayerInventory playerInventory, TileBlockExtender tileEntity) {
        super(ModContainers.blockExtender, windowId);

        this.player = playerInventory.player;
        this.tileEntity = tileEntity;

        for (int i = 0; i < 3; i++) {
            addSlot(new SlotItemHandler(tileEntity.getItemHandlerUpgrades(), i, 152, 22 + i * 18));
        }

        addPlayerInventory(playerInventory, 94);
    }

    public TileBlockExtender getTileEntity() {
        return tileEntity;
    }

    public Direction getClickedFace() {
        return clickedFace;
    }

    public void setClickedFace(Direction clickedFace) {
        this.clickedFace = clickedFace;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < lastSideMapping.length; i++) {
            Direction nowSideMapping = tileEntity.getSideMapping(RelativeSide.fromIndex(i));
            if (lastSideMapping[i] != nowSideMapping) {
                CompoundNBT compound = new CompoundNBT();
                compound.putByte(KEY_SIDE_INDEX, (byte) i);
                compound.putByte(KEY_SIDE_MAPPING, nowSideMapping != null ? (byte) nowSideMapping.getIndex() : -1);
                RefinedRelocationAPI.syncContainerValue(KEY_SIDE_MAPPING, compound, listeners);
                lastSideMapping[i] = nowSideMapping;
            }
        }

        int nowStackLimiterLimit = tileEntity.getStackLimiterLimit();
        if (lastStackLimiterLimit != nowStackLimiterLimit) {
            RefinedRelocationAPI.syncContainerValue(KEY_STACK_LIMITER, tileEntity.getStackLimiterLimit(), listeners);
            lastStackLimiterLimit = nowStackLimiterLimit;
        }
    }

    @Override
    public void receivedMessageServer(IContainerMessage message) {
        switch (message.getKey()) {
            case KEY_TOGGLE_SIDE:
                RelativeSide side = RelativeSide.fromIndex(message.getIndex());
                if (side != RelativeSide.FRONT) {
                    int facingIdx = message.getIntValue();
                    Direction facing = facingIdx == -1 ? null : Direction.byIndex(facingIdx);
                    tileEntity.setSideMapping(side, facing);
                    lastSideMapping[side.ordinal()] = facing;
                }
                break;
            case KEY_STACK_LIMITER:
                int stackSizeLimit = MathHelper.clamp(message.getIntValue(), 1, Items.AIR.getItemStackLimit(ItemStack.EMPTY));
                tileEntity.setStackLimiterLimit(stackSizeLimit);
                lastStackLimiterLimit = stackSizeLimit;
                break;
            case KEY_CONFIGURE_INPUT_FILTER:
                tileEntity.getInputFilter().ifPresent(it -> {
                    INamedContainerProvider config = it.getConfiguration(player, tileEntity);
                    if (config != null) {
                        NetworkHooks.openGui((ServerPlayerEntity) player, config, tileEntity.getPos());
                    }
                });
                break;
            case KEY_CONFIGURE_OUTPUT_FILTER:
                tileEntity.getOutputFilter().ifPresent(it -> {
                    INamedContainerProvider config = it.getConfiguration(player, tileEntity);
                    if (config != null) {
                        NetworkHooks.openGui((ServerPlayerEntity) player, config, tileEntity.getPos());
                    }
                });
                break;
        }
    }

    @Override
    public void receivedMessageClient(IContainerMessage message) {
        if (message.getKey().equals(KEY_SIDE_MAPPING)) {
            CompoundNBT compound = message.getNBTValue();
            tileEntity.setSideMapping(RelativeSide.fromIndex(compound.getByte(KEY_SIDE_INDEX)), Direction.byIndex(compound.getByte(KEY_SIDE_MAPPING)));
        } else if (message.getKey().equals(KEY_STACK_LIMITER)) {
            tileEntity.setStackLimiterLimit(message.getIntValue());
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if(index < 3) {
                if (!mergeItemStack(slotStack, 3, 30, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 30) {
                if (!mergeItemStack(slotStack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                } else if (!mergeItemStack(slotStack, 27, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 30, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return !tileEntity.isRemoved() && player.getDistanceSq(tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5) <= 64;
    }

    public Slot getUpgradeSlot(int i) {
        return inventorySlots.get(i);
    }

}
