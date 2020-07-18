package net.blay09.mods.refinedrelocation;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.Capabilities;
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
import net.blay09.mods.refinedrelocation.network.*;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class InternalMethodsImpl implements InternalMethods {

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
        if (world == null) {
            return;
        }

        BlockPos pos = member.getTileEntity().getPos();
        for (Direction facing : Direction.values()) {
            BlockPos facingPos = pos.offset(facing);
            if (world.isBlockLoaded(facingPos)) {
                TileEntity tileEntity = world.getChunk(facingPos).getTileEntity(facingPos);
                if (tileEntity != null) {
                    ISortingGridMember otherMember = RefinedRelocationUtils.orNull(tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, facing.getOpposite()));
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
        for (ISortingGridMember otherMember : sortingGrid.getMembers()) {
            otherMember.setSortingGrid(null);
        }
        // Then, re-add them to the grid
        for (ISortingGridMember otherMember : sortingGrid.getMembers()) {
            RefinedRelocationAPI.addToSortingGrid(otherMember);
        }
    }

    @Override
    public void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack) {
        List<ISortingInventory> passingList = Lists.newArrayList();
        IItemHandler itemHandler = RefinedRelocationUtils.orNull(sortingInventory.getItemHandler());
        if (itemHandler == null) {
            return;
        }

        ItemStack restStack = itemHandler.extractItem(fromSlotIndex, Items.AIR.getItemStackLimit(ItemStack.EMPTY), true);
        if (restStack.isEmpty()) {
            return;
        }

        ISortingGrid sortingGrid = sortingInventory.getSortingGrid();
        if (sortingGrid != null) {
            for (ISortingGridMember member : sortingGrid.getMembers()) {
                if (member instanceof ISortingInventory) {
                    ISortingInventory memberInventory = (ISortingInventory) member;
                    LazyOptional<? extends ISimpleFilter> filter = memberInventory.getFilter();
                    final ItemStack testStack = restStack;
                    boolean passes = filter.filter(it -> it.passes(memberInventory.getTileEntity(), testStack, itemStack)).isPresent();
                    if (passes) {
                        passingList.add(memberInventory);
                    }
                }
            }
        }

        // No point trying if there's no matching inventories.
        if (!passingList.isEmpty()) {
            ISortingInventory targetInventory = getBestTargetInventory(passingList, null);
            if (targetInventory != sortingInventory) {
                // Only move the item if it's not already in the correct inventory
                while (!restStack.isEmpty() && !passingList.isEmpty() && targetInventory != null) {
                    // Insert stack into passing inventories
                    int insertCount = restStack.getCount();
                    IItemHandler targetItemHandler = RefinedRelocationUtils.orNull(targetInventory.getItemHandler());
                    if (targetItemHandler != null) {
                        restStack = ItemHandlerHelper.insertItemStacked(targetItemHandler, restStack, false);
                    }

                    int actuallyInserted = insertCount - restStack.getCount();
                    if (actuallyInserted > 0) {
                        ItemStack movedStack = itemHandler.extractItem(fromSlotIndex, actuallyInserted, false);
                        if (movedStack.getCount() != actuallyInserted) {
                            // This would mean we just duped an item. This should only be possible if someone implements IItemHandler incorrectly, so crash and make it more likely to be reported.
                            throw new RuntimeException("Refined Relocation ran into a major problem with the connected inventory " + sortingInventory + ". Please report this at https://github.com/blay09/RefinedRelocation2/issues.");
                        }
                    }

                    if (!restStack.isEmpty()) {
                        targetInventory = getBestTargetInventory(passingList, targetInventory);
                    }
                }
            }
        }
    }

    @Nullable
    private static ISortingInventory getBestTargetInventory(List<ISortingInventory> passingList, @Nullable ISortingInventory lastInventory) {
        ISortingInventory targetInventory = null;
        int highestPriority = Integer.MIN_VALUE;
        Iterator<ISortingInventory> it = passingList.iterator();
        while (it.hasNext()) {
            ISortingInventory sortingInventory = it.next();
            if (sortingInventory == lastInventory) {
                it.remove();
            } else if (sortingInventory.getPriority() > highestPriority) {
                targetInventory = sortingInventory;
                highestPriority = sortingInventory.getPriority();
            }
        }
        return targetInventory;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Button createOpenFilterButton(ContainerScreen<?> guiContainer, TileEntity tileEntity, int rootFilterIndex) {
        return new GuiOpenFilterButton(guiContainer.getGuiLeft() + guiContainer.getXSize() - 18, guiContainer.getGuiTop() + 4, tileEntity, rootFilterIndex);
    }

    @Override
    public void sendContainerMessageToServer(String key, String value) {
        NetworkHandler.channel.sendToServer(new MessageContainerString(key, value));
    }

    @Override
    public void sendContainerMessageToServer(String key, CompoundNBT value) {
        NetworkHandler.channel.sendToServer(new MessageContainerNBT(key, value));
    }


    @Override
    public void sendContainerMessageToServer(String key, int value) {
        NetworkHandler.channel.sendToServer(new MessageContainerInt(key, value));
    }

    @Override
    public void sendContainerMessageToServer(String key, int value, int secondaryValue) {
        NetworkHandler.channel.sendToServer(new MessageContainerIndexedInt(key, value, secondaryValue));
    }

    @Override
    public void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners) {
        syncContainerValue(new MessageContainerString(key, value), listeners);
    }

    @Override
    public void syncContainerValue(String key, int value, Iterable<IContainerListener> listeners) {
        syncContainerValue(new MessageContainerInt(key, value), listeners);
    }

    @Override
    public void syncContainerValue(String key, byte[] value, Iterable<IContainerListener> listeners) {
        syncContainerValue(new MessageContainerByteArray(key, value), listeners);
    }

    @Override
    public void syncContainerValue(String key, CompoundNBT value, Iterable<IContainerListener> listeners) {
        syncContainerValue(new MessageContainerNBT(key, value), listeners);
    }

    private void syncContainerValue(MessageContainer message, Iterable<IContainerListener> listeners) {
        for (IContainerListener listener : listeners) {
            if (listener instanceof ServerPlayerEntity) {
                NetworkHandler.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener), message);
            }
        }
    }

    @Override
    public void openRootFilterGui(PlayerEntity player, TileEntity tileEntity, int rootFilterIndex) {
        if (player.world.isRemote) {
            NetworkHandler.channel.sendToServer(new MessageRequestFilterGUI(tileEntity.getPos(), rootFilterIndex));
        } else {
            RefinedRelocationUtils.getRootFilter(tileEntity, rootFilterIndex).ifPresent(rootFilter -> {
                INamedContainerProvider filterConfig = rootFilter.getConfiguration(player, tileEntity, rootFilterIndex);
                NetworkHooks.openGui((ServerPlayerEntity) player, filterConfig, tileEntity.getPos());
            });
        }
    }

    @Override
    public void updateFilterPreview(PlayerEntity entityPlayer, TileEntity tileEntity, ISimpleFilter filter) {
        if (!entityPlayer.world.isRemote) {
            byte[] slotStates = new byte[MessageFilterPreview.INVENTORY_SLOT_COUNT];
            for (int i = 0; i < slotStates.length; i++) {
                ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    slotStates[i] = (byte) (filter.passes(tileEntity, itemStack, itemStack) ? MessageFilterPreview.STATE_SUCCESS : MessageFilterPreview.STATE_FAILURE);
                }
            }

            NetworkHandler.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entityPlayer), new MessageFilterPreview(slotStates));
        }
    }

    @Override
    public void returnToParentContainer() {
        NetworkHandler.channel.sendToServer(new MessageReturnGUI());
    }

}
