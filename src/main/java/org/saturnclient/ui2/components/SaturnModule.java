package org.saturnclient.ui2.components;

import org.saturnclient.modules.Module;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui2.Element;
import org.saturnclient.ui2.RenderContext;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.render.RenderLayer;

public class SaturnModule extends Element {
    private Module mod;

    public SaturnModule(Module mod) {
        this.mod = mod;

        this.width = 270;

        this.height = 120;
    }

    @Override
    public void render(RenderScope renderScope, RenderContext ctx) {
        renderScope.drawRoundedRectangle(0, 0, width, height, 10, -16777216);

        if (mod.isEnabled()) {
            renderScope.enableScissor(0, 0, 7, height);
            renderScope.drawRoundedRectangle(0, 0, 7 * 2, height, 10, -7643914);
            renderScope.disableScissor();
        }

        renderScope.drawText(mod.getName(), 10 + 7, 10, true, -1);

        renderScope.matrices.push();
        renderScope.matrices.translate(10 + 7, 30, 0);
        renderScope.matrices.scale(0.7f, 0.7f, 1.0f);
        renderScope.drawText(formatText(mod.getDescription(), width - 7 - 10), 0, 0, false, 0xFFAAAAAA);
        renderScope.matrices.pop();

        if (ctx.isHovering(10 + 7, height - 10 - 10, 10, 10)) {

        }

        renderScope.setRenderLayer(RenderLayer::getGuiTextured);
        renderScope.drawTexture(Textures.SETTINGS, 10 + 7, height - 10 - 10, 0, 0, 10, 10);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        mod.setEnabled(!mod.isEnabled());
    }

    private static String formatText(String text, int width) {
        width -= Fonts.getWidth("..", Fonts.INTER);

        StringBuilder trimmedText = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            String current = trimmedText.toString() + text.charAt(i);
            double currentWidth = Fonts.getWidth(current, Fonts.INTER) * 0.7;
            
            if (currentWidth > width) {
                trimmedText.append("..");
                break;
            }
            
            trimmedText.append(text.charAt(i));
        }
        
        return trimmedText.toString();
    }    
}
