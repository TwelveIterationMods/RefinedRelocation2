package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
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
        if (!event.getPlayer().isSneaking()) {
            return;
        }
        if (event.getTarget() != null && event.getTarget().type == RayTraceResult.Type.BLOCK) {
            World world = event.getPlayer().world;
            TileEntity tileEntity = world.getTileEntity(event.getTarget().getBlockPos());
            if (tileEntity != null) {
                LazyOptional<ISortingGridMember> sortingMemberCap = tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, event.getTarget().sideHit);
                sortingMemberCap.ifPresent(sortingMember -> {
                    ISortingGrid sortingGrid = sortingMember.getSortingGrid();
                    if (sortingGrid != null) {
                        GlStateManager.enableBlend();
                        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                        GlStateManager.lineWidth(2f);
                        GlStateManager.disableTexture2D();
                        GlStateManager.depthMask(false);
                        double expansion = 0.002;
                        double offsetX = event.getPlayer().lastTickPosX + (event.getPlayer().posX - event.getPlayer().lastTickPosX) * (double) event.getPartialTicks();
                        double offsetY = event.getPlayer().lastTickPosY + (event.getPlayer().posY - event.getPlayer().lastTickPosY) * (double) event.getPartialTicks();
                        double offsetZ = event.getPlayer().lastTickPosZ + (event.getPlayer().posZ - event.getPlayer().lastTickPosZ) * (double) event.getPartialTicks();
                        for (ISortingGridMember member : sortingGrid.getMembers()) {
                            TileEntity memberTile = member.getTileEntity();
                            IBlockState blockState = world.getBlockState(memberTile.getPos());
                            VoxelShape shape = blockState.getRaytraceShape(world, memberTile.getPos());
                            AxisAlignedBB boundingBox = shape.getBoundingBox()
                                    .expand(expansion, expansion, expansion)
                                    .offset(-offsetX, -offsetY, -offsetZ);
                            WorldRenderer.drawSelectionBoundingBox(boundingBox, 1f, 1f, 0f, 0.75f);
                        }
                        GlStateManager.depthMask(true);
                        GlStateManager.enableTexture2D();
                        GlStateManager.disableBlend();
                        event.setCanceled(true);
                    }
                });
            }
        }
    }

}
