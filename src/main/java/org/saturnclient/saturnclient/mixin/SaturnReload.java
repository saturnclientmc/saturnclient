package org.saturnclient.saturnclient.mixin;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.auth.Auth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(ChatHud.class)
public class SaturnReload {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    public void onAddMessage(Text message, @Nullable MessageSignatureData signatureData,
            @Nullable MessageIndicator indicator, CallbackInfo ci) {
        String msg = message.toString();
        if (msg.contains("$SATURN_RELOAD")) {
            for (Map.Entry<String, String> player : Auth.playerNames.entrySet()) {
                String name = player.getKey();
                if (msg.contains(name)) {
                    Auth.player(name, player.getValue());
                }
                ci.cancel();
            }
        } else if (msg.contains("$SATURN_EMOTE")) {
            String[] args = msg.split("&@");
            for (AbstractClientPlayerEntity player : SaturnClient.client.world.getPlayers()) {
                if (player.getUuidAsString().replace("-", "").equals(args[1])) {
                    AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                    animationStack.addAnimLayer(1000, PlayerAnimationRegistry.getAnimation(Identifier.of("saturnclient", args[2])).playAnimation());
                }
            }
            ci.cancel();
        }
    }
}
