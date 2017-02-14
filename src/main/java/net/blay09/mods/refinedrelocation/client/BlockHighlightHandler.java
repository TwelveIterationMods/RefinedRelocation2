package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHighlightHandler {

	@SubscribeEvent
	public void onBlockHighlight(DrawBlockHighlightEvent event) {
		if(!event.getPlayer().isSneaking()) {
			return;
		}
		if(event.getTarget() != null && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
			TileEntity tileEntity = event.getPlayer().world.getTileEntity(event.getTarget().getBlockPos());
			if(tileEntity != null) {
				ISortingGridMember sortingMember = tileEntity.getCapability(Capabilities.SORTING_GRID_MEMBER, event.getTarget().sideHit);
				if(sortingMember != null) {
					ISortingGrid sortingGrid = sortingMember.getSortingGrid();
					if(sortingGrid != null) {
						GlStateManager.enableBlend();
						GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
						GlStateManager.glLineWidth(2f);
						GlStateManager.disableTexture2D();
						GlStateManager.depthMask(false);
						double expansion = 0.002;
						double offsetX = event.getPlayer().lastTickPosX + (event.getPlayer().posX - event.getPlayer().lastTickPosX) * (double) event.getPartialTicks();
						double offsetY = event.getPlayer().lastTickPosY + (event.getPlayer().posY - event.getPlayer().lastTickPosY) * (double) event.getPartialTicks();
						double offsetZ = event.getPlayer().lastTickPosZ + (event.getPlayer().posZ - event.getPlayer().lastTickPosZ) * (double) event.getPartialTicks();
						for (ISortingGridMember member : sortingGrid.getMembers()) {
							IBlockState blockState = member.getTileContainer().getWorld().getBlockState(member.getTileContainer().getPos());
							@SuppressWarnings("deprecation") AxisAlignedBB aabb = blockState.getBlock().getSelectedBoundingBox(blockState, member.getTileContainer().getWorld(), member.getTileContainer().getPos()).expand(expansion, expansion, expansion).offset(-offsetX, -offsetY, -offsetZ);
							RenderGlobal.drawSelectionBoundingBox(aabb, 1f, 1f, 0f, 0.75f);
						}
						GlStateManager.depthMask(true);
						GlStateManager.enableTexture2D();
						GlStateManager.disableBlend();
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
