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

public class RefinedRelocationAPI {

    private static InternalMethods internalMethods;

    public static void __internal__setupAPI(InternalMethods internalMethods) {
        RefinedRelocationAPI.internalMethods = internalMethods;
    }

    public static void addToSortingGrid(ISortingGridMember member) {
        internalMethods.addToSortingGrid(member);
    }

    public static void removeFromSortingGrid(ISortingGridMember member) {
        internalMethods.removeFromSortingGrid(member);
    }

    public static void registerFilter(Class<? extends IFilter> filterClass) {
        internalMethods.registerFilter(filterClass);
    }

    @OnlyIn(Dist.CLIENT)
    public static Button createOpenFilterButton(ContainerScreen<?> guiContainer, TileEntity tileEntity) {
        return internalMethods.createOpenFilterButton(guiContainer, tileEntity);
    }

    public static void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack) {
        internalMethods.insertIntoSortingGrid(sortingInventory, fromSlotIndex, itemStack);
    }

    public static void sendContainerMessageToServer(String key, String value) {
        internalMethods.sendContainerMessageToServer(key, value);
    }

    public static void sendContainerMessageToServer(String key, CompoundNBT value) {
        internalMethods.sendContainerMessageToServer(key, value);
    }

    public static void sendContainerMessageToServer(String key, int value) {
        internalMethods.sendContainerMessageToServer(key, value);
    }

    public static void sendContainerMessageToServer(String key, int value, int value2) {
        internalMethods.sendContainerMessageToServer(key, value, value2);
    }

    public static void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners) {
        internalMethods.syncContainerValue(key, value, listeners);
    }

    public static void syncContainerValue(String key, int value, Iterable<IContainerListener> listeners) {
        internalMethods.syncContainerValue(key, value, listeners);
    }

    public static void syncContainerValue(String key, byte[] value, Iterable<IContainerListener> listeners) {
        internalMethods.syncContainerValue(key, value, listeners);
    }

    public static void syncContainerValue(String key, CompoundNBT value, Iterable<IContainerListener> listeners) {
        internalMethods.syncContainerValue(key, value, listeners);
    }

    public static void updateFilterPreview(PlayerEntity player, TileEntity tileEntity, ISimpleFilter filter) {
        internalMethods.updateFilterPreview(player, tileEntity, filter);
    }

    public static void openRootFilterGui(PlayerEntity player, TileEntity tileEntity) {
        internalMethods.openRootFilterGui(player, tileEntity);
    }

    public static void returnToParentContainer() {
        internalMethods.returnToParentContainer();
    }
}
