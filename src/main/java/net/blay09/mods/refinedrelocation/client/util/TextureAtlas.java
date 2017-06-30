package net.blay09.mods.refinedrelocation.client.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ProgressManager;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TextureAtlas extends AbstractTexture implements ITickableTextureObject, IResourceManagerReloadListener {

	private final ResourceLocation atlasFile;
	private final String basePath;
	private final Map<String, TextureAtlasRegion> registeredSprites = Maps.newHashMap();
	private final Map<String, TextureAtlasRegion> uploadedSprites = Maps.newHashMap();
	private final List<TextureAtlasRegion> animatedSprites = Lists.newArrayList();
	private final TextureAtlasRegion missingImage;

	public TextureAtlas(ResourceLocation atlasFile, String basePath) {
		this.atlasFile = atlasFile;
		this.basePath = basePath;
		this.missingImage = new TextureAtlasRegion(this, "missingno");
	}

	private void initMissingImage() {
		int[] data = TextureUtil.MISSING_TEXTURE_DATA;
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
		Stitcher stitcher = new Stitcher(maxTextureSize, maxTextureSize, 0, 0);

		ProgressManager.ProgressBar bar = ProgressManager.push("GUI Texture stitching", registeredSprites.size());
		for (Map.Entry<String, TextureAtlasRegion> entry : registeredSprites.entrySet()) {
			TextureAtlasSprite sprite = entry.getValue();
			ResourceLocation location = new ResourceLocation(sprite.getIconName());
			ResourceLocation fullLocation = completeResourceLocation(location);
			bar.step(fullLocation.getResourcePath());
			IResource resource = null;
			if (sprite.hasCustomLoader(resourceManager, location)) {
				if (sprite.load(resourceManager, location, l -> registeredSprites.get(l.toString()))) {
					continue;
				}
			} else {
				try {
					resource = resourceManager.getResource(fullLocation);
					PngSizeInfo pngSizeInfo = PngSizeInfo.makeFromResource(resource);
					boolean isAnimated = resource.getMetadata("animation") != null;
					sprite.loadSprite(pngSizeInfo, isAnimated);
				} catch (RuntimeException e) {
					FMLClientHandler.instance().trackBrokenTexture(fullLocation, e.getMessage());
					continue;
				} catch (IOException e) {
					FMLClientHandler.instance().trackMissingTexture(fullLocation);
					continue;
				} finally {
					IOUtils.closeQuietly(resource);
				}
			}
			stitcher.addSprite(sprite);
		}
		ProgressManager.pop(bar);
		stitcher.addSprite(missingImage);
		bar = ProgressManager.push("GUI Texture creation", 2);
		bar.step("Stitching");
		stitcher.doStitch();

		RefinedRelocation.logger.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), basePath);
		bar.step("Allocating GL texture");
		TextureUtil.allocateTextureImpl(getGlTextureId(), 0, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
		ProgressManager.pop(bar);

		Map<String, TextureAtlasRegion> map = Maps.newHashMap(registeredSprites);
		bar = ProgressManager.push("GUI Texture upload", stitcher.getStichSlots().size());
		for (TextureAtlasSprite sprite : stitcher.getStichSlots()) {
			bar.step(sprite.getIconName());

			ResourceLocation location = new ResourceLocation(sprite.getIconName());
			ResourceLocation fullLocation = completeResourceLocation(location);
			if(sprite != missingImage && !sprite.hasCustomLoader(resourceManager, location)) {
				try {
					sprite.loadSpriteFrames(resourceManager.getResource(fullLocation), 1);
				} catch (RuntimeException e) {
					RefinedRelocation.logger.error("Unable to parse metadata from {}", fullLocation, e);
				} catch (IOException e) {
					RefinedRelocation.logger.error("Using missing texture, unable to load {}", fullLocation, e);
				}
			}

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

		ProgressManager.pop(bar);
	}

	public TextureAtlasRegion registerSprite(ResourceLocation location) {
		return registeredSprites.computeIfAbsent(location.toString(), k -> new TextureAtlasRegion(this, location));
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

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		loadSprites(resourceManager);
	}
}
