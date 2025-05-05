package org.saturnclient.ui2;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.anim.Animation;
import org.saturnclient.ui2.components.ElementRenderer;

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
        synchronized (elements) {
            elements.add(element);

            if (element instanceof ElementRenderer) {
                elements.addAll(((ElementRenderer) element).getChildren());
            }
        }
        
        if (element.animation != null) {
            element.animation.init(element);

            Animation.execute((Float progress) -> {
                element.animation.tick(progress, element);
            }, element.animation.duration);
        }
    }

    @Override
    protected void init() {
        width *= 2;
        height *= 2;
        ui(); // abstraction to render the saturn ui and also render extra stuff here
        ThemeManager.load();

        Animation.execute((Float progress) -> {
            blurProgress = progress;
        }, blurDuration);
    }

    public abstract void ui();

    @SuppressWarnings("deprecation")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        mouseX *= 2;
        mouseY *= 2;

        if (client.world == null && client.getCurrentServerEntry() == null) {
            ROTATING_PANORAMA_RENDERER.render(context, this.width, this.height, 1.0F, delta);
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

        for (Element element : elements) {
            renderScope.enableScissor(element.x, element.y, element.x + element.width, element.y + element.height);

            renderScope.matrices.push();
            renderScope.setOpacity(element.opacity);
            renderScope.matrices.translate(element.x, element.y, 0);
            renderScope.matrices.scale(element.scale, element.scale, 1.0f);
            element.render(renderScope, new RenderContext(mouseX, mouseY, element));
            renderScope.matrices.pop();
            renderScope.setRenderLayer(null);

            renderScope.disableScissor();
        }

        renderScope.matrices.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX *= 2;
        mouseY *= 2;
        if (button == 0) {
            for (Element element : new ArrayList<>(elements)) {
                element.focused = false;

                int adjustedMouseX = (int) mouseX - element.x;
                int adjustedMouseY = (int) mouseY - element.y;

                if (Utils.isHovering(adjustedMouseX, adjustedMouseY, element.width, element.height, element.scale)) {
                    element.focused = true;
                    element.click(adjustedMouseX, adjustedMouseY);
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount) {
        for (Element element : new ArrayList<>(elements)) {
            double adjustedMouseX = mouseX - element.x;
            double adjustedMouseY = mouseY - element.y;
            boolean isMouseInside = adjustedMouseX >= 0 &&
                    adjustedMouseX <= (element.width * element.scale) &&
                    adjustedMouseY >= 0 &&
                    adjustedMouseY <= (element.height * element.scale);

            if (isMouseInside) {
                element.scroll(
                        (int) (adjustedMouseX / element.scale),
                        (int) (adjustedMouseY / element.scale),
                        horizontalAmount,
                        verticalAmount);
            }
        }

        return super.mouseScrolled(
                mouseX,
                mouseY,
                horizontalAmount,
                verticalAmount);
    }
}
