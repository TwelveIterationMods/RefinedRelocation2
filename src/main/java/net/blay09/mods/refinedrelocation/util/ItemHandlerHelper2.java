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
			if (itemStack != null) {
				spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
			}
		}
	}

	public static void spawnItemStack(World world, double x, double y, double z, ItemStack itemStack) {
		float offsetX = rand.nextFloat() * 0.8f + 0.1f;
		float offsetY = rand.nextFloat() * 0.8f + 0.1f;
		float offsetZ = rand.nextFloat() * 0.8f + 0.1f;

		while (itemStack.stackSize > 0) {
			int stackSize = rand.nextInt(21) + 10;
			if (stackSize > itemStack.stackSize) {
				stackSize = itemStack.stackSize;
			}
			itemStack.stackSize -= stackSize;

			EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, new ItemStack(itemStack.getItem(), stackSize, itemStack.getMetadata()));
			if (itemStack.hasTagCompound()) {
				entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
			}
			float motion = 0.05f;
			entityItem.motionX = rand.nextGaussian() * motion;
			entityItem.motionY = rand.nextGaussian() * motion + 0.2;
			entityItem.motionZ = rand.nextGaussian() * motion;
			world.spawnEntityInWorld(entityItem);
		}
	}

}
