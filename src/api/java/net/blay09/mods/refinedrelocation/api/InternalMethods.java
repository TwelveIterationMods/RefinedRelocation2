package net.blay09.mods.refinedrelocation.api;

import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface InternalMethods {

    void registerFilter(Class<? extends IFilter> filterClass);

    void addToSortingGrid(ISortingGridMember member);

    void removeFromSortingGrid(ISortingGridMember member);

    void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack);

    @OnlyIn(Dist.CLIENT)
    Button createOpenFilterButton(ContainerScreen<?> guiContainer, TileEntity tileEntity, int buttonId);

    void sendContainerMessageToServer(String key, String value);

    void sendContainerMessageToServer(String key, CompoundNBT value);

    void sendContainerMessageToServer(String key, int value);

    void sendContainerMessageToServer(String key, int value, int secondaryValue);

    void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners);

    void syncContainerValue(String key, int value, Iterable<IContainerListener> listeners);

    void syncContainerValue(String key, byte[] value, Iterable<IContainerListener> listeners);

    void syncContainerValue(String key, CompoundNBT value, Iterable<IContainerListener> listeners);

    void openRootFilterGui(PlayerEntity player, TileEntity tileEntity);

    void updateFilterPreview(PlayerEntity player, TileEntity tileEntity, ISimpleFilter filter);

    void returnToParentContainer();
}
