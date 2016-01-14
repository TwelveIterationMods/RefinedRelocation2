package net.blay09.mods.refinedrelocation2.network;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerOpenToolbox implements IMessageHandler<MessageOpenToolbox, IMessage> {
    @Override
    public IMessage onMessage(MessageOpenToolbox message, MessageContext ctx) {
        RefinedRelocation2.proxy.addScheduledTask(() -> {
            EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_TOOLBOX, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
        });
        return null;
    }
}
