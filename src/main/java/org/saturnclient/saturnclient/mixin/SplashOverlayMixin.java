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

    @Shadow(remap = false)
    private float progress;

    private static int withAlpha(int color, int alpha) {
        return color & 0xFFFFFF | alpha << 24;
    }

    @Inject(method = "init(Lnet/minecraft/client/texture/TextureManager;)V", at = @At("HEAD"))
    private static void onInit(TextureManager textureManager, CallbackInfo ci) {
        textureManager.registerTexture(Textures.LOGO_TEXT);
    }

    /**
     * @reason Replace vanilla splash rendering completely but keep exit logic
     */
    @Overwrite
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        long currentTime = Util.getMeasuringTimeMs();

        // Initialize reload start time if not set
        if (reloadStartTime == -1L) {
            reloadStartTime = currentTime;
        }

        // Compute fade-out progress after reload completes
        float fadeOutProgress = reloadCompleteTime > -1L
                ? (float) (currentTime - reloadCompleteTime) / 1000f
                : -1f;

        int logoAlpha = fadeOutProgress >= 0f
                ? MathHelper.ceil((1.0f - MathHelper.clamp(fadeOutProgress - 1f, 0f, 1f)) * 255f)
                : 255;

        // Compute fade-in progress while reloading
        float reloadProgress = reloadStartTime > -1L
                ? (float) (currentTime - reloadStartTime) / 500f
                : -1f;

        // Draw background overlay
        if (fadeOutProgress >= 1.0F) {
            renderCurrentScreen(context, mouseX, mouseY, delta);
            int overlayAlpha = computeOverlayAlpha(fadeOutProgress - 1f);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, screenWidth, screenHeight,
                    withAlpha(0x28282B, overlayAlpha));
        } else if (reloading) {
            if (reloadProgress < 1.0F) {
                renderCurrentScreen(context, mouseX, mouseY, delta);
            }
            int overlayAlpha = MathHelper.ceil(MathHelper.clamp(reloadProgress, 0.15, 1.0) * 255.0);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, screenWidth, screenHeight,
                    withAlpha(0x28282B, overlayAlpha));
        }

        float u = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95F + u * 0.050000012F, 0.0F, 1.0F);

        // Get render scope for better UI
        RenderScope scope = new RenderScope(context);

        // Draw central logo
        drawLogo(scope, screenWidth, screenHeight, logoAlpha);
        renderProgressBar(scope, context, screenWidth, screenHeight, logoAlpha);

        // Remove overlay after fade-out completes
        if (fadeOutProgress >= 2.0F) {
            client.setOverlay(null);
        }

        // Handle reload completion and exceptions
        handleReloadCompletion(context);
    }

    private void renderCurrentScreen(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client.currentScreen != null) {
            client.currentScreen.render(context, mouseX, mouseY, delta);
        }
    }

    /** Draw the main logo centered */
    private void drawLogo(RenderScope scope, int screenWidth, int screenHeight, int alpha) {
        scope.setRenderLayer(RenderLayer::getGuiTextured);
        scope.drawTexture(Textures.LOGO, (screenWidth - 60) / 2, ((screenHeight - 60) / 2) - 20, 0, 0, 60, 60,
                withAlpha(0xFFFFFF, alpha));
    }

    private void renderProgressBar(RenderScope scope, DrawContext context, int screenWidth, int screenHeight,
            int alpha) {
        int progressY = ((screenHeight - 5) / 2) + 25;
        context.drawBorder((screenWidth - 120) / 2, progressY, 120, 5, withAlpha(0xFFFFFF, alpha));

        int progressWidth = Math.min((int) (progress * 120), 120);
        int progressX = (screenWidth - 120) / 2;

        context.fill(progressX, progressY, progressX + progressWidth, progressY + 5, withAlpha(0xFFFFFF, alpha));
    }

    /** Compute overlay alpha from fade progress */
    private int computeOverlayAlpha(float progress) {
        return MathHelper.ceil((1.0F - MathHelper.clamp(progress, 0.0F, 1.0F)) * 255.0F);
    }

    /** Handle reload completion logic */
    private void handleReloadCompletion(DrawContext context) {
        float reloadProgress = reloadStartTime > -1L ? (float) (Util.getMeasuringTimeMs() - reloadStartTime) / 500f
                : -1f;

        if (reloadCompleteTime == -1L && reload.isComplete() && (!reloading || reloadProgress >= 2.0F)) {
            try {
                reload.throwException();
                exceptionHandler.accept(Optional.empty());
            } catch (Throwable ex) {
                exceptionHandler.accept(Optional.of(ex));
            }

            reloadCompleteTime = Util.getMeasuringTimeMs();

            if (client.currentScreen != null) {
                client.currentScreen.init(client, context.getScaledWindowWidth(), context.getScaledWindowHeight());
            }
        }
    }
}