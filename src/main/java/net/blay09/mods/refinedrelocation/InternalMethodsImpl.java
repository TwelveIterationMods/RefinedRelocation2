package net.blay09.mods.refinedrelocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.InternalMethods;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.blay09.mods.refinedrelocation.grid.SortingGrid;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageContainer;
import net.blay09.mods.refinedrelocation.network.MessageFilterPreview;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InternalMethodsImpl implements InternalMethods {

	private static final Map<Class, ITileGuiHandler> tileGuiHandlerMap = Maps.newHashMap();

	@Override
	public void registerFilter(Class<? extends IFilter> filterClass) {
		FilterRegistry.registerFilter(filterClass);
	}

	@Override
	public void addToSortingGrid(ISortingGridMember member) {
		ISortingGrid sortingGrid = member.getSortingGrid();
		if (sortingGrid != null) {
			return;
		}
		World world = member.getTileEntity().getWorld();
		BlockPos pos = member.getTileEntity().getPos();
		for (EnumFacing facing : EnumFacing.VALUES) {
			BlockPos facingPos = pos.offset(facing);
			if(world.isBlockLoaded(facingPos)) {
				TileEntity tileEntity = world.getChunkFromBlockCoords(facingPos).getTileEntity(facingPos, Chunk.EnumCreateEntityType.CHECK);
				if (tileEntity != null) {
					ISortingGridMember otherMember = tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, facing.getOpposite());
					if (otherMember != null && otherMember.getSortingGrid() != null) {
						if (sortingGrid != null) {
							((SortingGrid) sortingGrid).mergeWith(otherMember.getSortingGrid());
						} else {
							sortingGrid = otherMember.getSortingGrid();
						}
					}
				}
			}
		}
		if (sortingGrid == null) {
			sortingGrid = new SortingGrid();
		}
		sortingGrid.addMember(member);
	}

	@Override
	public void removeFromSortingGrid(ISortingGridMember member) {
		ISortingGrid sortingGrid = member.getSortingGrid();
		if (sortingGrid == null) {
			return;
		}
		sortingGrid.removeMember(member);
		// First, reset all sorting grid members
		for(ISortingGridMember otherMember : sortingGrid.getMembers()) {
			otherMember.setSortingGrid(null);
		}
		// Then, re-add them to the grid
		for(ISortingGridMember otherMember : sortingGrid.getMembers()) {
			RefinedRelocationAPI.addToSortingGrid(otherMember);
		}
	}

	@Override
	public void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack) {
		List<ISortingInventory> passingList = Lists.newArrayList();
		ItemStack restStack = sortingInventory.getItemHandler().extractItem(fromSlotIndex, 64, false);
		if(restStack.isEmpty()) {
			return;
		}
		ISortingGrid sortingGrid = sortingInventory.getSortingGrid();
		if(sortingGrid != null) {
			for (ISortingGridMember member : sortingGrid.getMembers()) {
				if (member instanceof ISortingInventory) {
					ISortingInventory memberInventory = (ISortingInventory) member;
					boolean passes = memberInventory.getFilter().passes(memberInventory.getTileEntity(), restStack);
					if (passes) {
						passingList.add(memberInventory);
					}
				}
			}
		}
		// No point trying if there's no matching inventories.
		if(!passingList.isEmpty()) {
			ISortingInventory targetInventory = getBestTargetInventory(passingList, null);
			if (targetInventory != sortingInventory) {
				// Only move the item if it's not already in the correct inventory
				while (!restStack.isEmpty() && !passingList.isEmpty() && targetInventory != null) {
					// Insert stack into passing inventories
					restStack = ItemHandlerHelper.insertItemStacked(targetInventory.getItemHandler(), restStack, false);
					if (!restStack.isEmpty()) {
						targetInventory = getBestTargetInventory(passingList, targetInventory);
					}
				}
			}
		}
		if(!restStack.isEmpty()) {
			restStack = sortingInventory.getItemHandler().insertItem(fromSlotIndex, restStack, false);
			if(!restStack.isEmpty()) {
				// This should be impossible, the item came from here to begin with so there should be at least enough space to re-insert it.
				// If because of some mess-up somewhere it does happen after all though, crash the player to notify them about deleted items and make it more likely to be reported.
				throw new RuntimeException("Refined Relocation just ate one of your items and that's really weird because this should never happen. Now go and report this so I can have a further look into this.");
			}
		}
	}

	@Nullable
	private static ISortingInventory getBestTargetInventory(List<ISortingInventory> passingList, @Nullable ISortingInventory lastInventory) {
		ISortingInventory targetInventory = null;
		int highestPriority = Integer.MIN_VALUE;
		Iterator<ISortingInventory> it = passingList.iterator();
		while(it.hasNext()) {
			ISortingInventory sortingInventory = it.next();
			if(sortingInventory == lastInventory) {
				it.remove();
			} else if(sortingInventory.getPriority() > highestPriority) {
				targetInventory = sortingInventory;
				highestPriority = sortingInventory.getPriority();
			}
		}
		return targetInventory;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiButton createOpenFilterButton(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		return new GuiOpenFilterButton(buttonId, guiContainer.guiLeft + guiContainer.xSize - 18, guiContainer.guiTop + 4, tileEntity);
	}

	@Override
	public void sendContainerMessageToServer(String key, String value) {
		NetworkHandler.wrapper.sendToServer(new MessageContainer(key, value));
	}

	@Override
	public void sendContainerMessageToServer(String key, NBTTagCompound value) {
		NetworkHandler.wrapper.sendToServer(new MessageContainer(key, value));
	}


	@Override
	public void sendContainerMessageToServer(String key, int value) {
		NetworkHandler.wrapper.sendToServer(new MessageContainer(key, value));
	}

	@Override
	public void sendContainerMessageToServer(String key, int value, int secondaryValue) {
		NetworkHandler.wrapper.sendToServer(new MessageContainer(key, value, secondaryValue));
	}

	@Override
	public void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners) {
		syncContainerValue(new MessageContainer(key, value), listeners);
	}

	@Override
	public void syncContainerValue(String key, int value, Iterable<IContainerListener> listeners) {
		syncContainerValue(new MessageContainer(key, value), listeners);
	}

	@Override
	public void syncContainerValue(String key, byte[] value, Iterable<IContainerListener> listeners) {
		syncContainerValue(new MessageContainer(key, value), listeners);
	}

	@Override
	public void syncContainerValue(String key, NBTTagCompound value, Iterable<IContainerListener> listeners) {
		syncContainerValue(new MessageContainer(key, value), listeners);
	}

	private void syncContainerValue(MessageContainer message, Iterable<IContainerListener> listeners) {
		for(IContainerListener listener : listeners) {
			if (listener instanceof EntityPlayerMP) {
				NetworkHandler.wrapper.sendTo(message, (EntityPlayerMP) listener);
			}
		}
	}

	@Override
	public void registerGuiHandler(Class tileClass, ITileGuiHandler handler) {
		tileGuiHandlerMap.put(tileClass, handler);
	}

	@Nullable
	public static ITileGuiHandler getGuiHandler(Class tileClass) {
		return tileGuiHandlerMap.get(tileClass);
	}

	@Override
	public void openRootFilterGui(EntityPlayer player, TileEntity tileEntity) {
		NetworkHandler.wrapper.sendToServer(new MessageOpenGui(GuiHandler.GUI_ROOT_FILTER, tileEntity));
	}

	@Override
	public void updateFilterPreview(EntityPlayer entityPlayer, TileEntity tileEntity, ISimpleFilter filter) {
		if(!entityPlayer.world.isRemote) {
			byte[] slotStates = new byte[MessageFilterPreview.INVENTORY_SLOT_COUNT];
			for (int i = 0; i < slotStates.length; i++) {
				ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);
				if (!itemStack.isEmpty()) {
					slotStates[i] = (byte) (filter.passes(tileEntity, itemStack) ? MessageFilterPreview.STATE_SUCCESS : MessageFilterPreview.STATE_FAILURE);
				}
			}
			NetworkHandler.wrapper.sendTo(new MessageFilterPreview(slotStates), (EntityPlayerMP) entityPlayer);
		}
	}

	@Override
	public void returnToParentContainer() {
		NetworkHandler.wrapper.sendToServer(new MessageReturnGUI());
	}
}
