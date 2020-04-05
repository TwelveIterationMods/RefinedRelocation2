package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.RefinedRelocationUtils;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkHooks;

public class AddFilterContainer extends BaseContainer implements IRootFilterContainer {

    public static final String KEY_ADD_FILTER = "AddFilter";
    public static final String KEY_ROOT_FILTER = "RootFilter";

    private final TileEntity tileEntity;
    private final PlayerEntity player;
    private final IRootFilter rootFilter;
    private final int rootFilterIndex;

    public AddFilterContainer(int windowId, PlayerInventory playerInventory, TileEntity tileEntity, int rootFilterIndex) {
        super(ModContainers.addFilter, windowId);
        this.tileEntity = tileEntity;

        player = playerInventory.player;

        addPlayerInventory(playerInventory, 8, 128);

        this.rootFilterIndex = rootFilterIndex;
        this.rootFilter = RefinedRelocationUtils.getRootFilter(tileEntity, rootFilterIndex).orElseGet(RootFilter::new);
    }

    @Override
    public void receivedMessageServer(IContainerMessage message) {
        switch (message.getKey()) {
            case KEY_ADD_FILTER: {
                String typeId = message.getStringValue();
                IFilter filter = FilterRegistry.createFilter(typeId);
                if (filter == null) {
                    // Client tried to create a filter that doesn't exist. Bad client!
                    return;
                }

                if (rootFilter.getFilterCount() >= 3) {
                    // Client tried to create more than three filters. Bad client!
                    return;
                }

                rootFilter.addFilter(filter);
                tileEntity.markDirty();
                syncFilterList();
                RefinedRelocationAPI.updateFilterPreview(player, tileEntity, rootFilter);
                INamedContainerProvider filterConfig = filter.getConfiguration(player, tileEntity, rootFilterIndex);
                if (filterConfig != null) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, filterConfig, it -> {
                        it.writeBlockPos(tileEntity.getPos());
                        it.writeByte(rootFilterIndex);
                        it.writeByte(rootFilter.getFilterCount() - 1);
                    });
                }
                break;
            }
        }
    }

    private void syncFilterList() {
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.put(KEY_ROOT_FILTER, rootFilter.serializeNBT());
        RefinedRelocationAPI.syncContainerValue(KEY_ROOT_FILTER, tagCompound, listeners);
    }

    @Override
    public void receivedMessageClient(IContainerMessage message) {
        switch (message.getKey()) {
            case KEY_ROOT_FILTER:
                rootFilter.deserializeNBT(message.getNBTValue().getCompound(KEY_ROOT_FILTER));
                break;
        }
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public IRootFilter getRootFilter() {
        return rootFilter;
    }
}
