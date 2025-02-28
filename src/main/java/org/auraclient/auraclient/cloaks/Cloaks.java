package org.auraclient.auraclient.cloaks;

import net.minecraft.util.Identifier;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.auth.AuraApi;
import org.auraclient.auraclient.cloaks.utils.IdentifierUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Manages the cloak/cape system for Aura Client.
 * Handles loading and caching of player capes.
 */
public class Cloaks {
    private static final String CAPES_RESOURCE_PATH = "assets/auraclient/textures/capes/";
    public static final List<String> availableCloaks = new ArrayList<>();
    public static Identifier capeCacheIdentifier = null;
    public static final Map<String, String> playerCapes = new HashMap<>();

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
            loadStaticCape(capeName);
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
}