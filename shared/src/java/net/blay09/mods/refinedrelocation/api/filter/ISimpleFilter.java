package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.nbt.Tag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface ISimpleFilter extends INBTSerializable<Tag> {
    @Deprecated
    default boolean passes(BlockEntity blockEntity, ItemStack itemStack) {
        return passes(blockEntity, itemStack, itemStack);
    }

    boolean passes(BlockEntity blockEntity, ItemStack itemStack, ItemStack originalStack);

    @Nullable
    default MenuProvider getConfiguration(Player player, BlockEntity blockEntity, int rootFilterIndex, int filterIndex) {
        return null;
    }

    default boolean hasConfiguration() {
        return false;
    }
}
