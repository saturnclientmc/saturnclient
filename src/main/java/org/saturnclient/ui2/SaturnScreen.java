package org.saturnclient.ui2;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.components.ElementRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.util.Pool;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class SaturnScreen extends Screen {
    private final Pool pool = new Pool(3);
    protected List<Element> elements = new ArrayList<>();
    protected float backgroundOpacity = 1.0f;
    protected Instant start = null;
    public int blurDuration = 700;
    public float blurProgress = 0.0f;
    public int backgroundBlur = 10;

    protected static final CubeMapRenderer PANORAMA_RENDERER = new CubeMapRenderer(
            Identifier.of("saturnclient", "textures/gui/title/background/panorama"));

    public static final RotatingCubeMapRenderer ROTATING_PANORAMA_RENDERER;

    static {
        ROTATING_PANORAMA_RENDERER = new RotatingCubeMapRenderer(PANORAMA_RENDERER);
    }

    public SaturnScreen(String title) {
        super(Text.literal(title));
    }

    public void draw(Element element) {
        ElementRenderer.draw(elements, element);
    }

    @Override
    protected void init() {
        elements.clear();
        width *= 2;
        height *= 2;
        ui(); // abstraction to render the saturn ui and also render extra stuff here
    }

    public abstract void ui();

    @SuppressWarnings("deprecation")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (start == null) {
            start = Instant.now();
        }
        long elapsed = Duration.between(start, Instant.now()).toMillis();

        mouseX *= 2;
        mouseY *= 2;

        if (client.world == null && client.getCurrentServerEntry() == null) {
            ROTATING_PANORAMA_RENDERER.render(context, this.width, this.height, backgroundOpacity, delta);
        }

        PostEffectProcessor postEffectProcessor = this.client.getShaderLoader().loadPostEffect(
                Identifier.ofVanilla("blur"),
                DefaultFramebufferSet.MAIN_ONLY);
        if (postEffectProcessor != null) {
            postEffectProcessor.setUniforms("Radius", backgroundBlur * blurProgress);
            postEffectProcessor.render(this.client.getFramebuffer(), this.pool);
        }

        // We are using a Abstracted RenderScope because older minecraft versions don't
        // use DrawContext
        RenderScope renderScope = new RenderScope(context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        renderScope.matrices.push();

        renderScope.matrices.scale(0.5f, 0.5f, 0.5f);

        ElementRenderer.render(new ArrayList<>(elements), elapsed, renderScope, mouseX, mouseY);

        renderScope.matrices.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.mouseClicked(elements, mouseX, mouseY, button);

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.mouseDragged(elements, mouseX, mouseY, button, deltaX, deltaY);

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.mouseReleased(elements, mouseX, mouseY, button);

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
        mouseX *= 2;
        mouseY *= 2;

        ElementRenderer.mouseScrolled(elements, mouseX, mouseY, horizontalAmount, verticalAmount);

        return super.mouseScrolled(
                mouseX,
                mouseY,
                horizontalAmount,
                verticalAmount);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        elements.clear();
        super.resize(client, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        ElementRenderer.keyPressed(elements, keyCode, scanCode, modifiers);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
