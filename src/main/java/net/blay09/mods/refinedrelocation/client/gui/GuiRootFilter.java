package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.InternalMethodsImpl;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiButtonPriority;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiDeleteFilterButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiFilterSlot;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiWhitelistButton;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.network.MessageReturnGUI;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiRootFilter extends GuiContainerMod<ContainerRootFilter> implements IFilterPreviewGui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter.png");
    private static final ResourceLocation TEXTURE_NO_PRIORITY = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter_no_priority.png");
    private static final int UPDATE_INTERVAL = 20;

    private final TextureAtlasRegion textureSeparator;

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
            filterSlots[i] = new GuiFilterSlot(this, container.getRootFilter(), i);
            filterSlots[i].setPosition(x, 30);
            rootNode.addChild(filterSlots[i]);

            deleteButtons[i] = new GuiDeleteFilterButton(x + 19, 27, filterSlots[i]);
            rootNode.addChild(deleteButtons[i]);

            whitelistButtons[i] = new GuiWhitelistButton(x + 1, 55, this, filterSlots[i]);
            rootNode.addChild(whitelistButtons[i]);
            x += 40;
        }

        ITileGuiHandler tileGuiHandler = InternalMethodsImpl.getGuiHandler(container.getTileEntity().getClass());
        if (tileGuiHandler != null) {
            GuiImageButton btnReturn = new GuiImageButton(guiLeft + xSize - 20, guiTop + 4, "chest_button") {
                @Override
                public void actionPerformed(int mouseButton) {
                    if (onGuiAboutToClose()) {
                        NetworkHandler.channel.sendToServer(new MessageReturnGUI());
                    }
                }
            };
            rootNode.addChild(btnReturn);
        }

        if (container.hasSortingInventory()) {
            rootNode.addChild(new GuiLabel(10, 65, I18n.format("gui.refinedrelocation:root_filter.priority_label"), 0x404040));
            rootNode.addChild(new GuiButtonPriority(10, 80, 100, 20, container.getSortingInventory()));
        }

        textureSeparator = ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:filter_separator");
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
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

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        ITextComponent displayName = container.getTileEntity().getDisplayName();
        fontRenderer.drawString(displayName != null ? I18n.format("container.refinedrelocation:root_filter_with_name", displayName.getFormattedText()) : I18n.format("container.refinedrelocation:root_filter"), 8, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public boolean onGuiAboutToClose() {
        RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_PRIORITY, container.getSortingInventory().getPriority());
        return true;
    }

}
