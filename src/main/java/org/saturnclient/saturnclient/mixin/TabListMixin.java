package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.client.player.Roles;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;
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
        UUID uuid = profile.getId();

        if (SaturnPlayer.get(uuid) != null) {
            return Text.literal(Roles.getSaturnIndicator())
                    .styled(style -> style.withColor(Roles.getIconColor(uuid)))
                    .append(Text.literal(name).styled(style -> style.withColor(Formatting.WHITE)));
        }
        return Text.literal(name);
    }
}
