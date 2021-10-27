package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.balm.api.item.BalmItem;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.block.ModBlocks;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.block.SortingChestBlock;
import net.blay09.mods.refinedrelocation.block.entity.SortingChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class SortingUpgradeItem extends BalmItem {

    public SortingUpgradeItem() {
        super(new Item.Properties().tab(ModItems.creativeModeTab));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        ItemStack itemStack = context.getItemInHand();
        InteractionHand hand = InteractionHand.MAIN_HAND;
        if (!level.isClientSide) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
                if (upgradeVanillaChest(player, level, pos, state)) {
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null && blockEntity.getCapability(Capabilities.SORTING_UPGRADABLE, facing).isPresent()) {
                LazyOptional<ISortingUpgradable> sortingUpgradableCap = blockEntity.getCapability(Capabilities.SORTING_UPGRADABLE, facing);
                return sortingUpgradableCap.map(sortingUpgradable -> {
                    Vec3 hit = context.getClickLocation();
                    if (sortingUpgradable.applySortingUpgrade(blockEntity, itemStack, player, level, pos, facing, hit.x, hit.y, hit.z, hand)) {
                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }

                        return InteractionResult.SUCCESS;
                    }

                    return InteractionResult.PASS;
                }).orElse(InteractionResult.PASS);
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean upgradeVanillaChest(Player player, Level level, BlockPos pos, BlockState state) {
        ChestBlockEntity tileEntity = (ChestBlockEntity) level.getBlockEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        if (ChestBlockEntity.getOpenCount(level, pos) > 0) {
            return false;
        }

        ItemStack[] inventory = new ItemStack[tileEntity.getContainerSize()];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = tileEntity.getItem(i);
        }

        tileEntity.clearContent();
        BlockState newState = ModBlocks.sortingChests[SortingChestType.WOOD.ordinal()].defaultBlockState().setValue(SortingChestBlock.FACING, state.getValue(ChestBlock.FACING));
        level.setBlockAndUpdate(pos, newState);
        SortingChestBlockEntity tileSortingChest = (SortingChestBlockEntity) level.getBlockEntity(pos);
        if (tileSortingChest != null) {
            for (int i = 0; i < inventory.length; i++) {
                tileSortingChest.getItemHandler().setStackInSlot(i, inventory[i]);
            }
        }

        return true;
    }

}
