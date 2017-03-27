package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

import java.util.List;

public class ItemStackLimiter extends ItemMod {

	public ItemStackLimiter() {
		setRegistryName("stack_limiter");
		setUnlocalizedName(getRegistryNameString());
		setCreativeTab(RefinedRelocation.creativeTab);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		subItems.add(new ItemStack(item, 1, 0));
		subItems.add(new ItemStack(item, 1, 1));
		subItems.add(new ItemStack(item, 1, 3));
		subItems.add(new ItemStack(item, 1, 7));
		subItems.add(new ItemStack(item, 1, 15));
		subItems.add(new ItemStack(item, 1, 31));
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add(I18n.format("tooltip.refinedrelocation:stack_limiter", stack.getMetadata() + 1));
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryNameString(), "inventory"));
	}
}
