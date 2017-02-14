package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetworkHandler {

	public static final SimpleNetworkWrapper wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(RefinedRelocation.MOD_ID);

	public static void init() {
		wrapper.registerMessage(HandlerOpenGui.class, MessageOpenGui.class, 1, Side.CLIENT);
		wrapper.registerMessage(HandlerOpenGui.class, MessageOpenGui.class, 2, Side.SERVER);
		wrapper.registerMessage(HandlerContainer.class, MessageContainer.class, 3, Side.SERVER);
		wrapper.registerMessage(HandlerContainer.class, MessageContainer.class, 4, Side.CLIENT);
		wrapper.registerMessage(HandlerReturnGUI.class, MessageReturnGUI.class, 5, Side.SERVER);
		wrapper.registerMessage(HandlerFilterPreview.class, MessageFilterPreview.class, 6, Side.CLIENT);
		wrapper.registerMessage(HandlerLoginSyncList.class, MessageLoginSyncList.class, 7, Side.CLIENT);
	}

	public static IThreadListener getThreadListener(MessageContext ctx) {
		return ctx.side == Side.SERVER ? (WorldServer) ctx.getServerHandler().player.world : getClientThreadListener();
	}

	@SideOnly(Side.CLIENT)
	public static IThreadListener getClientThreadListener() {
		return Minecraft.getMinecraft();
	}

	@SideOnly(Side.CLIENT)
	public static EntityPlayer getClientPlayerEntity() {
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
}
