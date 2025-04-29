package org.saturnclient.ui2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.saturnclient.saturnclient.config.ThemeManager;
import org.saturnclient.saturnclient.mixin.DrawContextAccessor;
import org.saturnclient.ui2.anim.Curve;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SaturnScreen extends Screen {
    protected List<Element> elements = new ArrayList<>();

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

            new Thread(() -> {
                long startTime = System.currentTimeMillis();

                Function<Double, Double> curveFunction = Curve::easeInOutCubic;
                float progress = 0.0f;
                double incremental = (1000 - element.animation.duration) / 10000.0;

                System.out.println("Starting animation...");
                // System.out.printf("Increment per step: %.6f%n", incremental);

                for (int i = 0; i <= 100; i++) {
                    float newProgress = (float) (double) curveFunction.apply(i / 100.0);

                    while (Math.abs(newProgress - progress) > 1e-6) {
                        if (progress < newProgress) {
                            progress += incremental;
                            if (progress > newProgress) progress = newProgress;
                        } else {
                            progress -= incremental;
                            if (progress < newProgress) progress = newProgress;
                        }

                        element.animation.tick(progress, element);

                        try {
                            Thread.sleep(element.animation.duration / 100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }            
                    }
                }

                System.out.println("Animation complete. " + (System.currentTimeMillis() - startTime) + "MS");
            }).start();
        }
    }

    @Override
    protected void init() {
        ui(); // abstraction to render the saturn ui and also render extra stuff here
    }

    public void ui() {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // We are using a Abstracted RenderScope because older minecraft versions don't
        // use DrawContext
        RenderScope renderScope = new RenderScope(context.getMatrices(),
                ((DrawContextAccessor) context).getVertexConsumers());

        renderScope.matrices.push();

        for (Element element : elements) {
            renderScope.setOpacity(element.opacity);
            renderScope.matrices.push();
            renderScope.matrices.translate(element.x, element.y, 0);
            element.render(renderScope, new RenderContext(mouseX, mouseY, element));
            renderScope.matrices.pop();
            renderScope.setRenderLayer(null);
        }

        renderScope.matrices.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
