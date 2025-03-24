package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
        String name = (displayName != null)
            ? displayName.getString()
            : ((PlayerListEntry) (Object) this).getProfile().getName();
        return Text.literal(
            SaturnSocket.playerNames.containsKey(name) ? "" + name : name
        );
    }
}
