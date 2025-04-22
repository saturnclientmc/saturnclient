package org.saturnclient.saturnclient.cosmetics.cloaks;

import net.minecraft.util.Identifier;
import org.saturnclient.saturnclient.auth.Auth;
import org.saturnclient.saturnclient.cosmetics.cloaks.utils.AnimatedCloakData;
import org.saturnclient.saturnclient.cosmetics.cloaks.utils.IdentifierUtils;
import org.saturnclient.saturnclient.auth.SaturnPlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.saturnclient.saturnclient.SaturnClient;

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
        Auth.setCloak(cloakName);
        setCloakSilent(uuid, cloakName);
        Auth.sendReload();
        SaturnClient.LOGGER.info("Cloak set to " + cloakName);
    }

    /**
     * Handles loading and caching of a new cloak texture, without connecting to the
     * server.
     * 
     * @param cloakName Name of the cloak file to load
     */
    public static void setCloakSilent(String uuid, String cloakName) {
        SaturnPlayer player = Auth.players.get(uuid);
        if (player == null) {
            Auth.players.put(uuid, new SaturnPlayer(cloakName, null));
        } else {
            player.cloak = cloakName;
        }

        loadCloak(uuid);
    }

    /**
     * Handles loading and caching of a new cloak texture, without connecting to the
     * server.
     * 
     * @param cloakName Name of the cloak file to load
     */
    public static void loadCloak(String uuid) {
        SaturnPlayer player = Auth.players.get(uuid);
        if (player != null && player.cloak != null) {
            if (!player.cloak.isEmpty()) {
                if (Arrays.asList(ANIMATED_CLOAKS).contains(player.cloak)) {
                    // Load animated cloak data in background thread
                    new Thread(() -> {
                        try {
                            String fileName = player.cloak + ".gif";
                            String resourcePath = CLOAKS_RESOURCE_PATH + fileName;
                            InputStream inputStream = Cloaks.class.getClassLoader().getResourceAsStream(resourcePath);

                            if (inputStream != null) {
                                byte[] data = inputStream.readAllBytes();
                                GifDecoder.GifImage gif = GifDecoder.read(data);
                                List<BufferedImage> frames = new ArrayList<>();
                                List<Integer> delays = new ArrayList<>();
                                int frameCount = gif.getFrameCount();

                                for (int i = 0; i < frameCount; i++) {
                                    frames.add(gif.getFrame(i));
                                    delays.add(gif.getDelay(i) * 10);
                                }

                                // Register textures on main thread
                                SaturnClient.client.execute(() -> {
                                    try {
                                        List<AnimatedCloakData> animatedFrames = new ArrayList<>();
                                        for (int i = 0; i < frameCount; i++) {
                                            String frameId = fileName.replace(".gif", "") + "_frame_" + i;
                                            Identifier frameIdentifier = Identifier.of(SaturnClient.MOD_ID, "cloaks_" + frameId);
                                            IdentifierUtils.registerBufferedImageTexture(frameIdentifier, frames.get(i));
                                            animatedFrames.add(new AnimatedCloakData(frameIdentifier, delays.get(i)));
                                        }
                                        animatedCloaks.put(uuid, animatedFrames);
                                        lastFrameTime.put(uuid, System.currentTimeMillis());
                                        SaturnClient.LOGGER.info("Loaded " + frames.size() + " frames for animated cloak: " + fileName);
                                    } catch (Exception e) {
                                        SaturnClient.LOGGER.error("Failed to register animated cloak textures: " + fileName, e);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            SaturnClient.LOGGER.error("Failed to load animated cloak from resources: " + player.cloak + ".gif", e);
                        }
                    }).start();
                } else {
                    SaturnClient.client.execute(() -> loadStaticCloak(player.cloak + ".png"));
                }
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

    public static Identifier getCurrentCloakTexture(String uuid) {
        if (!Auth.players.containsKey(uuid)) {
            return null;
        }

        String cloakName = Auth.players.get(uuid).cloak;

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