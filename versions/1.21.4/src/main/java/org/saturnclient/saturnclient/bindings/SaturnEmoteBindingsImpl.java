package org.saturnclient.saturnclient.bindings;

import java.util.UUID;

import org.saturnclient.common.minecraft.bindings.SaturnEmoteBindings;
import org.saturnclient.saturnclient.SaturnClient;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class SaturnEmoteBindingsImpl implements SaturnEmoteBindings {

    @Override
    public void setEmote(UUID fromPlayerUuid, String emoteIdOrNull) {
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
}
