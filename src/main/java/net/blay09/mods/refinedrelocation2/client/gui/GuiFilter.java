package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.capability.IFilterProvider;
import net.blay09.mods.refinedrelocation2.balyware.TextureAtlasRegion;
import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonFilterLink;
import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonPriority;
import net.blay09.mods.refinedrelocation2.client.gui.element.IButtonHandler;
import net.blay09.mods.refinedrelocation2.container.ContainerFilter;
import net.blay09.mods.refinedrelocation2.container.handler.IPriorityHandler;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.blay09.mods.refinedrelocation2.network.container.MessageContainerInteger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiFilter extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(RefinedRelocation2.MOD_ID, "textures/gui/filter.png");

    private final ContainerFilter container;
    private final IInventory playerInventory;
    private TextureAtlasRegion textureOR;
    private TextureAtlasRegion textureAND;

    public GuiFilter(EntityPlayer entityPlayer, IFilterProvider provider) {
        super(new ContainerFilter(entityPlayer, provider));
        container = (ContainerFilter) inventorySlots;
        playerInventory = entityPlayer.inventory;

        ySize = 209;

        textureOR = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:filter_or");
        textureAND = GuiRefinedRelocation.textureMap.getSprite("refinedrelocation2:filter_and");
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.add(new GuiButtonPriority(0, guiLeft + 30, guiTop + 29, new IPriorityHandler() {
            @Override
            public int getPriority() {
                return container.getPriority();
            }

            @Override
            public void setPriority(int priority) {
                container.setPriority(priority);
                NetworkHandler.instance.sendToServer(new MessageContainerInteger("priority", priority));
            }
        }));
        buttonList.add(new GuiButtonFilterLink(1, guiLeft + 7, guiTop + 50, 0));
        buttonList.add(new GuiButtonFilterLink(2, guiLeft + 7, guiTop + 70, 1));
        buttonList.add(new GuiButtonFilterLink(3, guiLeft + 7, guiTop + 90, 2));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Setup Filter", 8, 8, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);

        GlStateManager.color(1f, 1f, 1f, 1f);
        textureOR.draw(37, 67, zLevel);
        textureOR.draw(37, 87, zLevel);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (GuiButton button : buttonList) {
            if(button instanceof IButtonHandler) {
                if(button.mousePressed(mc, mouseX, mouseY)) {
                    ((IButtonHandler) button).handleClick(mouseX, mouseY, mouseButton);
                    button.playPressSound(mc.getSoundHandler());
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
