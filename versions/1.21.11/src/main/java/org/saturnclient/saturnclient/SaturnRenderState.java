package org.saturnclient.saturnclient;

import org.jspecify.annotations.NonNull;
import org.saturnclient.client.player.SaturnPlayer;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class SaturnRenderState {
    @NonNull
    public static RenderStateDataKey<SaturnPlayer> saturnDataKey = RenderStateDataKey
            .create(() -> "saturn-client-player");
}
