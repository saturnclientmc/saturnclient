package org.saturnclient.common.minecraft.bindings;

import java.util.UUID;

public interface SaturnEmoteBindings {
    void setEmote(UUID fromPlayerUuid, String emoteIdOrNull);
}
