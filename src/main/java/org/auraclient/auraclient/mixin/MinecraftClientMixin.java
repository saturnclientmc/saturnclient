package org.auraclient.auraclient.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.auraclient.auraclient.cloaks.Cloaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for the MinecraftClient class.
 * Handles the periodic updating of cloaks system.
 */
@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public class MinecraftClientMixin {
    
    /**
     * Injects into the client tick to update cloaks.
     * @param ci Callback info
     */
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    private void tick(CallbackInfo ci) {
        Cloaks.tick();
    }
}
