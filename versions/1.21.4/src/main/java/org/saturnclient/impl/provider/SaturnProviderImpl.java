package org.saturnclient.impl.provider;

import java.awt.image.BufferedImage;
import java.util.UUID;

import org.saturnclient.common.provider.SaturnProvider;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.game.MinecraftClientRef;
import org.saturnclient.cosmetics.cloak.utils.IdentifierUtils;
import org.saturnclient.saturnclient.SaturnClient;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class SaturnProviderImpl implements SaturnProvider {
    @Override
    public MinecraftClientRef getClient() {
        return (MinecraftClientRef) SaturnClient.client;
    }

    @Override
    public void playEmote(UUID fromPlayerUuid, String emoteIdOrNull) {
        if (SaturnClient.client == null || SaturnClient.client.world == null) {
            return;
        }

        for (AbstractClientPlayerEntity player : SaturnClient.client.world.getPlayers()) {
            if (!player.getUuid().equals(fromPlayerUuid)) {
                continue;
            }

            AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);

            if (emoteIdOrNull != null && !emoteIdOrNull.isEmpty()) {
                if (animationStack.isActive() && animationStack.getPriority() == 1000) {
                    animationStack.removeLayer(1000);
                }
                animationStack.addAnimLayer(1000,
                        PlayerAnimationRegistry
                                .getAnimation(Identifier.of("saturnclient", emoteIdOrNull))
                                .playAnimation());
            } else {
                animationStack.removeLayer(1000);
            }

            break;
        }
    }

    @Override
    public String getAccessToken() {
        var session = SaturnClient.client.getSession();
        if (session == null) {
            return null;
        }
        return session.getAccessToken();
    }

    @Override
    public UUID getUuid() {
        var session = SaturnClient.client.getSession();
        if (session == null) {
            return null;
        }
        return session.getUuidOrNull();
    }

    @Override
    public String getUsername() {
        var session = SaturnClient.client.getSession();
        if (session == null) {
            return null;
        }
        return session.getUsername();
    }

    @Override
    public void onClientStopping(Runnable handler) {
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> handler.run());
    }

    @Override
    public void logInfo(String message) {
        SaturnClient.LOGGER.info(message);
    }

    @Override
    public void logError(String message) {
        SaturnClient.LOGGER.error(message);
    }

    @Override
    public void logError(String message, Throwable throwable) {
        SaturnClient.LOGGER.error(message, throwable);
    }

    @Override
    public void stop() {
        SaturnClient.client.scheduleStop();
    }

    @Override
    public void registerBufferedImageTexture(IdentifierRef i, BufferedImage bi) {
        IdentifierUtils.registerBufferedImageTextureFast((Identifier) (Object) i, bi);
    }
}
