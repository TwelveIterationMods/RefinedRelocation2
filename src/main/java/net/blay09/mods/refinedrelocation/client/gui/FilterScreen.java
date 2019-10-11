package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.container.FilterContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public abstract class FilterScreen<T extends FilterContainer> extends ModContainerScreen<T> {
    public FilterScreen(T container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
    }

    @Override
    protected void init() {
        super.init();

        if (hasOpenFilterButton()) {
            addButton(new GuiOpenFilterButton(guiLeft + xSize - 17, guiTop + 5, container.getTileEntity()));
        }
    }

    protected boolean hasOpenFilterButton() {
        return true;
    }

}
