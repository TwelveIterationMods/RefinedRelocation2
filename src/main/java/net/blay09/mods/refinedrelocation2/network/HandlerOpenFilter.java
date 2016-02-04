package net.blay09.mods.refinedrelocation2.network;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerOpenFilter implements IMessageHandler<MessageOpenFilter, IMessage> {
    @Override
    public IMessage onMessage(MessageOpenFilter message, MessageContext ctx) {
        RefinedRelocation2.proxy.addScheduledTask(() -> {
            EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_FILTER, entityPlayer.worldObj, message.getPos().getX(), message.getPos().getY(), message.getPos().getZ());
        });
        return null;
    }
}
