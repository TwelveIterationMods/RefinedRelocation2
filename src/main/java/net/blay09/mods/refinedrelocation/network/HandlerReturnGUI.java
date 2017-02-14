package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.InternalMethodsImpl;
import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerReturnGUI implements IMessageHandler<MessageReturnGUI, IMessage> {
	@Override
	@Nullable
	public IMessage onMessage(MessageReturnGUI message, final MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				Container container = ctx.getServerHandler().player.openContainer;
				if(container instanceof ContainerRootFilter) {
					TileEntity tileEntity = ((ContainerRootFilter) container).getTileOrMultipart().getTileEntity();
					ITileGuiHandler tileGuiHandler = InternalMethodsImpl.getGuiHandler(tileEntity.getClass());
					if (tileGuiHandler != null) {
						tileGuiHandler.openGui(ctx.getServerHandler().player, new TileWrapper(tileEntity));
					}
				}
			}
		});
		return null;
	}
}
