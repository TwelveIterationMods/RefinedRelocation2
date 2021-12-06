package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.entity.ModBlockEntities;
import net.blay09.mods.refinedrelocation.block.entity.SortingChestBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public enum SortingChestType {
    WOOD(27, 9, "sorting_chest", "normal", new ResourceLocation("textures/gui/container/generic_54.png"), 176, 168, 256, 256),
    IRON(54, 9, "sorting_iron_chest", "iron", new ResourceLocation("textures/gui/container/generic_54.png"), 176, 222, 256, 256),
    GOLD(84, 12, "sorting_gold_chest", "gold", new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/gold_chest.png"), 230, 240, 230, 240),
    DIAMOND(112, 16, "sorting_diamond_chest", "diamond", new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/diamond_chest.png"), 302, 240, 302, 240);

    private final int inventorySize;
    private final int containerRowSize;
    private final String registryName;
    private final String texture;
    private final ResourceLocation guiTextureLocation;
    private final int guiWidth;
    private final int guiHeight;
    private final int guiTextureWidth;
    private final int guiTextureHeight;

    private Material material;

    SortingChestType(int inventorySize, int containerRowSize, String registryName, String texture, ResourceLocation guiTextureLocation, int guiWidth, int guiHeight, int guiTextureWidth, int guiTextureHeight) {
        this.inventorySize = inventorySize;
        this.containerRowSize = containerRowSize;
        this.registryName = registryName;
        this.texture = texture;
        this.guiTextureLocation = guiTextureLocation;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.guiTextureWidth = guiTextureWidth;
        this.guiTextureHeight = guiTextureHeight;
    }

    public String getRegistryName() {
        return registryName;
    }

    public BlockEntityType<SortingChestBlockEntity> getTileEntityType() {
        return ModBlockEntities.sortingChests.get(ordinal()).get();
    }

    public ResourceLocation getTextureLocation() {
        return new ResourceLocation(RefinedRelocation.MOD_ID, "entity/sorting_chest/" + texture);
    }

    public Material getMaterial() {
        if (material == null) {
            material = new Material(Sheets.CHEST_SHEET, getTextureLocation());
        }

        return material;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public int getContainerRowSize() {
        return containerRowSize;
    }

    public ResourceLocation getGuiTextureLocation() {
        return guiTextureLocation;
    }

    public int getGuiWidth() {
        return guiWidth;
    }

    public int getGuiHeight() {
        return guiHeight;
    }

    public int getGuiTextureWidth() {
        return guiTextureWidth;
    }

    public int getGuiTextureHeight() {
        return guiTextureHeight;
    }
}
