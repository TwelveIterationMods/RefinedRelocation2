package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingUpgradable;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class ItemSortingUpgrade extends Item {

    public static final String name = "sorting_upgrade";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public ItemSortingUpgrade() {
        super(new Item.Properties().group(RefinedRelocation.itemGroup));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext useContext) {
        PlayerEntity player = useContext.getPlayer();
        if (player == null) {
            return ActionResultType.PASS;
        }

        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        Direction facing = useContext.getFace();
        ItemStack itemStack = useContext.getItem();
        Hand hand = Hand.MAIN_HAND;
        if (!world.isRemote) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
                if (upgradeVanillaChest(player, world, pos, state)) {
                    if (!player.abilities.isCreativeMode) {
                        itemStack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }

            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.getCapability(CapabilitySortingUpgradable.CAPABILITY, facing).isPresent()) {
                LazyOptional<ISortingUpgradable> sortingUpgradableCap = tileEntity.getCapability(CapabilitySortingUpgradable.CAPABILITY, facing);
                return sortingUpgradableCap.map(sortingUpgradable -> {
                    Vec3d hit = useContext.getHitVec();
                    if (sortingUpgradable.applySortingUpgrade(tileEntity, itemStack, player, world, pos, facing, hit.x, hit.y, hit.z, hand)) {
                        if (!player.abilities.isCreativeMode) {
                            itemStack.shrink(1);
                        }

                        return ActionResultType.SUCCESS;
                    }

                    return ActionResultType.PASS;
                }).orElse(ActionResultType.PASS);
            }
        }

        return ActionResultType.PASS;
    }

    private static boolean upgradeVanillaChest(PlayerEntity player, World world, BlockPos pos, BlockState state) {
        ChestTileEntity tileEntity = (ChestTileEntity) world.getTileEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        if (ChestTileEntity.getPlayersUsing(world, pos) > 0) {
            return false;
        }

        ItemStack[] inventory = new ItemStack[tileEntity.getSizeInventory()];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = tileEntity.getStackInSlot(i);
        }

        tileEntity.clear();
        BlockState newState = ModBlocks.sortingChest.getDefaultState().with(BlockStateProperties.FACING, state.get(ChestBlock.FACING));
        world.setBlockState(pos, newState);
        TileSortingChest tileSortingChest = (TileSortingChest) world.getTileEntity(pos);
        if (tileSortingChest != null) {
            for (int i = 0; i < inventory.length; i++) {
                tileSortingChest.getItemHandler().setStackInSlot(i, inventory[i]);
            }
        }

        return true;
    }

}
