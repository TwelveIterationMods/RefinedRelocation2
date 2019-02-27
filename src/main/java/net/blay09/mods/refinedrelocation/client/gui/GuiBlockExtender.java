package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.ModItems;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiButtonBlockExtenderFilter;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiButtonStackLimiter;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiSideButton;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiTooltipButton;
import net.blay09.mods.refinedrelocation.container.ContainerBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiBlockExtender extends GuiContainerMod<ContainerBlockExtender> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/block_extender.png");
    private static final int UPDATE_INTERVAL = 20;

    private final TileBlockExtender tileEntity;
    private final GuiButtonStackLimiter btnStackLimiter;
    private final GuiButtonBlockExtenderFilter btnInputFilter;
    private final GuiButtonBlockExtenderFilter btnOutputFilter;
    private final GuiButton btnSlotLock;

    private int stackLimiterIdx;
    private int slotLockIdx;
    private int inputFilterIdx;
    private int outputFilterIdx;

    private int ticksSinceUpdate;
    private int lastSentStackLimit;

    public GuiBlockExtender(EntityPlayer player, TileBlockExtender tileEntity, EnumFacing clickedFace) {
        super(new ContainerBlockExtender(player, tileEntity));
        this.tileEntity = tileEntity;
        ySize = 176;

        RelativeSide centerFace = RelativeSide.fromFacing(tileEntity.getFacing(), clickedFace);
        RelativeSide topFace = RelativeSide.TOP;
        RelativeSide leftFace;
        if (clickedFace.getAxis() == EnumFacing.Axis.Y) {
            centerFace = RelativeSide.FRONT;
            topFace = RelativeSide.TOP;
            leftFace = RelativeSide.LEFT;
        } else if (tileEntity.getFacing().getAxis() == EnumFacing.Axis.Y) {
            leftFace = clickedFace.getAxis() == EnumFacing.Axis.Z ? centerFace.rotateX() : centerFace.rotateX().getOpposite();
            if (tileEntity.getFacing().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                leftFace = leftFace.getOpposite();
                topFace = RelativeSide.FRONT;
            } else {
                topFace = RelativeSide.BACK;
            }
        } else {
            leftFace = centerFace.rotateY();
        }
        addButton(new GuiSideButton(0, 9, 40, tileEntity, leftFace));
        addButton(new GuiSideButton(0, 26, 40, tileEntity, centerFace));
        addButton(new GuiSideButton(0, 43, 40, tileEntity, leftFace.getOpposite()));
        addButton(new GuiSideButton(0, 60, 40, tileEntity, centerFace.getOpposite()));
        addButton(new GuiSideButton(0, 26, 23, tileEntity, topFace));
        addButton(new GuiSideButton(0, 26, 57, tileEntity, topFace.getOpposite()));

        btnStackLimiter = new GuiButtonStackLimiter(0, 0, 0, 24, 16, tileEntity);
        btnStackLimiter.visible = false;
        addButton(btnStackLimiter);

        btnInputFilter = new GuiButtonBlockExtenderFilter(0, 0, 0, 64, 16, false);
        btnInputFilter.visible = false;
        addButton(btnInputFilter);

        btnOutputFilter = new GuiButtonBlockExtenderFilter(0, 0, 0, 64, 16, true);
        btnOutputFilter.visible = false;
        addButton(btnOutputFilter);

        btnSlotLock = new GuiTooltipButton(0, 0, 0, 64, 16, I18n.format("gui.refinedrelocation:block_extender.slot_lock")) {
            @Override
            public void addTooltip(List<String> list) {
                list.add(I18n.format("tooltip.refinedrelocation:slot_lock"));
            }
        };
        btnSlotLock.visible = false;
        btnSlotLock.enabled = false;
        addButton(btnSlotLock);
    }

    @Override
    public void tick() {
        super.tick();

        stackLimiterIdx = -1;
        slotLockIdx = -1;
        inputFilterIdx = -1;
        outputFilterIdx = -1;
        for (int i = 0; i < 3; i++) {
            ItemStack itemStack = container.getUpgradeSlot(i).getStack();
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() == ModItems.stackLimiter) {
                    stackLimiterIdx = i;
                } else if (itemStack.getItem() == ModItems.slotLock) {
                    slotLockIdx = i;
                } else if (itemStack.getItem() == ModItems.inputFilter) {
                    inputFilterIdx = i;
                } else if (itemStack.getItem() == ModItems.outputFilter) {
                    outputFilterIdx = i;
                }
            }

        }
        btnStackLimiter.visible = stackLimiterIdx != -1;
        btnStackLimiter.x = 152 - btnStackLimiter.getWidth() - 3;
        btnStackLimiter.y = 22 + stackLimiterIdx * 18;
        btnSlotLock.visible = slotLockIdx != -1;
        btnSlotLock.x = 152 - btnSlotLock.getWidth() - 3;
        btnSlotLock.y = 22 + slotLockIdx * 18;
        btnInputFilter.visible = inputFilterIdx != -1;
        btnInputFilter.x = 152 - btnInputFilter.getWidth() - 3;
        btnInputFilter.y = 22 + inputFilterIdx * 18;
        btnOutputFilter.visible = outputFilterIdx != -1;
        btnOutputFilter.x = 152 - btnOutputFilter.getWidth() - 3;
        btnOutputFilter.y = 22 + outputFilterIdx * 18;

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            if (lastSentStackLimit != tileEntity.getStackLimiterLimit()) {
                RefinedRelocationAPI.sendContainerMessageToServer(ContainerBlockExtender.KEY_STACK_LIMITER, tileEntity.getStackLimiterLimit());
                lastSentStackLimit = tileEntity.getStackLimiterLimit();
            }
            ticksSinceUpdate = 0;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Render upgrade conflicts
        ItemStack mouseStack = mc.player.inventory.getItemStack();
        if (!mouseStack.isEmpty()) {
            int conflictSlot = -1;
            if (mouseStack.getItem() == ModItems.stackLimiter && stackLimiterIdx != -1) {
                conflictSlot = stackLimiterIdx;
            } else if (mouseStack.getItem() == ModItems.slotLock && slotLockIdx != -1) {
                conflictSlot = slotLockIdx;
            } else if (mouseStack.getItem() == ModItems.inputFilter && inputFilterIdx != -1) {
                conflictSlot = inputFilterIdx;
            } else if (mouseStack.getItem() == ModItems.outputFilter && outputFilterIdx != -1) {
                conflictSlot = outputFilterIdx;
            }
            if (conflictSlot != -1) {
                Slot slot = container.getUpgradeSlot(conflictSlot);
                Gui.drawRect(guiLeft + slot.xPos, guiTop + slot.yPos, guiLeft + slot.xPos + 16, guiTop + slot.yPos + 16, 0x55FF0000);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(tileEntity.getDisplayName().getFormattedText(), 8, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public boolean onGuiAboutToClose() {
        RefinedRelocationAPI.sendContainerMessageToServer(ContainerBlockExtender.KEY_STACK_LIMITER, tileEntity.getStackLimiterLimit());
        return true;
    }
}
