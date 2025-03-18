package org.saturnclient.saturnclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.saturnclient.saturnmods.HudMod;
import org.saturnclient.saturnmods.ModDimensions;
import org.saturnclient.saturnmods.ModManager;
import org.saturnclient.saturnmods.SaturnMod;
import java.util.ArrayList;
import java.util.List;

public class HudEditor extends Screen {
    private final List<HudMod> hudMods = new ArrayList<>();
    private HudMod draggingMod = null;
    private boolean resizing = false;
    private int offsetX, offsetY;
    private static final int RESIZE_MARGIN = 6;

    public HudEditor() {
        super(Text.literal("HUD Editor"));
        for (SaturnMod m : ModManager.MODS) {
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
                    offsetX = (int) (mouseX - (dim.x + dim.width * dim.scale));
                    offsetY = (int) (mouseY - (dim.y + dim.height * dim.scale));
                    return true;
                }

                // Check if clicking inside the mod
                if (isInside(mouseX, mouseY, dim)) {
                    draggingMod = mod;
                    resizing = false;
                    offsetX = (int) (mouseX - dim.x);
                    offsetY = (int) (mouseY - dim.y);
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
                float newScale = Math.max(0.5f, ((float) (mouseX - dim.x) / dim.width));
                dim.scale = newScale;
            } else {
                // Move the mod
                dim.x = (int) mouseX - offsetX;
                dim.y = (int) mouseY - offsetY;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (HudMod mod : hudMods) {
            ModDimensions dim = mod.getDimensions();
            MatrixStack matrices = context.getMatrices();
            matrices.push();

            matrices.translate(dim.x, dim.y, 0);
            matrices.scale(dim.scale, dim.scale, 1.0f);

            // Render the mod
            mod.renderDummy(context);

            matrices.pop();

            // Draw border around mod
            int borderX = dim.x;
            int borderY = dim.y;
            int borderW = (int) (dim.width * dim.scale);
            int borderH = (int) (dim.height * dim.scale);

            context.drawBorder(borderX, borderY, borderW, borderH, 0xFFFFFFFF); // White border
        }
    }

    private boolean isInside(double mouseX, double mouseY, ModDimensions dim) {
        return mouseX >= dim.x && mouseX <= dim.x + (dim.width * dim.scale) &&
                mouseY >= dim.y && mouseY <= dim.y + (dim.height * dim.scale);
    }

    private boolean isCorner(double mouseX, double mouseY, ModDimensions dim) {
        float rightX = dim.x + dim.width * dim.scale;
        float bottomY = dim.y + dim.height * dim.scale;

        return Math.abs(mouseX - rightX) < RESIZE_MARGIN && Math.abs(mouseY - bottomY) < RESIZE_MARGIN;
    }
}
