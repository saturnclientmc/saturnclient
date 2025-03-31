package org.saturnclient.saturnclient.cosmetics.cloaks;

import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.saturnclient.saturnclient.cosmetics.cloaks.utils.AnimatedCloakData;
import org.saturnclient.saturnclient.cosmetics.cloaks.utils.IdentifierUtils;
import org.saturnclient.saturnclient.auth.SaturnPlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the cloak system for Saturn Client.
 * Handles loading and caching of player cloaks.
 * Originally created by IIpho3nix and modified for Saturn Client by leo.
 */
public class Cloaks {
    private static final String[] ANIMATED_CLOAKS = { "glitch" };

    private static final String CLOAKS_RESOURCE_PATH = "assets/saturnclient/textures/cloaks/";
    public static final List<String> availableCloaks = new ArrayList<>();
    public static Identifier cloakCacheIdentifier = null;
    public static final Map<String, List<AnimatedCloakData>> animatedCloaks = new ConcurrentHashMap<>();
    private static final Map<String, Long> lastFrameTime = new ConcurrentHashMap<>();

    /**
     * Initializes the cloak system.
     * Loads cloak textures from resources.
     */
    public static void initialize() {
        availableCloaks.add(0, "");
    }

    /**
     * Handles loading and caching of a new cloak texture.
     * 
     * @param cloakName Name of the cloak file to load
     */
    public static void setCloak(String uuid, String cloakName) {
        SaturnSocket.setCloak(cloakName);
        setCloakSilent(uuid, cloakName);
        SaturnClient.LOGGER.info("Cloak set to " + cloakName);
    }

    /**
     * Handles loading and caching of a new cloak texture, without connecting to the
     * server.
     * 
     * @param cloakName Name of the cloak file to load
     */
    public static void setCloakSilent(String uuid, String cloakName) {
        SaturnPlayer player = SaturnSocket.players.get(uuid);
        if (player == null) {
            SaturnSocket.players.put(uuid, new SaturnPlayer(cloakName, null));
        } else {
            player.cloak = cloakName;
        }

        if (!cloakName.isEmpty()) {
            if (Arrays.asList(ANIMATED_CLOAKS).contains(cloakName)) {
                new Thread(() -> loadAnimatedCloak(uuid, cloakName + ".gif")).start();
            } else {
                loadStaticCloak(cloakName + ".png");
            }
        }
    }

    /**
     * Loads and processes a static cloak from a PNG file in resources.
     * 
     * @param fileName Name of the PNG file to load
     */
    private static void loadStaticCloak(String fileName) {
        try {
            String resourcePath = CLOAKS_RESOURCE_PATH + fileName;
            InputStream inputStream = Cloaks.class.getClassLoader().getResourceAsStream(resourcePath);

            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);

                // Add debug logging for static cloak
                SaturnClient.LOGGER.info("Static cloak dimensions: " + image.getWidth() + "x" + image.getHeight());

                String safeFileName = fileName.toLowerCase(Locale.ROOT)
                        .replace(' ', '_')
                        .replaceAll("[^a-z0-9/._-]", "");

                cloakCacheIdentifier = Identifier.of(SaturnClient.MOD_ID, "cloaks_" + safeFileName);
                IdentifierUtils.registerBufferedImageTexture(cloakCacheIdentifier, image);
            }
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Failed to load static cloak from resources: " + fileName, e);
        }
    }

    private static void loadAnimatedCloak(String uuid, String fileName) {
        try {
            String resourcePath = CLOAKS_RESOURCE_PATH + fileName;
            InputStream inputStream = Cloaks.class.getClassLoader().getResourceAsStream(resourcePath);

            if (inputStream != null) {
                // Read all bytes from input stream
                byte[] data = inputStream.readAllBytes();

                // Use our custom GifDecoder
                GifDecoder.GifImage gif = GifDecoder.read(data);

                List<AnimatedCloakData> frames = new ArrayList<>();
                int frameCount = gif.getFrameCount();

                for (int i = 0; i < frameCount; i++) {
                    BufferedImage frame = gif.getFrame(i);
                    // Convert from hundredths of a second to milliseconds (multiply by 10)
                    int delay = gif.getDelay(i) * 10;

                    String frameId = fileName.replace(".gif", "") + "_frame_" + i;
                    Identifier frameIdentifier = Identifier.of(SaturnClient.MOD_ID, "cloaks_" + frameId);
                    IdentifierUtils.registerBufferedImageTexture(frameIdentifier, frame);

                    frames.add(new AnimatedCloakData(frameIdentifier, delay));
                }

                animatedCloaks.put(uuid, frames);
                lastFrameTime.put(uuid, System.currentTimeMillis());

                SaturnClient.LOGGER.info("Loaded " + frames.size() + " frames for animated cloak: " + fileName);
            }
        } catch (IOException e) {
            SaturnClient.LOGGER.error("Failed to load animated cloak from resources: " + fileName, e);
        }
    }

    public static Identifier getCurrentCloakTexture(String uuid) {
        if (!SaturnSocket.players.containsKey(uuid)) {
            return null;
        }

        String cloakName = SaturnSocket.players.get(uuid).cloak;

        if (cloakName.isEmpty()) {
            return null;
        }

        if (Arrays.asList(ANIMATED_CLOAKS).contains(cloakName)) {
            List<AnimatedCloakData> frames = animatedCloaks.get(uuid);
            if (frames == null || frames.isEmpty()) {
                return null;
            }

            long currentTime = System.currentTimeMillis();
            long lastTime = lastFrameTime.getOrDefault(uuid, currentTime);
            int currentFrame = 0;

            long elapsedTime = currentTime - lastTime;
            long totalTime = 0;
            for (int i = 0; i < frames.size(); i++) {
                totalTime += frames.get(i).getDelayMs();
                if (elapsedTime < totalTime) {
                    currentFrame = i;
                    break;
                }
            }

            if (elapsedTime >= totalTime) {
                lastFrameTime.put(uuid, currentTime);
                currentFrame = 0;
            }

            return frames.get(currentFrame).getTextureId();
        } else {
            return Identifier.of(SaturnClient.MOD_ID, "textures/cloaks/" + cloakName + ".png");
        }
    }
}