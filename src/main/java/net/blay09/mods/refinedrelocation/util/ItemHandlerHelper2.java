package net.blay09.mods.refinedrelocation.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.Random;

public class ItemHandlerHelper2 {

	private static final Random rand = new Random();

	public static void dropItemHandlerItems(World world, BlockPos pos, IItemHandler itemHandler) {
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack itemStack = itemHandler.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
			}
		}
	}

	public static void spawnItemStack(World world, double x, double y, double z, ItemStack itemStack) {
		float offsetX = rand.nextFloat() * 0.8f + 0.1f;
		float offsetY = rand.nextFloat() * 0.8f + 0.1f;
		float offsetZ = rand.nextFloat() * 0.8f + 0.1f;

		while (!itemStack.isEmpty()) {
			int stackSize = rand.nextInt(21) + 10;
			if (stackSize > itemStack.getCount()) {
				stackSize = itemStack.getCount();
			}
			itemStack.shrink(stackSize);

			EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, new ItemStack(itemStack.getItem(), stackSize, itemStack.getMetadata()));
			if (itemStack.hasTagCompound()) {
				NBTTagCompound tagCompound = itemStack.getTagCompound();
				if(tagCompound != null) {
					entityItem.getEntityItem().setTagCompound(tagCompound.copy());
				}
			}
			float motion = 0.05f;
			entityItem.motionX = rand.nextGaussian() * motion;
			entityItem.motionY = rand.nextGaussian() * motion + 0.2;
			entityItem.motionZ = rand.nextGaussian() * motion;
			world.spawnEntity(entityItem);
		}
	}

}
