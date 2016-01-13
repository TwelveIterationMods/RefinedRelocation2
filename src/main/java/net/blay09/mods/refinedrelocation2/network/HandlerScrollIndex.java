package net.blay09.mods.refinedrelocation2.network;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.item.IScrollableItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerScrollIndex implements IMessageHandler<MessageScrollIndex, IMessage> {
    @Override
    public IMessage onMessage(MessageScrollIndex message, MessageContext ctx) {
        RefinedRelocation2.proxy.addScheduledTask(() -> {
            EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
            ItemStack itemStack = entityPlayer.getHeldItem();
            if(itemStack != null && itemStack.getItem() instanceof IScrollableItem) {
                ((IScrollableItem) itemStack.getItem()).setScrollIndex(entityPlayer, itemStack, message.getIndex());
            }
        });
        return null;
    }
}
