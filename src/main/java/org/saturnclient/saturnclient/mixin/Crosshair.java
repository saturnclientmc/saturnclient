/*
 * Code from: https://github.com/Selyss
 * Repo: https://github.com/Selyss/CrosshairIndicator/tree/main
*/

package org.saturnclient.saturnclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.DrawContext;

@Mixin(InGameHud.class)
public class Crosshair {
    @Unique
    Identifier textureLocation = Identifier.of("saturnclient", "textures/gui/hud/crosshair_range.png");

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void drawCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (org.saturnclient.modules.mods.Crosshair.enabled.prop.value
                && org.saturnclient.modules.mods.Crosshair.range_indicator.prop.value
                && SaturnClient.client.targetedEntity != null
                && SaturnClient.client.targetedEntity.isAlive()) {
            RenderSystem.setShaderTexture(0, textureLocation);
            int scaledWidth = 15;
            int scaledHeight = 15;

            context.drawTexture(RenderLayer::getCrosshair, textureLocation,
                    (context.getScaledWindowWidth() - scaledWidth) / 2,
                    (context.getScaledWindowHeight() - scaledHeight) / 2, 0.0F, 0.0F, scaledWidth, scaledHeight,
                    scaledWidth, scaledHeight);
        }
    }
}