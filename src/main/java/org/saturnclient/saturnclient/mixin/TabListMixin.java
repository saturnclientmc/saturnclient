package org.saturnclient.saturnclient.mixin;

import org.saturnclient.saturnclient.auth.SaturnApi;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

@Mixin(PlayerListEntry.class)
public abstract class TabListMixin {
    @Shadow
    private Text displayName;

    /**
     * @author HexLeo
     * @reason Adds the Saturn Client icon to the player's name if they are online
     *         with Saturn Client
     */
    @Overwrite
    @Nullable
    public Text getDisplayName() {
        String name = displayName.getString();
        return Text.literal(SaturnApi.playerNames.containsKey(name) ? "" + name : name);
    }
}
