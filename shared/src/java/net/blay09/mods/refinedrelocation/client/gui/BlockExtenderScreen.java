package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.refinedrelocation.item.ModItems;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.element.BlockExtenderFilterButton;
import net.blay09.mods.refinedrelocation.client.gui.element.StackLimiterButton;
import net.blay09.mods.refinedrelocation.client.gui.element.SideToggleButton;
import net.blay09.mods.refinedrelocation.client.gui.element.TooltipButton;
import net.blay09.mods.refinedrelocation.menu.BlockExtenderMenu;
import net.blay09.mods.refinedrelocation.block.entity.BlockExtenderBlockEntity;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BlockExtenderScreen extends ModContainerScreen<BlockExtenderMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/block_extender.png");
    private static final int UPDATE_INTERVAL = 20;

    private final BlockExtenderBlockEntity tileEntity;

    private StackLimiterButton btnStackLimiter;
    private BlockExtenderFilterButton btnInputFilter;
    private BlockExtenderFilterButton btnOutputFilter;
    private Button slotLockButton;

    private int stackLimiterIdx;
    private int slotLockIdx;
    private int inputFilterIdx;
    private int outputFilterIdx;

    private int ticksSinceUpdate;
    private int lastSentStackLimit;

    public BlockExtenderScreen(BlockExtenderMenu container, Inventory inventory, Component displayName) {
        super(container, inventory, displayName);
        this.tileEntity = container.getBlockEntity();
        imageHeight = 176;
        inventoryLabelY = imageHeight - 96 + 2;
    }

    @Override
    public void init() {
        super.init();

        addRenderableWidget(new SideToggleButton(leftPos + 9, topPos + 40, tileEntity, RelativeSide.LEFT));
        addRenderableWidget(new SideToggleButton(leftPos + 26, topPos + 40, tileEntity, RelativeSide.FRONT));
        addRenderableWidget(new SideToggleButton(leftPos + 43, topPos + 40, tileEntity, RelativeSide.RIGHT));
        addRenderableWidget(new SideToggleButton(leftPos + 60, topPos + 40, tileEntity, RelativeSide.BACK));
        addRenderableWidget(new SideToggleButton(leftPos + 26, topPos + 23, tileEntity, RelativeSide.TOP));
        addRenderableWidget(new SideToggleButton(leftPos + 26, topPos + 57, tileEntity, RelativeSide.BOTTOM));

        btnStackLimiter = new StackLimiterButton(0, 0, 24, 16, tileEntity);
        btnStackLimiter.visible = false;
        addRenderableWidget(btnStackLimiter);

        btnInputFilter = new BlockExtenderFilterButton(0, 0, 0, 64, 16, false);
        btnInputFilter.visible = false;
        addRenderableWidget(btnInputFilter);

        btnOutputFilter = new BlockExtenderFilterButton(0, 0, 0, 64, 16, true);
        btnOutputFilter.visible = false;
        addRenderableWidget(btnOutputFilter);

        slotLockButton = new TooltipButton(0, 0, 64, 16, new TranslatableComponent("gui.refinedrelocation:block_extender.slot_lock"), it -> {
        }) {
            @Override
            public void addTooltip(List<Component> list) {
                list.add(new TranslatableComponent("tooltip.refinedrelocation:slot_lock"));
            }
        };
        slotLockButton.visible = false;
        slotLockButton.active = false;
        addRenderableWidget(slotLockButton);
    }

    @Override
    public void containerTick() {
        super.containerTick();

        stackLimiterIdx = -1;
        slotLockIdx = -1;
        inputFilterIdx = -1;
        outputFilterIdx = -1;
        for (int i = 0; i < 3; i++) {
            ItemStack itemStack = menu.getUpgradeSlot(i).getItem();
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
        btnStackLimiter.x = leftPos + 152 - btnStackLimiter.getWidth() - 3;
        btnStackLimiter.y = topPos + 22 + stackLimiterIdx * 18;
        slotLockButton.visible = slotLockIdx != -1;
        slotLockButton.x = leftPos + 152 - slotLockButton.getWidth() - 3;
        slotLockButton.y = topPos + 22 + slotLockIdx * 18;
        btnInputFilter.visible = inputFilterIdx != -1;
        btnInputFilter.x = leftPos + 152 - btnInputFilter.getWidth() - 3;
        btnInputFilter.y = topPos + 22 + inputFilterIdx * 18;
        btnOutputFilter.visible = outputFilterIdx != -1;
        btnOutputFilter.x = leftPos + 152 - btnOutputFilter.getWidth() - 3;
        btnOutputFilter.y = topPos + 22 + outputFilterIdx * 18;

        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            if (lastSentStackLimit != tileEntity.getStackLimiterLimit()) {
                RefinedRelocationAPI.sendContainerMessageToServer(BlockExtenderMenu.KEY_STACK_LIMITER, tileEntity.getStackLimiterLimit());
                lastSentStackLimit = tileEntity.getStackLimiterLimit();
            }
            ticksSinceUpdate = 0;
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Render upgrade conflicts
        Player player = Minecraft.getInstance().player;
        ItemStack mouseStack = player != null ? menu.getCarried() : ItemStack.EMPTY;
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
                Slot slot = menu.getUpgradeSlot(conflictSlot);
                fill(poseStack, leftPos + slot.x, topPos + slot.y, leftPos + slot.x + 16, topPos + slot.y + 16, 0x55FF0000);
            }
        }
    }

    @Override
    public boolean onGuiAboutToClose() {
        RefinedRelocationAPI.sendContainerMessageToServer(BlockExtenderMenu.KEY_STACK_LIMITER, tileEntity.getStackLimiterLimit());
        return true;
    }
}
