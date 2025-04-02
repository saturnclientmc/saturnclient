package org.saturnclient.saturnclient.mixin;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;

@Mixin(ChatHud.class)
public class SaturnReload {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    public void onAddMessage(Text message, @Nullable MessageSignatureData signatureData,
            @Nullable MessageIndicator indicator, CallbackInfo ci) {
        String msg = message.toString();
        if (msg.contains("$SATURN_RELOAD")) {
            for (Map.Entry<String, String> player : SaturnSocket.playerNames.entrySet()) {
                String name = player.getKey();
                if (msg.contains(name)) {
                    SaturnSocket.player(name, player.getValue());
                }
                ci.cancel();
            }
        }
    }
}
