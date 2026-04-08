package org.saturnclient.impl.ui;

import java.time.Duration;
import java.time.Instant;

import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.SaturnScreen.ScreenProvider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
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

    public final SaturnScreen screen;

    public static void preload(MinecraftClient client) {
        TextureManager textureManager = client.getTextureManager();
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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (screen.start == null) {
            screen.start = Instant.now();
        }
        long elapsed = Duration.between(screen.start, Instant.now()).toMillis();

        mouseX *= 2;
        mouseY *= 2;

        if (client.world == null && client.getCurrentServerEntry() == null) {
            // ROTATING_PANORAMA_RENDERER.render(context, this.width, this.height, true);
        }

        RenderScope renderScope = new RenderScopeImpl(context);

        screen.render(renderScope, mouseX, mouseY, delta, elapsed);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (screen.mouseClicked(click.x(), click.y(), click.button())) {
            return true;
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (screen.mouseDragged(click.x(), click.y(), click.button(), 0.0, 0.0)) {
            return true;
        }

        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (screen.mouseReleased(click.x(), click.y(), click.button())) {
            return true;
        }

        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (screen.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (screen.keyPressed(input.getKeycode(), input.scancode(), input.modifiers())) {
            return true;
        }

        return super.keyPressed(input);
    }

    // @Override
    // public void resize(MinecraftClient client, int width, int height) {
    //     screen.resize(width, height);
    //     super.resize(client, width, height);
    // }

    @Override
    public void close() {
        this.screen.onClose();
        super.close();
    }

    @Override
    public int getWidth() {
        return width * 2;
    }

    @Override
    public int getHeight() {
        return height * 2;
    }
}