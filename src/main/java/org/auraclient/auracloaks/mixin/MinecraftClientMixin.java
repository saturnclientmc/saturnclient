package org.auraclient.auracloaks.mixin;

import org.auraclient.auracloaks.AuraCloaks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        AuraCloaks.tick();
    }
}
