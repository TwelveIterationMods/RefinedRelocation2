package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.client.gui.base.element.LabelWidget;
import net.blay09.mods.refinedrelocation.client.gui.element.*;
import net.blay09.mods.refinedrelocation.container.RootFilterContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class RootFilterScreen extends FilterScreen<RootFilterContainer> implements IFilterPreviewGui, IHasContainer<RootFilterContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter.png");
    private static final ResourceLocation TEXTURE_NO_PRIORITY = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter_no_priority.png");
    private static final int UPDATE_INTERVAL = 20;

    private final IDrawable textureSeparator = GuiTextures.FILTER_SEPARATOR;

    private int ticksSinceUpdate;
    private int lastSentPriority;

    public RootFilterScreen(RootFilterContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);

        if (container.hasSortingInventory()) {
            ySize = 210;
        }
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    @Override
    public void init() {
        super.init();

        final GuiFilterSlot[] filterSlots = new GuiFilterSlot[3];
        final GuiDeleteFilterButton[] deleteButtons = new GuiDeleteFilterButton[3];
        final GuiWhitelistButton[] whitelistButtons = new GuiWhitelistButton[3];

        int x = guiLeft + 10;
        for (int i = 0; i < filterSlots.length; i++) {
            filterSlots[i] = new GuiFilterSlot(x, guiTop + 30, this, container.getRootFilter(), i);
            addButton(filterSlots[i]);

            deleteButtons[i] = new GuiDeleteFilterButton(x + 19, guiTop + 27, filterSlots[i]);
            addButton(deleteButtons[i]);

            whitelistButtons[i] = new GuiWhitelistButton(x + 1, guiTop + 55, this, filterSlots[i]);
            addButton(whitelistButtons[i]);
            x += 40;
        }

        if (container.canReturnFromFilter()) {
            addButton(new GuiReturnFromFilterButton(guiLeft + xSize - 20, guiTop + 4));
        }

        if (container.hasSortingInventory()) {
            addButton(new LabelWidget(font, guiLeft + 10, guiTop + 65, new TranslationTextComponent("gui.refinedrelocation:root_filter.priority_label"), 0x404040));
            addButton(new GuiButtonPriority(guiLeft + 10, guiTop + 80, 100, 20, container.getSortingInventory()));
        }
    }

    @Override
    public void tick() {
        super.tick();

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            ISortingInventory sortingInventory = container.getSortingInventory();
            if (lastSentPriority != sortingInventory.getPriority()) {
                RefinedRelocationAPI.sendContainerMessageToServer(RootFilterContainer.KEY_PRIORITY, sortingInventory.getPriority());
                lastSentPriority = sortingInventory.getPriority();
            }
            ticksSinceUpdate = 0;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        if (container.hasSortingInventory()) {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        } else {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_NO_PRIORITY);
        }

        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        final int x = guiLeft + 10;
        final int y = guiTop + 37;
        RenderSystem.enableBlend();
        textureSeparator.bind();
        textureSeparator.draw(matrixStack,x + 30, y, getBlitOffset());
        textureSeparator.draw(matrixStack,x + 70, y, getBlitOffset());
        RenderSystem.disableBlend();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        TileEntity tileEntity = container.getTileEntity();
        ITextComponent displayName = null;
        if (tileEntity instanceof INameable) {
            displayName = ((INameable) tileEntity).getDisplayName();
        }

        final TranslationTextComponent title = displayName != null ? new TranslationTextComponent("container.refinedrelocation:root_filter_with_name", displayName) : new TranslationTextComponent("container.refinedrelocation:root_filter");
        font.func_238422_b_(matrixStack, title.func_241878_f(), 8, 6, 4210752);
        font.drawString(matrixStack, I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public boolean onGuiAboutToClose() {
        RefinedRelocationAPI.sendContainerMessageToServer(RootFilterContainer.KEY_PRIORITY, container.getSortingInventory().getPriority());
        return true;
    }

    @Override
    public Container getFilterContainer() {
        return container;
    }

    @Override
    public int getFilterGuiLeft() {
        return guiLeft;
    }

    @Override
    public int getFilterGuiTop() {
        return guiTop;
    }

    @Override
    protected boolean hasOpenFilterButton() {
        return false;
    }
}
