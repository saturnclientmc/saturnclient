package org.auraclient.auraclient.cloaks;

import net.minecraft.util.Identifier;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.auth.AuraApi;
import org.auraclient.auraclient.cloaks.utils.AnimatedCapeData;
import org.auraclient.auraclient.cloaks.utils.IdentifierUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the cloak/cape system for Aura Client.
 * Handles loading and caching of player capes.
 */
public class Cloaks {
    private static final String CAPES_RESOURCE_PATH = "assets/auraclient/textures/capes/";
    public static final List<String> availableCloaks = new ArrayList<>();
    public static Identifier capeCacheIdentifier = null;
    public static final Map<String, String> playerCapes = new HashMap<>();
    public static final Map<String, List<AnimatedCapeData>> animatedCapes = new ConcurrentHashMap<>();
    private static final Map<String, Long> lastFrameTime = new ConcurrentHashMap<>();

    /**
     * Initializes the cloak system.
     * Loads cape textures from resources.
     */
    public static void initialize() {
        availableCloaks.add(0, "");
    }

    /**
     * Handles loading and caching of a new cape texture.
     * 
     * @param capeName Name of the cape file to load
     */
    public static void setCape(String uuid, String capeName) {
        AuraApi.setCloak(capeName);
        Cloaks.playerCapes.put(uuid, capeName);

        if (!capeName.isEmpty()) {
            if (capeName.endsWith(".gif")) {
                loadAnimatedCape(uuid, capeName);
            } else {
                loadStaticCape(capeName);
            }
        }
    }

    /**
     * Loads and processes a static cape from a PNG file in resources.
     * 
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

    private static void loadAnimatedCape(String uuid, String fileName) {
        try {
            String resourcePath = CAPES_RESOURCE_PATH + fileName;
            InputStream inputStream = Cloaks.class.getClassLoader().getResourceAsStream(resourcePath);

            if (inputStream != null) {
                ImageInputStream stream = ImageIO.createImageInputStream(inputStream);
                ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
                reader.setInput(stream);

                List<AnimatedCapeData> frames = new ArrayList<>();
                int frameCount = reader.getNumImages(true);

                for (int i = 0; i < frameCount; i++) {
                    BufferedImage frame = reader.read(i);
                    String frameId = fileName.replace(".gif", "") + "_frame_" + i;
                    Identifier frameIdentifier = Identifier.of(AuraClient.MOD_ID, "capes_" + frameId);
                    IdentifierUtils.registerBufferedImageTexture(frameIdentifier, frame);

                    // Get frame delay in milliseconds (default to 100ms if not specified)
                    int delay = 100;
                    try {
                        javax.imageio.metadata.IIOMetadata metadata = reader.getImageMetadata(i);
                        String metaFormatName = metadata.getNativeMetadataFormatName();
                        org.w3c.dom.Node root = metadata.getAsTree(metaFormatName);
                        org.w3c.dom.NodeList children = root.getChildNodes();

                        for (int j = 0; j < children.getLength(); j++) {
                            org.w3c.dom.Node node = children.item(j);
                            if (node.getNodeName().equals("GraphicControlExtension")) {
                                org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
                                org.w3c.dom.Node delayNode = attrs.getNamedItem("delayTime");
                                if (delayNode != null) {
                                    delay = Integer.parseInt(delayNode.getNodeValue()) * 10; // Convert centiseconds to
                                                                                             // milliseconds
                                }
                                break;
                            }
                        }
                    } catch (Exception e) {
                        AuraClient.LOGGER.warn("Could not read frame delay for " + fileName + " frame " + i);
                    }

                    frames.add(new AnimatedCapeData(frameIdentifier, delay));
                }

                animatedCapes.put(uuid, frames);
                lastFrameTime.put(uuid, System.currentTimeMillis());
            }
        } catch (IOException e) {
            AuraClient.LOGGER.error("Failed to load animated cape from resources: " + fileName, e);
        }
    }

    public static Identifier getCurrentCapeTexture(String uuid) {
        if (!playerCapes.containsKey(uuid)) {
            return null;
        }

        String capeName = playerCapes.get(uuid);
        if (capeName.endsWith(".gif")) {
            List<AnimatedCapeData> frames = animatedCapes.get(uuid);
            if (frames == null || frames.isEmpty()) {
                return null;
            }

            long currentTime = System.currentTimeMillis();
            long lastTime = lastFrameTime.getOrDefault(uuid, currentTime);
            int currentFrame = 0;

            // Find the current frame based on elapsed time
            long elapsedTime = currentTime - lastTime;
            long totalTime = 0;
            for (int i = 0; i < frames.size(); i++) {
                totalTime += frames.get(i).getDelayMs();
                if (elapsedTime < totalTime) {
                    currentFrame = i;
                    break;
                }
            }

            // Reset animation if we've gone through all frames
            if (elapsedTime >= totalTime) {
                lastFrameTime.put(uuid, currentTime);
                currentFrame = 0;
            }

            return frames.get(currentFrame).getTextureId();
        } else {
            return Identifier.of(AuraClient.MOD_ID, "textures/capes/" + capeName + ".png");
        }
    }
}