package org.saturnclient.saturnclient.mixin;

import java.util.Optional;
import java.util.function.Consumer;

import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Textures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {

    @Shadow
    private MinecraftClient client;
    @Shadow
    private ResourceReload reload;

    @Shadow(remap = false)
    private long reloadCompleteTime;

    @Shadow(remap = false)
    private long reloadStartTime;

    @Shadow(remap = false)
    private boolean reloading;

    @Shadow(remap = false)
    private Consumer<Optional<Throwable>> exceptionHandler;

    private static int withAlpha(int color, int alpha) {
        return color & 0xFFFFFF | alpha << 24;
    }

    @Inject(method = "init(Lnet/minecraft/client/texture/TextureManager;)V", at = @At("HEAD"))
    private static void onInit(TextureManager textureManager, CallbackInfo ci) {
        textureManager.registerTexture(Textures.LOGO_TEXT);
    }

    /**
     * @author SaturnClient
     * @reason Replace vanilla splash rendering completely but keep exit logic
     */
    @Overwrite
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        long currentTime = Util.getMeasuringTimeMs();

        // Handle reload start timing (vanilla uses 500ms fade-in)
        if (this.reloadStartTime == -1L) {
            this.reloadStartTime = currentTime;
        }

        // Compute fade-out timing
        float f = this.reloadCompleteTime > -1L ? (float) (currentTime - this.reloadCompleteTime) / 1000f : -1f;

        int alpha = f >= 0f
                ? MathHelper.ceil((1.0f - MathHelper.clamp(f - 1f, 0f, 1f)) * 255f)
                : 255;

        long l = Util.getMeasuringTimeMs();
        float g = this.reloadStartTime > -1L ? (float) (l - this.reloadStartTime) / 500.0F : -1.0F;

        int k;

        if (f >= 1.0F) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(context, 0, 0, delta);
            }

            k = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) *
                    255.0F);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, width, height,
                    withAlpha(0x28282B, k));
        } else if (this.reloading) {
            if (this.client.currentScreen != null && g < 1.0F) {
                this.client.currentScreen.render(context, mouseX, mouseY, delta);
            }

            k = MathHelper.ceil(MathHelper.clamp((double) g, 0.15, 1.0) * 255.0);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, width, height,
                    withAlpha(0x28282B, k));
        }

        RenderScope scope = new RenderScope(context);
        scope.setRenderLayer(RenderLayer::getGuiTextured);
        scope.drawTexture(Textures.LOGO, width / 2 - 49, height / 2 - 49, 0, 0, 98, 98, withAlpha(0xFFFFFF, alpha));

        if (f >= 2.0F) {
            this.client.setOverlay(null);
        }

        if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || g >= 2.0F)) {
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable var24) {
                this.exceptionHandler.accept(Optional.of(var24));
            }

            this.reloadCompleteTime = Util.getMeasuringTimeMs();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, context.getScaledWindowWidth(),
                        context.getScaledWindowHeight());
            }
        }
    }
}