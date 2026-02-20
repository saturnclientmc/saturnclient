package org.saturnclient.saturnclient.mixin;

import org.saturnclient.saturnclient.SaturnClientConfig;
import org.saturnclient.ui2.screens.TitleMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

@Mixin(MinecraftClient.class)
public class SetScreenMixin {

    private boolean replacingScreen = false;

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo ci) {
        if (replacingScreen)
            return;

        @SuppressWarnings("resource") // False positive bc MinecraftClient has close()
        MinecraftClient client = ((MinecraftClient) (Object) this);

        if (SaturnClientConfig.saturnTitleScreen.value
                && ((screen == null && client.world == null) || screen instanceof TitleScreen)) {
            replacingScreen = true;
            client.setScreen(new TitleMenu());
            ci.cancel();
            replacingScreen = false;
        }
    }
}