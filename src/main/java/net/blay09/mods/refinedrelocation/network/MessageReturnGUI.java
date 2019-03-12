package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.api.container.IContainerReturnable;
import net.blay09.mods.refinedrelocation.api.container.ReturnCallback;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class MessageReturnGUI {

    public static void handle(MessageReturnGUI message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            EntityPlayerMP player = context.getSender();
            if (player == null) {
                return;
            }

            Container container = player.openContainer;
            if (container instanceof ContainerRootFilter) {
                TileEntity tileEntity = ((ContainerRootFilter) container).getTileEntity();
                if (tileEntity instanceof IInteractionObject) {
                    NetworkHooks.openGui(player, (IInteractionObject) tileEntity, tileEntity.getPos());
                }
            } else if (container instanceof IContainerReturnable) {
                ReturnCallback callback = ((IContainerReturnable) container).getReturnCallback();
                if (callback != null) {
                    callback.returnToParentGui();
                }
            }
        });
    }
}
