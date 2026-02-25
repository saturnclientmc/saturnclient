package org.saturnclient.ui2.screens;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.client.ServiceClient;
import org.saturnclient.saturnclient.config.AnimationConfig;
import org.saturnclient.saturnclient.config.Config;
import org.saturnclient.saturnclient.cosmetics.Emotes;
import org.saturnclient.ui2.SaturnScreen;
import org.saturnclient.ui2.Utils;
import org.saturnclient.ui2.anim.Fade;
import org.saturnclient.ui2.anim.SlideY;
import org.saturnclient.ui2.elements.AnimationStagger;
import org.saturnclient.ui2.elements.ImageTexture;
import org.saturnclient.ui2.elements.TextureButton;
import org.saturnclient.ui2.resources.Textures;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.util.Identifier;

public class EmoteWheel extends SaturnScreen {
    int page = 1;

    public EmoteWheel() {
        super("Emote Wheel");
    }

    @Override
    public void ui() {
        int row = 0;
        int col = 0;

        // =============================
        // Logo
        // =============================

        draw(new ImageTexture(Textures.LOGO_TEXT)
                .dimensions(98, 10)
                .centerOffset(width, height, 0, -105)
                .animation(new Fade(AnimationConfig.logo.duration.value)));

        draw(new ImageTexture(Config.getLogo())
                .dimensions(98, 98)
                .centerOffset(width, height, 0, -149)
                .animation(new SlideY(AnimationConfig.logo, -20)));

        // =============================
        // Navigation Buttons
        // =============================

        draw(new TextureButton(Textures.LEFT, () -> {
            if (page > 1) {
                page--;
                elements.clear();
                ui();
            }
        }).dimensions(30, 30)
                .centerOffset(width, height, -50, 105)
                .animation(new Fade(500)));

        draw(new TextureButton(Textures.RIGHT, () -> {
            if ((page * 8) < Emotes.availableEmotes.size()) {
                page++;
                elements.clear();
                ui();
            }
        }).dimensions(30, 30)
                .centerOffset(width, height, 50, 105)
                .animation(new Fade(500)));

        // =============================
        // Emote Grid (Staggered)
        // =============================

        AnimationStagger emoteStagger = new AnimationStagger(AnimationConfig.emoteWheel);

        while (row < 3) {

            int idx = (row * 3 + col) + ((page - 1) * 8);

            if (row == 1 && col == 1) {
                col++;
            }

            String emote = Utils.getOrNull(Emotes.availableEmotes, idx);

            emoteStagger.draw(
                    new TextureButton(Textures.getEmotePreview(emote), () -> {

                        if (emote == null)
                            return;

                        AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(
                                SaturnClient.client.player);

                        if (animationStack.isActive()
                                && animationStack.getPriority() == 1000) {
                            animationStack.removeLayer(1000);
                        }

                        animationStack.addAnimLayer(
                                1000,
                                PlayerAnimationRegistry
                                        .getAnimation(
                                                Identifier.of("saturnclient", emote))
                                        .playAnimation());

                        ServiceClient.emote(emote);
                        close();
                    })
                            .dimensions(70, 70)
                            .centerOffset(width, height,
                                    -80 + (col * 80),
                                    -35 + (row * 80))
                            .animation(new SlideY(AnimationConfig.emoteWheel, 12)));

            if (col < 2) {
                col++;
            } else {
                col = 0;
                row++;
            }
        }

        draw(emoteStagger);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
