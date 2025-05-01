package org.saturnclient.ui2;

import java.util.ArrayList;
import java.util.List;

import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.anim.Animation;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.util.Pool;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SaturnScreen extends Screen {
    private final Pool pool = new Pool(3);
    protected List<Element> elements = new ArrayList<>();
    public int blurDuration = 700;
    public float blurProgress = 0.0f;

    public SaturnScreen(String title) {
        super(Text.literal(title));
        ThemeManager.load();
    }

    public void draw(Element element) {
        synchronized (elements) {
            elements.add(element);
        }
        
        if (element.animation != null) {
            element.animation.init(element);

            try {
                Thread.sleep(25);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

        try {
            Thread.sleep(25);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Animation.execute((Float progress) -> {
            blurProgress = progress;
        }, blurDuration);
    }

    public void ui() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        mouseX *= 2;
        mouseY *= 2;

        PostEffectProcessor postEffectProcessor = this.client.getShaderLoader().loadPostEffect(
                    Identifier.ofVanilla("blur"),
                    DefaultFramebufferSet.MAIN_ONLY);
            if (postEffectProcessor != null) {
                postEffectProcessor.setUniforms("Radius", 20 * blurProgress);
                postEffectProcessor.render(this.client.getFramebuffer(), this.pool);
            }

        // We are using a Abstracted RenderScope because older minecraft versions don't
        // use DrawContext
        RenderScope renderScope = new RenderScope(context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        renderScope.matrices.push();

        renderScope.matrices.scale(0.5f, 0.5f, 1.0f);

        for (Element element : elements) {
            renderScope.matrices.push();
            renderScope.setOpacity(element.opacity);
            renderScope.matrices.translate(element.x, element.y, 0);
            element.render(renderScope, new RenderContext(mouseX, mouseY, element));
            renderScope.matrices.pop();
            renderScope.setRenderLayer(null);
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

                if (Utils.isHovering(adjustedMouseX, adjustedMouseY, element.width, element.height)) {
                    element.focused = true;
                    element.click(adjustedMouseX, adjustedMouseY);
                }
            }
        }

        return false;
    }
}
