package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.api.client.InternalClientMethods;
import net.blay09.mods.refinedrelocation.client.gui.element.OpenFilterButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InternalClientMethodsImpl implements InternalClientMethods {
    @Override
    public Button createOpenFilterButton(AbstractContainerScreen<?> guiContainer, BlockEntity blockEntity, int rootFilterIndex) {
        return new OpenFilterButton(guiContainer.getGuiLeft() + guiContainer.getXSize() - 18, guiContainer.getGuiTop() + 4, blockEntity, rootFilterIndex);
    }
}
