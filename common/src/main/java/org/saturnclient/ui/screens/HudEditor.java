package org.saturnclient.ui.screens;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.ModManager;
import org.saturnclient.modules.Module;
import org.saturnclient.common.minecraft.render.IMatrixStack;
import org.saturnclient.config.manager.ConfigManager;
import org.saturnclient.ui.RenderScope;
import org.saturnclient.ui.SaturnScreen;

import java.util.ArrayList;
import java.util.List;

public class HudEditor extends SaturnScreen {
    private final List<HudMod> hudMods = new ArrayList<>();
    private HudMod draggingMod = null;
    private boolean resizing = false;
    private int offsetX, offsetY;
    private static final int RESIZE_MARGIN = 3;

    public HudEditor() {
        super("HUD Editor");
        for (Module m : ModManager.ENABLED_MODS) {
            if (m.isEnabled() && m instanceof HudMod) {
                hudMods.add((HudMod) m);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int m = hudMods.size() - 1; m >= 0; m--) {
                HudMod mod = hudMods.get(m);
                ModDimensions dim = mod.getDimensions();

                // Check if clicking the corner for resizing
                if (isCorner(mouseX, mouseY, dim)) {
                    draggingMod = mod;
                    resizing = true;
                    offsetX = (int) (mouseX - (dim.x.value + dim.width * dim.scale.value));
                    offsetY = (int) (mouseY - (dim.y.value + dim.height * dim.scale.value));
                    return true;
                }

                // Check if clicking inside the mod
                if (isInside(mouseX, mouseY, dim)) {
                    draggingMod = mod;
                    resizing = false;
                    offsetX = (int) (mouseX - dim.x.value);
                    offsetY = (int) (mouseY - dim.y.value);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            draggingMod = null;
            resizing = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingMod != null) {
            ModDimensions dim = draggingMod.getDimensions();

            if (resizing) {
                // Scale based on cursor movement
                float newScale = Math.max(0.5f, ((float) (mouseX - dim.x.value) / dim.width));
                dim.scale.value = newScale;
            } else {
                // Move the mod
                dim.x.value = (int) mouseX - offsetX;
                dim.y.value = (int) mouseY - offsetY;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void ui() {
    }

    @Override
    public void render(RenderScope renderScope, int mouseX, int mouseY, float delta, long elapsed) {
        for (HudMod mod : hudMods) {
            ModDimensions dim = mod.getDimensions();
            IMatrixStack matrices = renderScope.getMatrixStack();
            matrices.push();

            matrices.translate(dim.x.value, dim.y.value, 0);
            matrices.scale(dim.scale.value, dim.scale.value, 1.0f);

            // Render the mod
            mod.renderDummy(renderScope);

            matrices.pop();

            // Draw border around mod
            int borderX = dim.x.value;
            int borderY = dim.y.value;
            int borderW = (int) (dim.width * dim.scale.value);
            int borderH = (int) (dim.height * dim.scale.value);

            renderScope.drawBorder(borderX, borderY, borderW, borderH, 0xFFFFFFFF); // White border
        }
    }

    private boolean isInside(double mouseX, double mouseY, ModDimensions dim) {
        return mouseX >= dim.x.value && mouseX <= dim.x.value + (dim.width * dim.scale.value) &&
                mouseY >= dim.y.value && mouseY <= dim.y.value + (dim.height * dim.scale.value);
    }

    private boolean isCorner(double mouseX, double mouseY, ModDimensions dim) {
        float rightX = dim.x.value + dim.width * dim.scale.value;
        float bottomY = dim.y.value + dim.height * dim.scale.value;

        return Math.abs(mouseX - rightX) < RESIZE_MARGIN && Math.abs(mouseY - bottomY) < RESIZE_MARGIN;
    }

    @Override
    public void onClose() {
        ConfigManager.save();
    }
}
