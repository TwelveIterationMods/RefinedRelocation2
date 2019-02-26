package net.blay09.mods.refinedrelocation.compat.ironchest;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.compat.Compat;
import net.blay09.mods.refinedrelocation.compat.RefinedAddon;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IronChestAddon implements RefinedAddon {

	@Override
	public void preInit() {
		GameRegistry.registerTileEntity(TileSortingIronChest.class, BlockSortingIronChest.registryName.toString());
		GameRegistry.registerTileEntity(TileSortingIronChest.Dirt.class, BlockSortingIronChest.registryName.toString() + "_dirt");
		GameRegistry.registerTileEntity(TileSortingIronChest.Obsidian.class, BlockSortingIronChest.registryName.toString() + "_obsidian");
		GameRegistry.registerTileEntity(TileSortingIronChest.Crystal.class, BlockSortingIronChest.registryName.toString() + "_crystal");
		GameRegistry.registerTileEntity(TileSortingIronChest.Diamond.class, BlockSortingIronChest.registryName.toString() + "_diamond");
		GameRegistry.registerTileEntity(TileSortingIronChest.Copper.class, BlockSortingIronChest.registryName.toString() + "_copper");
		GameRegistry.registerTileEntity(TileSortingIronChest.Gold.class, BlockSortingIronChest.registryName.toString() + "_gold");
		GameRegistry.registerTileEntity(TileSortingIronChest.Silver.class, BlockSortingIronChest.registryName.toString() + "_silver");

		final Object modInstance = Loader.instance().getIndexedModList().get(Compat.IRONCHEST).getMod();
		final ITileGuiHandler tileGuiHandler = (player, tileEntity) -> {
			TileSortingIronChest tileIronChest = (TileSortingIronChest) tileEntity;
			player.openGui(modInstance, tileIronChest.getType().ordinal(), tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
		};
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Dirt.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Obsidian.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Crystal.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Diamond.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Copper.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Gold.class, tileGuiHandler);
		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Silver.class, tileGuiHandler);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockSortingIronChest().setRegistryName(BlockSortingIronChest.registryName)
		);
	}

	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlockSortingIronChest(Compat.sortingIronChest)
		);
	}

	@Override
	public void setupClient() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileSortingIronChest.class, new RenderSortingIronChest(Compat.sortingIronChest));
	}

	@Override
	public void init() {
		((BlockSortingIronChest) Compat.sortingIronChest).setDelegateBlock(Compat.ironChest);
	}

	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote || event.getItemStack().isEmpty() || !(event.getItemStack().getItem() instanceof ItemChestChanger)) {
			return;
		}

		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = world.getBlockState(pos);
		ChestChangerType type = ((ItemChestChanger) event.getItemStack().getItem()).type;
		if (type.canUpgrade(IronChestType.WOOD)) {
			if (state.getBlock() != ModBlocks.sortingChest) {
				return;
			}
		} else {
			if (state != Compat.sortingIronChest.getStateFromMeta(IronChestType.valueOf(type.source.getName().toUpperCase()).ordinal())) {
				return;
			}
		}

		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity == null) {
			return;
		}
		NonNullList<ItemStack> previousContent;
		EnumFacing previousFacing;
		IRootFilter rootFilter = tileEntity.getCapability(Capabilities.ROOT_FILTER, null);
		ISortingInventory sortingInventory = tileEntity.getCapability(Capabilities.SORTING_INVENTORY, null);
		if (tileEntity instanceof TileSortingIronChest) {
			previousContent = ((TileSortingIronChest) tileEntity).getItems();
			previousFacing = ((TileSortingIronChest) tileEntity).getFacing();
		} else if (tileEntity instanceof TileSortingChest) {
			previousFacing = state.getValue(BlockChest.FACING);
			TileSortingChest tileSortingChest = (TileSortingChest) tileEntity;
			if (tileSortingChest.getDoorAnimator().getNumPlayersUsing() > 0) {
				return;
			}
			previousContent = NonNullList.create();
			for (int i = 0; i < tileSortingChest.getItemHandler().getSlots(); i++) {
				previousContent.add(tileSortingChest.getItemHandler().getStackInSlot(i));
			}
		} else {
			return;
		}

		tileEntity.updateContainingBlockInfo();

		world.removeTileEntity(pos);
		world.removeBlock(pos);

		IBlockState newState = Compat.sortingIronChest.getDefaultState().with(BlockIronChest.VARIANT_PROP, type.target);
		world.setBlockState(pos, newState, 3);

		TileEntity newTileEntity = world.getTileEntity(pos);
		if (newTileEntity instanceof TileSortingIronChest) {
			((TileSortingIronChest) newTileEntity).setContents(previousContent);
			((TileSortingIronChest) newTileEntity).setFacing(previousFacing);
			IRootFilter newRootFilter = newTileEntity.getCapability(Capabilities.ROOT_FILTER, null);
			if(rootFilter != null && newRootFilter != null) {
				newRootFilter.deserializeNBT(rootFilter.serializeNBT());
			}
			ISortingInventory newSortingInventory = newTileEntity.getCapability(Capabilities.SORTING_INVENTORY, null);
			if(sortingInventory != null && newSortingInventory != null) {
				newSortingInventory.deserializeNBT(sortingInventory.serializeNBT());
			}
		}

		if(!event.getEntityPlayer().abilities.isCreativeMode) {
			event.getItemStack().shrink(1);
		}
		event.setCanceled(true);
	}

	private class IronChestCapabilityProvider implements ICapabilityProvider, ISortingUpgradable {
		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
			return Capabilities.SORTING_UPGRADABLE.orEmpty(cap, LazyOptional.of(() -> this));
		}

		@Override
		public boolean applySortingUpgrade(TileEntity tileEntity, ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
			TileEntityIronChest tileIronChest = (TileEntityIronChest) tileEntity;
			if (tileIronChest.lidAngle > 0) {
				return false;
			}
			NBTTagCompound storedData = tileIronChest.writeToNBT(new NBTTagCompound());
			tileIronChest.clear();
			IBlockState oldState = world.getBlockState(pos);
			world.setBlockState(pos, Compat.sortingIronChest.getDefaultState()
					.with(BlockIronChest.VARIANT_PROP, oldState.get(BlockIronChest.VARIANT_PROP)));
			TileSortingIronChest sortingIronChest = (TileSortingIronChest) world.getTileEntity(pos);
			if (sortingIronChest == null) {
				return false;
			}
			sortingIronChest.readFromNBT(storedData);
			return true;
		}
	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof TileEntityIronChest) {
			event.addCapability(new ResourceLocation(RefinedRelocation.MOD_ID, "SortingUpgradable"), new IronChestCapabilityProvider());
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onInitGui(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof GUIChest) {
			GuiContainer guiContainer = (GuiContainer) event.getGui();
			TileEntityIronChest tileIronChest = (TileEntityIronChest) guiContainer.inventorySlots.inventorySlots.get(0).inventory;
			if (tileIronChest instanceof TileSortingIronChest) {
				GuiButton button = RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileIronChest, -1);
				button.x += 7;
				button.y += 3;
				event.getButtonList().add(button);
			}
		}
	}
}
