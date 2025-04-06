package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

@Mixin(PlayerListEntry.class)
public abstract class TabListMixin {

    @Shadow
    private GameProfile profile;

    /**
     * @author HexLeo
     * @reason Adds the Saturn Client icon to the player's name if they are online
     *         with Saturn Client
     */
    @Overwrite
    @Nullable
    public Text getDisplayName() {
        GameProfile profile = ((PlayerListEntry) (Object) this).getProfile();
        String name = profile.getName();
        String uuid = profile.getId().toString().replaceAll("-", "");

        return Text.literal(
                SaturnSocket.players.containsKey(uuid) ? SaturnClientConfig.getSaturnIndicator() + name : name);
    }
}
