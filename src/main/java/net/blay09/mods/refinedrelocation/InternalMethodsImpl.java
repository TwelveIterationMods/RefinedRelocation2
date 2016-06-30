package net.blay09.mods.refinedrelocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.InternalMethods;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.blay09.mods.refinedrelocation.grid.SortingGrid;
import net.blay09.mods.refinedrelocation.network.MessageContainer;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InternalMethodsImpl implements InternalMethods {

	private static final Map<Class<? extends TileEntity>, ITileGuiHandler> tileGuiHandlerMap = Maps.newHashMap();

	@Override
	public void registerFilter(String id, Class<? extends IFilter> filterClass) {
		FilterRegistry.registerFilter(id, filterClass);
	}

	@Override
	public void addToSortingGrid(ISortingGridMember member) {
		ISortingGrid sortingGrid = member.getSortingGrid();
		if (sortingGrid != null) {
			return;
		}
		World world = member.getTileWrapper().getWorld();
		BlockPos pos = member.getTileWrapper().getPos();
		for (EnumFacing facing : EnumFacing.VALUES) {
			TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
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
		if(restStack == null) {
			return;
		}
		ISortingGrid sortingGrid = sortingInventory.getSortingGrid();
		for(ISortingGridMember member : sortingGrid.getMembers()) {
			if(member instanceof ISortingInventory) {
				ISortingInventory memberInventory = (ISortingInventory) member;
				boolean passes = memberInventory.getFilter().passes(memberInventory.getTileWrapper(), itemStack);
				if(passes) {
					passingList.add(memberInventory);
				}
			}
		}
		if(passingList.isEmpty()) {
			// No passing inventories found, just leave it there.
			sortingInventory.getItemHandler().insertItem(fromSlotIndex, restStack, false);
			return;
		}
		ISortingInventory targetInventory = getBestTargetInventory(passingList, null);
		if(targetInventory == sortingInventory) {
			// Already in the correct inventory, nothing to do here
			sortingInventory.getItemHandler().insertItem(fromSlotIndex, restStack, false);
			return;
		}
		while(restStack != null && !passingList.isEmpty() && targetInventory != null) {
			// Insert stack into passing inventories
			restStack = ItemHandlerHelper.insertItemStacked(targetInventory.getItemHandler(), restStack, false);
			if(restStack != null) {
				targetInventory = getBestTargetInventory(passingList, targetInventory);
			}
		}
		if(restStack != null) {
			restStack = sortingInventory.getItemHandler().insertItem(fromSlotIndex, restStack, false);
			if(restStack != null) {
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
	public GuiButton createOpenFilterButton(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		return new GuiOpenFilterButton(buttonId, guiContainer.guiLeft + guiContainer.xSize - 20, guiContainer.guiTop + 4, new TileWrapper(tileEntity));
	}

	@Override
	public void sendContainerMessageToServer(String key, String value) {
		NetworkHandler.wrapper.sendToServer(new MessageContainer(key, value));
	}

	@Override
	public void sendContainerMessageToServer(String key, int value) {
		NetworkHandler.wrapper.sendToServer(new MessageContainer(key, value));
	}

	@Override
	public void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners) {
		for(IContainerListener listener : listeners) {
			if (listener instanceof EntityPlayerMP) {
				NetworkHandler.wrapper.sendTo(new MessageContainer(key, value), (EntityPlayerMP) listener);
			}
		}
	}

	@Override
	public void syncContainerValue(String key, NBTTagCompound value, Iterable<IContainerListener> listeners) {
		for(IContainerListener listener : listeners) {
			if (listener instanceof EntityPlayerMP) {
				NetworkHandler.wrapper.sendTo(new MessageContainer(key, value), (EntityPlayerMP) listener);
			}
		}
	}

	@Override
	public void registerGuiHandler(Class<? extends TileEntity> tileClass, ITileGuiHandler handler) {
		tileGuiHandlerMap.put(tileClass, handler);
	}

	@Nullable
	public static ITileGuiHandler getGuiHandler(Class<? extends TileEntity> tileClass) {
		return tileGuiHandlerMap.get(tileClass);
	}

}
