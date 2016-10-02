package net.blay09.mods.refinedrelocation.compat.ironchest;

import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.ChestChangerType;
import cpw.mods.ironchest.ContainerIronChest;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.TileEntityIronChest;
import cpw.mods.ironchest.client.GUIChest;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.block.BlockMod;
import net.blay09.mods.refinedrelocation.compat.Compat;
import net.blay09.mods.refinedrelocation.compat.RefinedAddon;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Locale;

public class IronChestAddon extends RefinedAddon {

	private static Block ironChest;
	private static BlockSortingIronChest sortingIronChest;

	@Override
	public void preInit() {
		ironChest = Block.REGISTRY.getObject(new ResourceLocation(Compat.IRONCHEST, "BlockIronChest"));
		sortingIronChest = new BlockSortingIronChest(ironChest);
		GameRegistry.register(sortingIronChest);
		GameRegistry.register(new ItemBlockSortingIronChest(sortingIronChest));
		GameRegistry.registerTileEntity(TileSortingIronChest.class, sortingIronChest.getRegistryName().toString());
		GameRegistry.registerTileEntity(TileSortingIronChest.Dirt.class, sortingIronChest.getRegistryName().toString() + "_dirt");
		GameRegistry.registerTileEntity(TileSortingIronChest.Obsidian.class, sortingIronChest.getRegistryName().toString() + "_obsidian");
		GameRegistry.registerTileEntity(TileSortingIronChest.Crystal.class, sortingIronChest.getRegistryName().toString() + "_crystal");
		GameRegistry.registerTileEntity(TileSortingIronChest.Diamond.class, sortingIronChest.getRegistryName().toString() + "_diamond");
		GameRegistry.registerTileEntity(TileSortingIronChest.Copper.class, sortingIronChest.getRegistryName().toString() + "_copper");
		GameRegistry.registerTileEntity(TileSortingIronChest.Gold.class, sortingIronChest.getRegistryName().toString() + "_gold");
		GameRegistry.registerTileEntity(TileSortingIronChest.Silver.class, sortingIronChest.getRegistryName().toString() + "_silver");

		final Object modInstance = Loader.instance().getIndexedModList().get(Compat.IRONCHEST).getMod();
		final ITileGuiHandler tileGuiHandler = new ITileGuiHandler() {
			@Override
			public void openGui(EntityPlayer player, TileOrMultipart tileEntity) {
				TileSortingIronChest tileIronChest = (TileSortingIronChest) tileEntity.getTileEntity();
				if (tileIronChest != null) {
					player.openGui(modInstance, tileIronChest.getType().ordinal(), tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
				}
			}
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

	@SideOnly(Side.CLIENT)
	public void preInitClient() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileSortingIronChest.class, new RenderSortingIronChest(sortingIronChest));

		Item sortingIronChestItem = Item.getItemFromBlock(sortingIronChest);
		if (sortingIronChestItem != null) {
			ModelLoader.setCustomMeshDefinition(sortingIronChestItem, new ItemMeshDefinition() {
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack) {
					return new ModelResourceLocation(sortingIronChest.getRegistryName(), "variant=" + IronChestType.VALUES[stack.getItemDamage()].name().toLowerCase(Locale.ENGLISH));
				}
			});
		}
	}

	@Override
	public void init() {
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.IRON.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.IRON.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.GOLD.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.GOLD.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.DIAMOND.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.DIAMOND.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.COPPER.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.COPPER.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.SILVER.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.SILVER.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.CRYSTAL.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.CRYSTAL.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.OBSIDIAN.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.OBSIDIAN.ordinal()), 'H', Blocks.HOPPER);
		GameRegistry.addShapedRecipe(new ItemStack(sortingIronChest, 1, IronChestType.DIRTCHEST9000.ordinal()), " B ", "RCR", " H ", 'B', Items.WRITABLE_BOOK, 'R', Items.REDSTONE, 'C', new ItemStack(ironChest, 1, IronChestType.DIRTCHEST9000.ordinal()), 'H', Blocks.HOPPER);
	}

	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getWorld().isRemote || event.getItemStack() == null || !(event.getItemStack().getItem() instanceof ItemChestChanger)) {
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
			if (state != sortingIronChest.getStateFromMeta(IronChestType.valueOf(type.source.getName().toUpperCase()).ordinal())) {
				return;
			}
		}

		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity == null) {
			return;
		}
		ItemStack[] previousContent;
		EnumFacing previousFacing;
		IRootFilter rootFilter = tileEntity.getCapability(Capabilities.ROOT_FILTER, null);
		ISortingInventory sortingInventory = tileEntity.getCapability(Capabilities.SORTING_INVENTORY, null);
		if (tileEntity instanceof TileSortingIronChest) {
			previousContent = ((TileSortingIronChest) tileEntity).chestContents;
			previousFacing = ((TileSortingIronChest) tileEntity).getFacing();
		} else if (tileEntity instanceof TileSortingChest) {
			previousFacing = state.getValue(BlockChest.FACING);
			TileSortingChest tileSortingChest = (TileSortingChest) tileEntity;
			if (tileSortingChest.getDoorAnimator().getNumPlayersUsing() > 0) {
				return;
			}
			previousContent = new ItemStack[tileSortingChest.getItemHandler().getSlots()];
			for (int i = 0; i < previousContent.length; i++) {
				previousContent[i] = tileSortingChest.getItemHandler().getStackInSlot(i);
			}
		} else {
			return;
		}

		tileEntity.updateContainingBlockInfo();

		world.removeTileEntity(pos);
		world.setBlockToAir(pos);

		IBlockState newState = sortingIronChest.getDefaultState().withProperty(BlockIronChest.VARIANT_PROP, type.target);
		world.setBlockState(pos, newState, 3);

		TileEntity newTileEntity = world.getTileEntity(pos);
		if (newTileEntity instanceof TileSortingIronChest) {
			((TileSortingIronChest) newTileEntity).setContents(previousContent);
			((TileSortingIronChest) newTileEntity).setFacing(previousFacing);
			IRootFilter newRootFilter = newTileEntity.getCapability(Capabilities.ROOT_FILTER, null);
			newRootFilter.deserializeNBT(rootFilter.serializeNBT());
			ISortingInventory newSortingInventory = newTileEntity.getCapability(Capabilities.SORTING_INVENTORY, null);
			newSortingInventory.deserializeNBT(sortingInventory.serializeNBT());
		}

		event.getItemStack().stackSize = event.getEntityPlayer().capabilities.isCreativeMode ? event.getItemStack().stackSize : event.getItemStack().stackSize - 1;
		event.setCanceled(true);
	}

	private class IronChestCapabilityProvider implements ICapabilityProvider, ISortingUpgradable {
		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == Capabilities.SORTING_UPGRADABLE;
		}

		@Override
		@SuppressWarnings("unchecked")
		@Nullable
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
			return capability == Capabilities.SORTING_UPGRADABLE ? (T) this : null;
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
			world.setBlockState(pos, sortingIronChest.getDefaultState()
					.withProperty(BlockIronChest.VARIANT_PROP, oldState.getValue(BlockIronChest.VARIANT_PROP)));
			TileSortingIronChest sortingIronChest = (TileSortingIronChest) world.getTileEntity(pos);
			if (sortingIronChest == null) {
				return false;
			}
			sortingIronChest.readFromNBT(storedData);
			return true;
		}
	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent.TileEntity event) {
		if (event.getTileEntity() instanceof TileEntityIronChest) {
			event.addCapability(new ResourceLocation(RefinedRelocation.MOD_ID, "SortingUpgradable"), new IronChestCapabilityProvider());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onInitGui(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof GUIChest) {
			GuiContainer guiContainer = (GuiContainer) event.getGui();
			TileEntityIronChest tileIronChest = (TileEntityIronChest) guiContainer.inventorySlots.inventorySlots.get(0).inventory;
			if (tileIronChest instanceof TileSortingIronChest) {
				GuiButton button = RefinedRelocationAPI.createOpenFilterButton(guiContainer, tileIronChest, -1);
				button.xPosition += 7;
				button.yPosition += 3;
				event.getButtonList().add(button);
			}
		}
	}
}
