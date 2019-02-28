package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiLabel;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiButtonPriority;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiDeleteFilterButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiFilterSlot;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiWhitelistButton;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiRootFilter extends GuiContainerMod<ContainerRootFilter> implements IFilterPreviewGui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter.png");
    private static final ResourceLocation TEXTURE_NO_PRIORITY = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter_no_priority.png");
    private static final int UPDATE_INTERVAL = 20;

    private final IDrawable textureSeparator;

    private int ticksSinceUpdate;
    private int lastSentPriority;

    public GuiRootFilter(EntityPlayer player, TileEntity tileEntity) {
        this(new ContainerRootFilter(player, tileEntity));
    }

    public GuiRootFilter(ContainerRootFilter container) {
        super(container);

        if (container.hasSortingInventory()) {
            ySize = 210;
        }

        final GuiFilterSlot[] filterSlots = new GuiFilterSlot[3];
        final GuiDeleteFilterButton[] deleteButtons = new GuiDeleteFilterButton[3];
        final GuiWhitelistButton[] whitelistButtons = new GuiWhitelistButton[3];

        int x = 10;
        for (int i = 0; i < filterSlots.length; i++) {
            filterSlots[i] = new GuiFilterSlot(0, x, 30, this, container.getRootFilter(), i);
            addButton(filterSlots[i]);

            deleteButtons[i] = new GuiDeleteFilterButton(0, x + 19, 27, filterSlots[i]);
            addButton(deleteButtons[i]);

            whitelistButtons[i] = new GuiWhitelistButton(0, x + 1, 55, this, filterSlots[i]);
            addButton(whitelistButtons[i]);
            x += 40;
        }

        GuiImageButton btnReturn = new GuiImageButton(0, guiLeft + xSize - 20, guiTop + 4, 16, 16, GuiTextures.CHEST_BUTTON) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (onGuiAboutToClose()) {
                    NetworkHandler.channel.sendToServer(new MessageReturnGUI());
                }
            }
        };
        addButton(btnReturn);

        if (container.hasSortingInventory()) {
            children.add(new GuiLabel(10, 65, I18n.format("gui.refinedrelocation:root_filter.priority_label"), 0x404040));
            addButton(new GuiButtonPriority(0, 10, 80, 100, 20, container.getSortingInventory()));
        }

        textureSeparator = GuiTextures.FILTER_SEPARATOR;
    }

    @Override
    public void tick() {
        super.tick();

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            ISortingInventory sortingInventory = container.getSortingInventory();
            if (lastSentPriority != sortingInventory.getPriority()) {
                RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_PRIORITY, sortingInventory.getPriority());
                lastSentPriority = sortingInventory.getPriority();
            }
            ticksSinceUpdate = 0;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        if (container.hasSortingInventory()) {
            mc.getTextureManager().bindTexture(TEXTURE);
        } else {
            mc.getTextureManager().bindTexture(TEXTURE_NO_PRIORITY);
        }

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        final int x = guiLeft + 10;
        final int y = guiTop + 37;
        GlStateManager.enableBlend();
        textureSeparator.draw(x + 30, y, zLevel);
        textureSeparator.draw(x + 70, y, zLevel);
        GlStateManager.disableBlend();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        TileEntity tileEntity = container.getTileEntity();
        ITextComponent displayName = null;
        if (tileEntity instanceof INameable) {
            displayName = ((INameable) tileEntity).getDisplayName();
        }

        fontRenderer.drawString(displayName != null ? I18n.format("container.refinedrelocation:root_filter_with_name", displayName.getFormattedText()) : I18n.format("container.refinedrelocation:root_filter"), 8, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public boolean onGuiAboutToClose() {
        RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_PRIORITY, container.getSortingInventory().getPriority());
        return true;
    }

}
