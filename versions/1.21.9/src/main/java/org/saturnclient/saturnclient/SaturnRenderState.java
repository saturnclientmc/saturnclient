package org.saturnclient.saturnclient;

import org.saturnclient.client.player.SaturnPlayer;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class SaturnRenderState {
    public static RenderStateDataKey<SaturnPlayer> saturnDataKey = RenderStateDataKey
            .create(() -> "saturn-client-player");
}
