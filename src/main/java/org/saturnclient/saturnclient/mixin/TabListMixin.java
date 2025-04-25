package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.saturnclient.auth.Auth;
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

        return Auth.players.containsKey(uuid) ? Text.literal(SaturnClientConfig.getSaturnIndicator()).formatted(getIconColor(name)).append(Text.literal(name).withColor(-1)) : Text.literal(name);
    }

    /*
     * Gets the icon color of a specific individual, here are the different colors
     * 
     * - Owner: Dark Red
     * - Admin: Red
     * - Partners: Gold
     * - Contributor: Aqua
     * - Other/player: White
    */
    private Formatting getIconColor(String name) {
        if (name == "HexLeo") {
            return Formatting.DARK_RED; // Owner
        } else {
            return Formatting.WHITE;
        }
    }
}
