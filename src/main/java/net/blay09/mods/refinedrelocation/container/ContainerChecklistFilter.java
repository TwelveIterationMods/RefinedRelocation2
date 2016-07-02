package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChecklistFilter extends ContainerMod {

	private final EntityPlayer player;
	private final TileOrMultipart tileEntity;
	private final IChecklistFilter filter;

	public ContainerChecklistFilter(EntityPlayer player, TileOrMultipart tileEntity, IChecklistFilter filter) {
		this.player = player;
		this.tileEntity = tileEntity;
		this.filter = filter;

		addPlayerInventory(player, 128);
	}



}
