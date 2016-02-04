package net.blay09.mods.refinedrelocation2.balyware;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class ItemUtils {

    private static final Random random = new Random();

    public static ItemStack insertItemSmart(IItemHandler dest, ItemStack itemStack, boolean simulate) {
        if (dest == null || itemStack == null) {
            return itemStack;
        }

        int firstEmptySlot = -1;
        for (int i = 0; i < dest.getSlots(); i++) {
            if(dest.getStackInSlot(i) == null) {
                firstEmptySlot = i;
            } else {
                itemStack = dest.insertItem(i, itemStack, simulate);
                if (itemStack == null || itemStack.stackSize <= 0) {
                    return null;
                }
            }
        }
        if(firstEmptySlot != -1) {
            itemStack = dest.insertItem(firstEmptySlot, itemStack, simulate);
            if(itemStack == null || itemStack.stackSize <= 0) {
                return null;
            }
        }

        return itemStack;
    }

    public static void dropInventoryItems(World world, BlockPos pos, ItemStackHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);
            if (itemStack != null) {
                float offsetX = random.nextFloat() * 0.8f + 0.1f;
                float offsetY = random.nextFloat() * 0.8f + 0.1f;
                float offsetZ = random.nextFloat() * 0.8f + 0.1f;
                while (itemStack.stackSize > 0) {
                    int stackSize = random.nextInt(21) + 10;
                    if (stackSize > itemStack.stackSize) {
                        stackSize = itemStack.stackSize;
                    }

                    itemStack.stackSize -= stackSize;
                    EntityItem entityitem = new EntityItem(world, pos.getX() + (double) offsetX, pos.getY() + (double) offsetY, pos.getZ() + (double) offsetZ, new ItemStack(itemStack.getItem(), stackSize, itemStack.getMetadata()));
                    if (itemStack.hasTagCompound()) {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                    }

                    float motionScale = 0.05f;
                    entityitem.motionX = random.nextGaussian() * (double) motionScale;
                    entityitem.motionY = random.nextGaussian() * (double) motionScale + 0.20000000298023224D;
                    entityitem.motionZ = random.nextGaussian() * (double) motionScale;
                    world.spawnEntityInWorld(entityitem);
                }
            }
        }
    }

}
