package net.blay09.mods.refinedrelocation2.client.render;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.refinedrelocation2.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.List;

public class ItemModelToolbox implements ISmartItemModel, IPerspectiveAwareModel {

    public static final ModelResourceLocation resource = new ModelResourceLocation("refinedrelocation2:toolbox", "inventory");
    private final IFlexibleBakedModel baseModel;

    private IFlexibleBakedModel currentModel;
    private IFlexibleBakedModel currentModelWithToolbox;

    public ItemModelToolbox(IFlexibleBakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        ItemStack activeStack = ModItems.toolbox.toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            IBakedModel itemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(activeStack);
            if(itemModel instanceof IFlexibleBakedModel) {
                currentModel = (IFlexibleBakedModel) itemModel;
            } else {
                currentModel = new IFlexibleBakedModel.Wrapper(itemModel, DefaultVertexFormats.ITEM);
            }
            if(currentModel.isBuiltInRenderer()) {
                return baseModel;
            }
            currentModelWithToolbox = new MultiModel.Baked(resource, true, baseModel, ImmutableMap.of("overlay", currentModel));
            return this;
        }
        return baseModel;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return null;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return null;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public VertexFormat getFormat() {
        return DefaultVertexFormats.ITEM;
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        IFlexibleBakedModel model = cameraTransformType == ItemCameraTransforms.TransformType.GUI ? currentModelWithToolbox : currentModel;
        if(model instanceof IPerspectiveAwareModel) {
            return ((IPerspectiveAwareModel) model).handlePerspective(cameraTransformType);
        }
        return Pair.of(model, new TRSRTransformation(model.getItemCameraTransforms().getTransform(cameraTransformType)).getMatrix());
    }
}
