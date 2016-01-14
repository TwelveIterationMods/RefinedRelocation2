package net.blay09.mods.refinedrelocation2.client;

import net.blay09.mods.refinedrelocation2.CommonProxy;
import net.blay09.mods.refinedrelocation2.ModBlocks;
import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation2.client.render.ItemModelToolbox;
import net.blay09.mods.refinedrelocation2.item.IScrollableItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy {

    private static final int SCROLL_COOLDOWN = 100;

    private long lastScrollTime;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        OBJLoader.instance.addDomain(RefinedRelocation2.MOD_ID);

        ModBlocks.registerSpecialModels();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ModBlocks.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
        ModItems.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
    }

    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        if(!event.player.isSneaking()) {
            return;
        }
        if(event.target != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            TileEntity tileEntity = event.player.worldObj.getTileEntity(event.target.getBlockPos());
            if(tileEntity != null) {
                ISortingGridMember sortingMember = tileEntity.getCapability(RefinedRelocation2.SORTING_GRID_MEMBER, event.target.sideHit);
                if(sortingMember != null) {
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    GL11.glLineWidth(2f);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    double expansion = 0.002;
                    double offsetX = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * (double) event.partialTicks;
                    double offsetY = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * (double) event.partialTicks;
                    double offsetZ = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * (double) event.partialTicks;
                    ISortingGrid sortingGrid = sortingMember.getSortingGrid();
                    for(ISortingGridMember member : sortingGrid.getMembers()) {
                        IBlockState blockState = member.getWorld().getBlockState(member.getPos());
                        blockState.getBlock().setBlockBoundsBasedOnState(member.getWorld(), member.getPos());
                        AxisAlignedBB aabb = blockState.getBlock().getSelectedBoundingBox(member.getWorld(), member.getPos()).expand(expansion, expansion, expansion).offset(-offsetX, -offsetY, -offsetZ);
                        RenderGlobal.drawOutlinedBoundingBox(aabb, 255, 255, 0, 192);
                    }
                    GlStateManager.depthMask(true);
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void addScheduledTask(Runnable runnable) {
        Minecraft.getMinecraft().addScheduledTask(runnable);
    }

    @Override
    public void showItemHighlight() {
        Minecraft.getMinecraft().ingameGUI.remainingHighlightTicks = 40;
    }

    @SubscribeEvent
    public void onMouseWheel(MouseEvent event) {
        int delta = event.dwheel;
        if(delta != 0) {
            EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            ItemStack itemStack = entityPlayer.getHeldItem();
            if(itemStack != null && itemStack.getItem() instanceof IScrollableItem && entityPlayer.isSneaking()) {
                long now = System.currentTimeMillis();
                if(now - lastScrollTime >= SCROLL_COOLDOWN) {
                    ((IScrollableItem) itemStack.getItem()).onScrolled(entityPlayer, itemStack, delta);
                    lastScrollTime = now;
                    showItemHighlight();
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onKeyDown(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            ItemStack itemStack = entityPlayer.getHeldItem();
            if(itemStack != null && itemStack.getItem() instanceof IScrollableItem && entityPlayer.isSneaking()) {
                Minecraft mc = Minecraft.getMinecraft();
                for (int i = 0; i <= 5; i++) {
                    int keyCode = mc.gameSettings.keyBindsHotbar[i].getKeyCode();
                    if (keyCode > 0 && keyCode == Keyboard.getEventKey()) {
                        ((IScrollableItem) itemStack.getItem()).setScrollIndex(entityPlayer, itemStack, i == 5 ? 0 : i + 1);
                        mc.gameSettings.keyBindsHotbar[i].isPressed();
                        showItemHighlight();
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        IBakedModel model = event.modelRegistry.getObject(ItemModelToolbox.resource);
        if(model instanceof IFlexibleBakedModel) {
            ItemModelToolbox customModel = new ItemModelToolbox((IFlexibleBakedModel) model);
            event.modelRegistry.putObject(ItemModelToolbox.resource, customModel);
        }
    }
}
