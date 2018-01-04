package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStackLimiter extends Item {

	public static final String name = "stack_limiter";
	public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

	public ItemStackLimiter() {
		setUnlocalizedName(registryName.toString());
		setCreativeTab(RefinedRelocation.creativeTab);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("tooltip.refinedrelocation:block_extender_module"));
	}

}
