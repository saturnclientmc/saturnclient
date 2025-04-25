package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
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

        MutableText iconText = Text.literal(SaturnClientConfig.getSaturnIndicator())
            .styled(style -> style.withColor(SaturnClientConfig.getIconColor(uuid)));
        
        Text nameText = Text.literal(name).styled(style -> style.withColor(Formatting.WHITE));

        return Auth.players.containsKey(uuid) ? iconText.append(nameText) : Text.literal(name);
    }
}
