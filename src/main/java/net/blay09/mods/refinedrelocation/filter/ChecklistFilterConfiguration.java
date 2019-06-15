package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ChecklistFilterConfiguration implements INamedContainerProvider {

    private final ResourceLocation id;
    private final TileEntity tileEntity;
    private final IChecklistFilter filter;

    public ChecklistFilterConfiguration(ResourceLocation id, TileEntity tileEntity, IChecklistFilter filter) {
        this.id = id;
        this.tileEntity = tileEntity;
        this.filter = filter;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerChecklistFilter(i, playerEntity, tileEntity, filter);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.refinedrelocation:checklistFilter");
    }
}
