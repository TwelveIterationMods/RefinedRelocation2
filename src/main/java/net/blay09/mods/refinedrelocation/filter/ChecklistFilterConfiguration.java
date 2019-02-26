package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
import net.blay09.mods.refinedrelocation.util.IInteractionObjectWithoutName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ChecklistFilterConfiguration implements IInteractionObjectWithoutName {

    private final ResourceLocation id;
    private final TileEntity tileEntity;
    private final IChecklistFilter filter;

    public ChecklistFilterConfiguration(ResourceLocation id, TileEntity tileEntity, IChecklistFilter filter) {
        this.id = id;
        this.tileEntity = tileEntity;
        this.filter = filter;
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerChecklistFilter(entityPlayer, tileEntity, filter);
    }

    @Override
    public String getGuiID() {
        return id.toString();
    }
}
