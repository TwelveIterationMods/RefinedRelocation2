package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStackLimiter extends Item {

    public static final String name = "stack_limiter";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public ItemStackLimiter() {
        super(new Item.Properties().group(RefinedRelocation.itemGroup).maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TextComponentTranslation("tooltip.refinedrelocation:stack_limiter_module"));
    }

}
