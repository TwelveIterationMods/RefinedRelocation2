package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBlockExtender extends ContainerMod {

	public static final String KEY_TOGGLE_SIDE = "ToggleSide";
	public static final String KEY_SIDE_INDEX = "SideIndex";
	public static final String KEY_SIDE_MAPPING = "SideMapping";
	public static final String KEY_STACK_LIMITER = "StackLimiter";
	public static final String KEY_CONFIGURE_FILTER = "ConfigureFilter";

	private final EntityPlayer player;
	private final TileBlockExtender tileEntity;

	private final EnumFacing[] lastSideMapping = new EnumFacing[5];
	private int lastStackLimiterLimit;

	public ContainerBlockExtender(EntityPlayer player, TileBlockExtender tileEntity) {
		this.player = player;
		this.tileEntity = tileEntity;

		for(int i = 0; i < 3; i++) {
			addSlotToContainer(new SlotItemHandler(tileEntity.getItemHandlerUpgrades(), i, 152, 22 + i * 18));
		}

		addPlayerInventory(player, 94);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for(int i = 0; i < lastSideMapping.length; i++) {
			EnumFacing nowSideMapping = tileEntity.getSideMapping(RelativeSide.fromIndex(i));
			if(lastSideMapping[i] != nowSideMapping) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte(KEY_SIDE_INDEX, (byte) i);
				compound.setByte(KEY_SIDE_MAPPING, nowSideMapping != null ? (byte) nowSideMapping.getIndex() : -1);
				RefinedRelocationAPI.syncContainerValue(KEY_SIDE_MAPPING, compound, listeners);
				lastSideMapping[i] = nowSideMapping;
			}
		}

		int nowStackLimiterLimit = tileEntity.getStackLimiterLimit();
		if(lastStackLimiterLimit != nowStackLimiterLimit) {
			RefinedRelocationAPI.syncContainerValue(KEY_STACK_LIMITER, tileEntity.getStackLimiterLimit(), listeners);
			lastStackLimiterLimit = nowStackLimiterLimit;
		}
	}

	@Override
	public void receivedMessageServer(IContainerMessage message) {
		if(message.getKey().equals(KEY_TOGGLE_SIDE)) {
			RelativeSide side = RelativeSide.fromIndex(message.getIntValue());
			if(side != RelativeSide.FRONT) {
				int facingIdx = message.getSecondaryIntValue();
				EnumFacing facing = facingIdx == -1 ? null : EnumFacing.getFront(facingIdx);
				tileEntity.setSideMapping(side, facing);
				lastSideMapping[side.ordinal()] = facing;
			}
		} else if(message.getKey().equals(KEY_STACK_LIMITER)) {
			int stackSizeLimit = MathHelper.clamp(message.getIntValue(), 1, 64);
			tileEntity.setStackLimiterLimit(stackSizeLimit);
			lastStackLimiterLimit = stackSizeLimit;
		} else if(message.getKey().equals(KEY_CONFIGURE_FILTER)) {
			if(message.getIntValue() == 0 && !tileEntity.hasInputFilter()) {
				return;
			} else if(message.getIntValue() == 1 && !tileEntity.hasOutputFilter()) {
				return;
			}
			RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_BLOCK_EXTENDER_ROOT_FILTER, tileEntity.getPos(), message.getIntValue()));
		}
	}

	@Override
	public void receivedMessageClient(IContainerMessage message) {
		if(message.getKey().equals(KEY_SIDE_MAPPING)) {
			NBTTagCompound compound = message.getNBTValue();
			tileEntity.setSideMapping(RelativeSide.fromIndex(compound.getByte(KEY_SIDE_INDEX)), EnumFacing.getFront(compound.getByte(KEY_SIDE_MAPPING)));
		} else if(message.getKey().equals(KEY_STACK_LIMITER)) {
			tileEntity.setStackLimiterLimit(message.getIntValue());
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();

			if (index < 27) {
				if (!mergeItemStack(slotStack, 27, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 0, 27, false)) {
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
	public boolean canInteractWith(EntityPlayer player) {
		return !tileEntity.isInvalid() && player.getDistanceSq(tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5) <= 64;
	}

	public Slot getUpgradeSlot(int i) {
		return inventorySlots.get(i);
	}

}
