package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingUpgradable;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class ItemSortingUpgrade extends Item {

    public static final String name = "sorting_upgrade";
    public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

    public ItemSortingUpgrade() {
        super(new Item.Properties().group(RefinedRelocation.itemGroup));
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext useContext) {
        EntityPlayer player = useContext.getPlayer();
        if (player == null) {
            return EnumActionResult.PASS;
        }

        World world = useContext.getWorld();
        BlockPos pos = useContext.getPos();
        EnumFacing facing = useContext.getFace();
        ItemStack itemStack = useContext.getItem();
        EnumHand hand = EnumHand.MAIN_HAND;
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
                if (upgradeVanillaChest(player, world, pos, state)) {
                    if (!player.abilities.isCreativeMode) {
                        itemStack.shrink(1);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }

            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.getCapability(CapabilitySortingUpgradable.CAPABILITY, facing).isPresent()) {
                LazyOptional<ISortingUpgradable> sortingUpgradableCap = tileEntity.getCapability(CapabilitySortingUpgradable.CAPABILITY, facing);
                return sortingUpgradableCap.map(sortingUpgradable -> {
                    if (sortingUpgradable.applySortingUpgrade(tileEntity, itemStack, player, world, pos, facing, useContext.getHitX(), useContext.getHitY(), useContext.getHitZ(), hand)) {
                        if (!player.abilities.isCreativeMode) {
                            itemStack.shrink(1);
                        }

                        return EnumActionResult.SUCCESS;
                    }

                    return EnumActionResult.PASS;
                }).orElse(EnumActionResult.PASS);
            }
        }

        return EnumActionResult.PASS;
    }

    private static boolean upgradeVanillaChest(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
        TileEntityChest tileEntity = (TileEntityChest) world.getTileEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        if (tileEntity.numPlayersUsing > 0) {
            return false;
        }

        ItemStack[] inventory = new ItemStack[tileEntity.getSizeInventory()];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = tileEntity.getStackInSlot(i);
        }

        tileEntity.clear();
        IBlockState newState = ModBlocks.sortingChest.getDefaultState().with(BlockStateProperties.FACING, state.get(BlockChest.FACING));
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
