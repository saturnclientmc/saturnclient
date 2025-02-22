package org.auraclient.auraclient;

import at.dhyan.open_imaging.GifDecoder;
import org.auraclient.auraclient.event.KeyInputHandler;
import org.auraclient.auraclient.utils.AnimatedCapeData;
import org.auraclient.auraclient.utils.IdentifierUtils;
import org.auraclient.auraclient.utils.RandomUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*
* Massive thank you to IIpho3nix for making this mod, i (leo) have modified it to fit Aura Client
*/

public class AuraClient implements ModInitializer {
    public static final String MOD_ID = "auraclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final File capesFolder = new File(MinecraftClient.getInstance().runDirectory, "Capes");
    public static final List<String> list = new ArrayList<>();
    private static final List<AnimatedCapeData> anicape = new ArrayList<>();
    private static int capeindex = 0;
    private static long mili = System.currentTimeMillis();
    private static String lastCache = "NotCached";
    public static Identifier capeCacheIdentifier = null;
    public static String cape = "";

    @Override
    public void onInitialize() {
        list.clear();
        list.add("");
        if (capesFolder.mkdir()) {
            LOGGER.info("New Cape Folder Created");
            downloadCloakTextures();
        } else {
            LOGGER.info("Cape Folder Already Exists");
        }
        loadCloakTextures();

        KeyInputHandler.register();
    }

    public void downloadCloakTextures() {
        // Todo: Download the cloak textures and put them in the Capes folder
    }

    private static void loadCloakTextures() {
        if (capesFolder.isDirectory()) {
            for (final File fileEntry : Objects.requireNonNull(capesFolder.listFiles())) {
                if (
                        fileEntry.getName().endsWith(".png") || fileEntry.getName().endsWith(".gif")
                ) {
                    list.add(fileEntry.getName());
                }
            }
        }
    }

    public static void tick() {
        if (!lastCache.equals(cape)) {
            if (!cape.isEmpty()) {
                if (cape.endsWith(".gif")) {
                    try {
                        anicape.clear();
                        Identifier frameIdentifier;
                        File file = new File(capesFolder, cape);
                        final FileInputStream data = new FileInputStream(file);
                        final GifDecoder.GifImage gif = GifDecoder.read(data);
                        final int frameCount = gif.getFrameCount();
                        for (int i = 0; i < frameCount; i++) {
                            final BufferedImage img = gif.getFrame(i);
                            final int delay = gif.getDelay(i);
                            frameIdentifier = Identifier.of(MOD_ID, "capes_" + RandomUtils.randomStringLowercase(10));
                            IdentifierUtils.registerBufferedImageTexture(frameIdentifier, img);
                            anicape.add(new AnimatedCapeData(frameIdentifier, delay));
                        }
                        capeindex = 0;
                        capeCacheIdentifier = anicape.get(0).getId();
                        lastCache = cape;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    BufferedImage img = null;
                    File file = new File(capesFolder, cape);
                    try {
                        img = ImageIO.read(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    capeCacheIdentifier = Identifier.of(MOD_ID, "capes_" + cape.toLowerCase(Locale.ROOT).replace(' ', '_').replaceAll("[^a-z0-9/._-]", ""));
                    IdentifierUtils.registerBufferedImageTexture(capeCacheIdentifier, img);
                    lastCache = cape;
                }
            } else {
                capeCacheIdentifier = null;
                lastCache = cape;
            }
        } else {
            if (cape.endsWith(".gif")) {
                if (capeindex > (anicape.size() - 1)) {
                    capeindex = 0;
                 }
                 if (System.currentTimeMillis() > mili + anicape.get(capeindex).getDelay()) {
                     mili = System.currentTimeMillis();
                    capeCacheIdentifier = anicape.get(capeindex).getId();
                    capeindex++;
                }
            }
        }
    }
}
