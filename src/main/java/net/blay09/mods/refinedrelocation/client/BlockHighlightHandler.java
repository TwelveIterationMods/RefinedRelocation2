package net.blay09.mods.refinedrelocation.client;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = RefinedRelocation.MOD_ID)
public class BlockHighlightHandler {

    @SubscribeEvent
    public static void onBlockHighlight(DrawHighlightEvent.HighlightBlock event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null || !player.isSneaking()) {
            return;
        }

        World world = player.world;
        BlockRayTraceResult rayTraceResult = event.getTarget();
        TileEntity tileEntity = world.getTileEntity(rayTraceResult.getPos());
        if (tileEntity != null) {
            LazyOptional<ISortingGridMember> sortingMemberCap = tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, rayTraceResult.getFace());
            //noinspection ConstantConditions - null check is necessary because some mods still didn't quite understand what Nonnull means
            if (sortingMemberCap != null) {
                sortingMemberCap.ifPresent(sortingMember -> {
                    ISortingGrid sortingGrid = sortingMember.getSortingGrid();
                    if (sortingGrid != null) {
                        double camX = event.getInfo().getProjectedView().x;
                        double camY = event.getInfo().getProjectedView().y;
                        double camZ = event.getInfo().getProjectedView().z;
                        for (ISortingGridMember member : sortingGrid.getMembers()) {
                            TileEntity memberTile = member.getTileEntity();
                            BlockPos pos = memberTile.getPos();
                            BlockState blockState = world.getBlockState(pos);
                            VoxelShape shape = blockState.getShape(world, pos, ISelectionContext.forEntity(player));
                            IVertexBuilder vertexBuilder = event.getBuffers().getBuffer(RenderType.getLines());
                            WorldRenderer.drawShape(event.getMatrix(), vertexBuilder, shape, pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, 1f, 1f, 0f, 0.75f);
                        }
                        event.setCanceled(true);
                    }
                });
            }
        }
    }

}
