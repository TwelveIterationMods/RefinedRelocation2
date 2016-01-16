package net.blay09.mods.refinedrelocation2.balyware;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TextureAtlas extends AbstractTexture implements ITickableTextureObject {

    private static final Logger logger = LogManager.getLogger();

    private final ResourceLocation atlasFile;
    private final String basePath;
    private final Map<String, TextureAtlasRegion> registeredSprites = Maps.newHashMap();
    private final Map<String, TextureAtlasRegion> uploadedSprites = Maps.newHashMap();
    private final List<TextureAtlasRegion> animatedSprites = Lists.newArrayList();
    private TextureAtlasRegion missingImage;

    public TextureAtlas(ResourceLocation atlasFile, String basePath) {
        this.atlasFile = atlasFile;
        this.basePath = basePath;
        this.missingImage = new TextureAtlasRegion(this, "missingno");
    }

    private void initMissingImage() {
        int[] data = TextureUtil.missingTextureData;
        missingImage.setIconWidth(16);
        missingImage.setIconHeight(16);
        int[][] mipmapData = new int[1][];
        mipmapData[0] = data;
        missingImage.setFramesTextureData(Collections.singletonList(mipmapData));
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        loadSprites(resourceManager);
    }

    private void loadSprites(IResourceManager resourceManager) {
        registeredSprites.clear();
        Gson gson = new Gson();
        try {
            IResource resource = resourceManager.getResource(atlasFile);
            try(InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                JsonObject object = gson.fromJson(reader, JsonObject.class);
                if(object != null) {
                    JsonArray sprites = object.getAsJsonArray("sprites");
                    for(int i = 0; i < sprites.size(); i++) {
                        String name = sprites.get(i).getAsString();
                        registerSprite(new ResourceLocation(name));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        initMissingImage();
        deleteGlTexture();
        loadAtlas(resourceManager);
    }

    public void loadAtlas(IResourceManager resourceManager) {
        uploadedSprites.clear();
        animatedSprites.clear();
        int maxTextureSize = Minecraft.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(maxTextureSize, maxTextureSize, true, 0, 0);
        for (Map.Entry<String, TextureAtlasRegion> entry : registeredSprites.entrySet()) {
            TextureAtlasSprite sprite = entry.getValue();
            ResourceLocation location = new ResourceLocation(sprite.getIconName());
            ResourceLocation fullLocation = completeResourceLocation(location);

            if (sprite.hasCustomLoader(resourceManager, location)) {
                if (!sprite.load(resourceManager, location)) {
                    stitcher.addSprite(sprite);
                }
                continue;
            }

            try {
                IResource resource = resourceManager.getResource(fullLocation);
                BufferedImage[] image = new BufferedImage[]{TextureUtil.readBufferedImage(resource.getInputStream())};
                AnimationMetadataSection animation = resource.getMetadata("animation");
                sprite.loadSprite(image, animation);
            } catch (RuntimeException e) {
                net.minecraftforge.fml.client.FMLClientHandler.instance().trackBrokenTexture(fullLocation, e.getMessage());
                continue;
            } catch (IOException e) {
                net.minecraftforge.fml.client.FMLClientHandler.instance().trackMissingTexture(fullLocation);
                continue;
            }
            stitcher.addSprite(sprite);
        }
        stitcher.addSprite(missingImage);
        stitcher.doStitch();

        logger.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), basePath);
        TextureUtil.allocateTextureImpl(getGlTextureId(), 0, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        Map<String, TextureAtlasSprite> map = Maps.newHashMap(registeredSprites);

        for (TextureAtlasSprite sprite : stitcher.getStichSlots()) {
            String name = sprite.getIconName();
            map.remove(name);
            uploadedSprites.put(name, (TextureAtlasRegion) sprite);

            try {
                TextureUtil.uploadTextureMipmap(sprite.getFrameTextureData(0), sprite.getIconWidth(), sprite.getIconHeight(), sprite.getOriginX(), sprite.getOriginY(), false, false);
            } catch (Throwable throwable) {
                CrashReport report = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                CrashReportCategory category = report.makeCategory("Texture being stitched together");
                category.addCrashSection("Atlas path", basePath);
                category.addCrashSection("Sprite", sprite);
                throw new ReportedException(report);
            }

            if (sprite.hasAnimationMetadata()) {
                animatedSprites.add((TextureAtlasRegion) sprite);
            }
        }

        for (TextureAtlasSprite sprite : map.values()) {
            sprite.copyFrom(missingImage);
        }
    }

    public TextureAtlasRegion registerSprite(ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        } else {
            TextureAtlasRegion sprite = registeredSprites.get(location.toString());
            if (sprite == null) {
                sprite = new TextureAtlasRegion(this, location);
                registeredSprites.put(location.toString(), sprite);
            }
            return sprite;
        }
    }

    public TextureAtlasRegion getSprite(String name) {
        TextureAtlasRegion sprite = uploadedSprites.get(name);
        if (sprite == null) {
            sprite = missingImage;
        }
        return sprite;
    }

    @Override
    public void tick() {
        GlStateManager.bindTexture(getGlTextureId());
        for (TextureAtlasSprite sprite : animatedSprites) {
            sprite.updateAnimation();
        }
    }

    private ResourceLocation completeResourceLocation(ResourceLocation resource) {
        return new ResourceLocation(resource.getResourceDomain(), String.format("%s/%s%s", basePath, resource.getResourcePath(), ".png"));
    }

}
