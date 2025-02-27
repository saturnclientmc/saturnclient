package org.auraclient.auraclient.cloaks;

import at.dhyan.open_imaging.GifDecoder;
import net.minecraft.util.Identifier;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.cloaks.utils.AnimatedCapeData;
import org.auraclient.auraclient.cloaks.utils.IdentifierUtils;
import org.auraclient.auraclient.cloaks.utils.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Manages the cloak/cape system for Aura Client.
 * Handles loading, caching, and animating player capes.
 */
public class Cloaks {
    private static final String CAPES_RESOURCE_PATH = "assets/auraclient/textures/capes/";
    private static final List<AnimatedCapeData> animatedCapeFrames = new ArrayList<>();
    private static int currentFrameIndex = 0;
    private static long lastFrameTime = System.currentTimeMillis();
    private static String lastCachedCape = "NoCache";
    
    public static final List<String> availableCapes = new ArrayList<>();
    public static Identifier capeCacheIdentifier = null;
    public static final Map<String, String> playerCapes = new HashMap<>();
    
    /**
     * Initializes the cloak system.
     * Loads cape textures from resources.
     */
    public static void initialize() {
        availableCapes.clear();
        availableCapes.add("");
        loadCloakTextures();
    }

    /**
     * Loads cloak textures from the resources.
     * Supports both static (.png) and animated (.gif) capes.
     */
    private static void loadCloakTextures() {
        // In a resource-based system, we would need to manually list available capes
        // For now, we can add default capes that we know exist in resources
        // This would need to be updated when adding new capes to resources
        availableCapes.add("glitch.png");
    }

    /**
     * Updates the cape animation state.
     * Called every tick to handle animated capes.
     */
    public static void tick() {
        String currentCape = playerCapes.values().stream().findFirst().orElse("");
        
        if (!lastCachedCape.equals(currentCape)) {
            handleCapeChange(currentCape);
        } else if (currentCape.endsWith(".gif")) {
            updateAnimatedCape();
        }
    }

    /**
     * Handles loading and caching of a new cape texture.
     * @param capeName Name of the cape file to load
     */
    private static void handleCapeChange(String capeName) {
        if (!capeName.isEmpty()) {
            if (capeName.endsWith(".gif")) {
                loadAnimatedCape(capeName);
            } else {
                loadStaticCape(capeName);
            }
            lastCachedCape = capeName;
        }
    }

    /**
     * Loads and processes an animated cape from a GIF file in resources.
     * @param fileName Name of the GIF file to load
     */
    private static void loadAnimatedCape(String fileName) {
        try {
            animatedCapeFrames.clear();
            String resourcePath = CAPES_RESOURCE_PATH + fileName;
            InputStream inputStream = Cloaks.class.getClassLoader().getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                GifDecoder.GifImage gif = GifDecoder.read(inputStream);
                int frameCount = gif.getFrameCount();
                
                for (int i = 0; i < frameCount; i++) {
                    BufferedImage frame = gif.getFrame(i);
                    int delay = gif.getDelay(i);
                    Identifier frameId = Identifier.of(AuraClient.MOD_ID, 
                        "capes_" + RandomUtils.randomStringLowercase(10));
                    
                    IdentifierUtils.registerBufferedImageTexture(frameId, frame);
                    animatedCapeFrames.add(new AnimatedCapeData(frameId, delay));
                }
                
                currentFrameIndex = 0;
                capeCacheIdentifier = animatedCapeFrames.get(0).getTextureId();
            }
            
        } catch (IOException e) {
            AuraClient.LOGGER.error("Failed to load animated cape from resources: " + fileName, e);
        }
    }

    /**
     * Loads and processes a static cape from a PNG file in resources.
     * @param fileName Name of the PNG file to load
     */
    private static void loadStaticCape(String fileName) {
        try {
            String resourcePath = CAPES_RESOURCE_PATH + fileName;
            InputStream inputStream = Cloaks.class.getClassLoader().getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                
                String safeFileName = fileName.toLowerCase(Locale.ROOT)
                    .replace(' ', '_')
                    .replaceAll("[^a-z0-9/._-]", "");
                
                capeCacheIdentifier = Identifier.of(AuraClient.MOD_ID, "capes_" + safeFileName);
                IdentifierUtils.registerBufferedImageTexture(capeCacheIdentifier, image);
            }
            
        } catch (IOException e) {
            AuraClient.LOGGER.error("Failed to load static cape from resources: " + fileName, e);
        }
    }

    /**
     * Updates the current frame of an animated cape.
     */
    private static void updateAnimatedCape() {
        if (currentFrameIndex >= animatedCapeFrames.size()) {
            currentFrameIndex = 0;
        }
        
        AnimatedCapeData currentFrame = animatedCapeFrames.get(currentFrameIndex);
        if (System.currentTimeMillis() > lastFrameTime + currentFrame.getDelayMs()) {
            lastFrameTime = System.currentTimeMillis();
            capeCacheIdentifier = currentFrame.getTextureId();
            currentFrameIndex++;
        }
    }
}