package net.blay09.mods.refinedrelocation.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = RefinedRelocation.MOD_ID)
public class BlockHighlightHandler {

    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (!player.isSneaking()) {
            return;
        }

        if (event.getTarget() != null && event.getTarget().getType() == RayTraceResult.Type.BLOCK) {
            World world = player.world;
            BlockRayTraceResult rayTraceResult = (BlockRayTraceResult) event.getTarget();
            TileEntity tileEntity = world.getTileEntity(rayTraceResult.getPos());
            if (tileEntity != null) {
                LazyOptional<ISortingGridMember> sortingMemberCap = tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, rayTraceResult.getFace());
                sortingMemberCap.ifPresent(sortingMember -> {
                    ISortingGrid sortingGrid = sortingMember.getSortingGrid();
                    if (sortingGrid != null) {
                        GlStateManager.enableBlend();
                        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                        GlStateManager.lineWidth(2f);
                        GlStateManager.disableTexture();
                        GlStateManager.depthMask(false);
                        double expansion = 0.002;
                        double offsetX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks();
                        double offsetY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks();
                        double offsetZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks();
                        for (ISortingGridMember member : sortingGrid.getMembers()) {
                            TileEntity memberTile = member.getTileEntity();
                            BlockState blockState = world.getBlockState(memberTile.getPos());
                            VoxelShape shape = blockState.getRaytraceShape(world, memberTile.getPos());
                            AxisAlignedBB boundingBox = shape.getBoundingBox()
                                    .expand(expansion, expansion, expansion)
                                    .offset(-offsetX, -offsetY, -offsetZ);
                            WorldRenderer.drawSelectionBoundingBox(boundingBox, 1f, 1f, 0f, 0.75f);
                        }
                        GlStateManager.depthMask(true);
                        GlStateManager.enableTexture();
                        GlStateManager.disableBlend();
                        event.setCanceled(true);
                    }
                });
            }
        }
    }

}
