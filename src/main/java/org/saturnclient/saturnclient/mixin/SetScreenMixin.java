package org.saturnclient.saturnclient.mixin;

import org.saturnclient.saturnclient.config.SaturnClientConfig;
import org.saturnclient.ui2.screens.TitleMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

@Mixin(MinecraftClient.class)
public class SetScreenMixin {
    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private Screen replaceTitleScreen(Screen screen) {
        if (SaturnClientConfig.saturnTitleScreen.value) {
            if (screen instanceof TitleScreen)
                return new TitleMenu();
            if (screen == null && ((MinecraftClient) (Object) this).world == null)
                return new TitleMenu();
        }

        return screen;
    }
}