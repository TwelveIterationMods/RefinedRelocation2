package net.blay09.mods.refinedrelocation.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IMenuMessage;
import net.blay09.mods.refinedrelocation.block.entity.BlockExtenderBlockEntity;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.SlotItemHandler;

public class BlockExtenderMenu extends AbstractBaseMenu {

    public static final String KEY_TOGGLE_SIDE = "ToggleSide";
    public static final String KEY_SIDE_INDEX = "SideIndex";
    public static final String KEY_SIDE_MAPPING = "SideMapping";
    public static final String KEY_STACK_LIMITER = "StackLimiter";
    public static final String KEY_CONFIGURE_INPUT_FILTER = "ConfigureInputFilter";
    public static final String KEY_CONFIGURE_OUTPUT_FILTER = "ConfigureOutputFilter";

    private final Player player;
    private final BlockExtenderBlockEntity blockExtender;

    private final Direction[] lastSideMapping = new Direction[5];
    private int lastStackLimiterLimit;

    public BlockExtenderMenu(int windowId, Inventory playerInventory, BlockExtenderBlockEntity blockExtender) {
        super(ModMenus.blockExtender.get(), windowId);

        this.player = playerInventory.player;
        this.blockExtender = blockExtender;

        for (int i = 0; i < 3; i++) {
            addSlot(new SlotItemHandler(blockExtender.getItemHandlerUpgrades(), i, 152, 22 + i * 18));
        }

        addPlayerInventory(playerInventory, 8, 94);
    }

    public BlockExtenderBlockEntity getBlockEntity() {
        return blockExtender;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        for (int i = 0; i < lastSideMapping.length; i++) {
            Direction nowSideMapping = blockExtender.getSideMapping(RelativeSide.fromIndex(i));
            if (lastSideMapping[i] != nowSideMapping) {
                CompoundTag compound = new CompoundTag();
                compound.putByte(KEY_SIDE_INDEX, (byte) i);
                compound.putByte(KEY_SIDE_MAPPING, nowSideMapping != null ? (byte) nowSideMapping.get3DDataValue() : -1);
                RefinedRelocationAPI.syncContainerValue(KEY_SIDE_MAPPING, compound, containerListeners());
                lastSideMapping[i] = nowSideMapping;
            }
        }

        int nowStackLimiterLimit = blockExtender.getStackLimiterLimit();
        if (lastStackLimiterLimit != nowStackLimiterLimit) {
            RefinedRelocationAPI.syncContainerValue(KEY_STACK_LIMITER, blockExtender.getStackLimiterLimit(), containerListeners());
            lastStackLimiterLimit = nowStackLimiterLimit;
        }
    }

    @Override
    public void receivedMessageServer(IMenuMessage message) {
        switch (message.getKey()) {
            case KEY_TOGGLE_SIDE -> {
                RelativeSide side = RelativeSide.fromIndex(message.getIndex());
                if (side != RelativeSide.FRONT) {
                    int facingIdx = message.getIntValue();
                    Direction facing = facingIdx == -1 ? null : Direction.from3DDataValue(facingIdx);
                    blockExtender.setSideMapping(side, facing);
                    lastSideMapping[side.ordinal()] = facing;
                }
            }
            case KEY_STACK_LIMITER -> {
                int stackSizeLimit = Mth.clamp(message.getIntValue(), 1, Items.AIR.getItemStackLimit(ItemStack.EMPTY));
                blockExtender.setStackLimiterLimit(stackSizeLimit);
                lastStackLimiterLimit = stackSizeLimit;
            }
            case KEY_CONFIGURE_INPUT_FILTER -> blockExtender.getInputFilter().ifPresent(it -> {
                MenuProvider config = it.getConfiguration(player, blockExtender, 0, 0);
                if (config != null) {
                    Balm.getNetworking().openGui(player, config);
                }
            });
            case KEY_CONFIGURE_OUTPUT_FILTER -> blockExtender.getOutputFilter().ifPresent(it -> {
                MenuProvider config = it.getConfiguration(player, blockExtender, 1, 0);
                if (config != null) {
                    Balm.getNetworking().openGui(player, config);
                }
            });
        }
    }

    @Override
    public void receivedMessageClient(IMenuMessage message) {
        if (message.getKey().equals(KEY_SIDE_MAPPING)) {
            CompoundTag compound = message.getNBTValue();
            blockExtender.setSideMapping(RelativeSide.fromIndex(compound.getByte(KEY_SIDE_INDEX)), Direction.from3DDataValue(compound.getByte(KEY_SIDE_MAPPING)));
        } else if (message.getKey().equals(KEY_STACK_LIMITER)) {
            blockExtender.setStackLimiterLimit(message.getIntValue());
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if(index < 3) {
                if (!moveItemStackTo(slotStack, 3, 30, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 30) {
                if (!moveItemStackTo(slotStack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                } else if (!moveItemStackTo(slotStack, 27, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(slotStack, 0, 30, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return !blockExtender.isRemoved() && player.distanceToSqr(blockExtender.getBlockPos().getX() + 0.5, blockExtender.getBlockPos().getY() + 0.5, blockExtender.getBlockPos().getZ() + 0.5) <= 64;
    }

    public Slot getUpgradeSlot(int i) {
        return slots.get(i);
    }

}
