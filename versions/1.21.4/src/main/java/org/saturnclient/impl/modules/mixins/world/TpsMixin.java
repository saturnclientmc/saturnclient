package org.saturnclient.impl.modules.mixins.world;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import org.saturnclient.mod.mods.TpsMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Feeds world-time packets into {@link TpsMod}'s TPS estimator.
 *
 * This is the only place in the entire codebase that reads a network
 * packet and passes data to a feature. The feature's
 * {@link TpsMod#onTimePacket(long)} method handles all averaging
 * logic; the mixin is intentionally reduced to a single forwarding call.
 *
 * The only change from the original is renaming the import from
 * {@code feature.features.Tps} to {@link TpsMod}.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class TpsMixin {

    @Inject(method = "onWorldTimeUpdate", at = @At("HEAD"))
    private void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        TpsMod.onTimePacket(packet.time());
    }
}
