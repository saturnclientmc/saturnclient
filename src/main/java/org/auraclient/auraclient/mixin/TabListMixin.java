package org.auraclient.auraclient.mixin;

import org.auraclient.auraclient.auth.AuraApi;
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

    @Overwrite
    @Nullable
    public Text getDisplayName() {
        String name = displayName.getString();
        return Text.literal(AuraApi.playerNames.containsKey(name) ? "" + name : name);
    }
}
