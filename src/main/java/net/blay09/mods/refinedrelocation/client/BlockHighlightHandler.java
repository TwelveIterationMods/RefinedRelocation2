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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
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
    public static void onBlockHighlight(DrawBlockHighlightEvent event) {
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
                        double offsetX = event.getInfo().getProjectedView().x;
                        double offsetY = event.getInfo().getProjectedView().y;
                        double offsetZ = event.getInfo().getProjectedView().z;
                        for (ISortingGridMember member : sortingGrid.getMembers()) {
                            TileEntity memberTile = member.getTileEntity();
                            BlockPos pos = memberTile.getPos();
                            BlockState blockState = world.getBlockState(pos);
                            VoxelShape shape = blockState.getShape(world, pos, ISelectionContext.forEntity(player));
                            WorldRenderer.drawShape(shape, pos.getX() - offsetX, pos.getY() - offsetY, pos.getZ() - offsetZ, 1f, 1f, 0f, 0.75f);
                        }
                        GlStateManager.depthMask(true);
                        GlStateManager.enableTexture();
                        GlStateManager.disableBlend();
                        event.setCanceled(true);
                    }
                });
                //noinspection ConstantConditions - null check is necessary because some mods still didn't quite understand what Nonnull means
                if (sortingMemberCap != null) {
                    sortingMemberCap.ifPresent(sortingMember -> {
                        ISortingGrid sortingGrid = sortingMember.getSortingGrid();
                        if (sortingGrid != null) {
                            GlStateManager.enableBlend();
                            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                            GlStateManager.lineWidth(2f);
                            GlStateManager.disableTexture();
                            GlStateManager.depthMask(false);
                            double offsetX = event.getInfo().getProjectedView().x;
                            double offsetY = event.getInfo().getProjectedView().y;
                            double offsetZ = event.getInfo().getProjectedView().z;
                            for (ISortingGridMember member : sortingGrid.getMembers()) {
                                TileEntity memberTile = member.getTileEntity();
                                BlockPos pos = memberTile.getPos();
                                BlockState blockState = world.getBlockState(pos);
                                VoxelShape shape = blockState.getShape(world, pos, ISelectionContext.forEntity(player));
                                WorldRenderer.drawShape(shape, pos.getX() - offsetX, pos.getY() - offsetY, pos.getZ() - offsetZ, 1f, 1f, 0f, 0.75f);
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

}

