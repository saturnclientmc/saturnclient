package org.saturnclient.ui;

import java.time.Duration;
import java.time.Instant;

import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui.SaturnScreen.ScreenProvider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Pool;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SaturnScreenFabric extends Screen implements ScreenProvider {
    protected static final Identifier PANORAMA = Identifier.of("saturnclient",
            "textures/gui/title/background/panorama");
    protected static final CubeMapRenderer PANORAMA_RENDERER = new CubeMapRenderer(PANORAMA);

    public static final RotatingCubeMapRenderer ROTATING_PANORAMA_RENDERER;

    static {
        ROTATING_PANORAMA_RENDERER = new RotatingCubeMapRenderer(PANORAMA_RENDERER);
    }

    private final Pool pool = new Pool(3);
    private final SaturnScreen screen;

    public static void preload(MinecraftClient client) {
        TextureManager textureManager = client.getTextureManager();

        // Force upload each texture to GPU
        for (int i = 0; i < 6; i++) {
            String var10001 = PANORAMA.getPath();
            Identifier tex = PANORAMA.withPath(var10001 + "_" + i + ".png");
            ResourceTexture resourceTexture = new ResourceTexture(tex);
            textureManager.registerTexture(tex, resourceTexture);
        }
    }

    public SaturnScreenFabric(SaturnScreen screen) {
        super(Text.literal(screen.title));
        screen.provider = this;
        this.screen = screen;
    }

    @Override
    protected void init() {
        screen.init();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (screen.start == null) {
            screen.start = Instant.now();
        }
        long elapsed = Duration.between(screen.start, Instant.now()).toMillis();

        mouseX *= 2;
        mouseY *= 2;

        if (client.world == null && client.getCurrentServerEntry() == null) {
            ROTATING_PANORAMA_RENDERER.render(context, this.width, this.height, screen.backgroundOpacity, delta);
        }

        PostEffectProcessor postEffectProcessor = this.client.getShaderLoader().loadPostEffect(
                Identifier.ofVanilla("blur"),
                DefaultFramebufferSet.MAIN_ONLY);

        if (postEffectProcessor != null) {
            postEffectProcessor.setUniforms("Radius", screen.backgroundBlur * Math.min((float) elapsed / 700, 1.0f));
            postEffectProcessor.render(this.client.getFramebuffer(), this.pool);
        }

        // We are using a Abstracted RenderScope because older minecraft versions don't
        // use DrawContext
        RenderScope renderScope = new RenderScopeImpl(context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        screen.render(renderScope, mouseX, mouseY, delta, elapsed);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return screen.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return screen.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return screen.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
        return screen.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return screen.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        screen.resize(width, height);
    }

    // ------------------------------------------
    // ScreenProvider implementations
    // ------------------------------------------

    @Override
    public void close() {
        this.screen.onClose();
        super.close();
    }
}
