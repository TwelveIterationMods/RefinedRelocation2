package net.blay09.mods.refinedrelocation.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class StackLimiterItem extends Item {

    public StackLimiterItem() {
        super(new Item.Properties().tab(ModItems.creativeModeTab).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.refinedrelocation:block_extender_module"));
    }

}
