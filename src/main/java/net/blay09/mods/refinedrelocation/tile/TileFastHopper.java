package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.block.BlockFastHopper;
import net.blay09.mods.refinedrelocation.block.BlockMod;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileFastHopper extends TileMod implements ITickable, INameable {

	protected String customName;
	private int cooldown;
	private ItemStackHandler itemHandler = createItemHandler();

	protected ItemStackHandler createItemHandler() {
		return new ItemStackHandler(5) {
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
			}
		};
	}

	@Override
	public void update() {
		if (world != null && !world.isRemote) {
			cooldown--;
			if (cooldown <= 0) {
				EnumFacing facing = world.getBlockState(getPos()).getValue(BlockFastHopper.FACING);
				EnumFacing opposite = facing.getOpposite();
				TileEntity facingTile = world.getTileEntity(pos.offset(facing));
				IItemHandler targetItemHandler = facingTile != null ? facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, opposite) : null;
				boolean hasSpace = false;
				if (targetItemHandler != null) {
					for (int i = 0; i < itemHandler.getSlots(); i++) {
						ItemStack itemStack = itemHandler.getStackInSlot(i);
						if (!itemStack.isEmpty()) {
							pushItem(i, targetItemHandler);
						}
						if (!hasSpace && (itemStack.isEmpty() || itemStack.getCount() < itemStack.getMaxStackSize())) {
							hasSpace = true;
						}
					}
				} else {
					for(int i = 0; i < itemHandler.getSlots(); i++) {
						if(itemHandler.getStackInSlot(i).isEmpty()) {
							hasSpace = true;
							break;
						}
					}
				}

				if (hasSpace) {
					BlockPos upPos = pos.offset(EnumFacing.UP);
					TileEntity upTile = world.getTileEntity(upPos);
					IItemHandler itemHandler = upTile != null ? upTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) : null;
					if (itemHandler != null) {
						pullItem(itemHandler);
					} else {
						float range = 0.75f;
						world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + 1.5f, pos.getZ() + range), EntitySelectors.IS_ALIVE).forEach(this::pullItem);
					}
				}

				cooldown = 20;
			}
		}
	}

	public void pushItem(int sourceSlot, IItemHandler targetItemHandler) {
		ItemStack sourceStack = itemHandler.extractItem(sourceSlot, 64, true);
		ItemStack restStack = ItemHandlerHelper.insertItem(targetItemHandler, sourceStack, false);
		itemHandler.extractItem(sourceSlot, restStack.isEmpty() ? sourceStack.getCount() : sourceStack.getCount() - restStack.getCount(), false);
	}

	private void pullItem(IItemHandler sourceItemHandler) {
		for (int i = 0; i < sourceItemHandler.getSlots(); i++) {
			ItemStack sourceStack = sourceItemHandler.extractItem(i, 64, true);
			if(!sourceStack.isEmpty()) {
				ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
				sourceItemHandler.extractItem(i, restStack.isEmpty() ? 64 : sourceStack.getCount() - restStack.getCount(), false);
				break;
			}
		}
	}

	public boolean pullItem(EntityItem entityItem) {
		ItemStack sourceStack = entityItem.getItem();
		ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
		if(!restStack.isEmpty()) {
			entityItem.setItem(restStack);
		} else {
			entityItem.setDead();
		}
		return restStack.isEmpty() || restStack.getCount() < sourceStack.getCount();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
		if(hasCustomName()) {
			tagCompound.setString("CustomName", customName);
		}
		return tagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
		customName = tagCompound.getString("CustomName");
	}

	@Override
	public String getUnlocalizedName() {
		return "container.refinedrelocation:fast_hopper";
	}

	@Override
	public boolean hasCustomName() {
		return customName != null && !customName.isEmpty();
	}

	@Override
	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public String getCustomName() {
		return customName;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		}
		return super.getCapability(capability, facing);
	}

	public ItemStackHandler getItemHandler() {
		return itemHandler;
	}
}
