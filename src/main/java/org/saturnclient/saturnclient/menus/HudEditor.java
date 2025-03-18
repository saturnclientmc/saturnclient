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
    private List<HudMod> hudMods = new ArrayList<>();
    private HudMod draggingMod = null;
    private int offsetX, offsetY;

    public HudEditor() {
        super(Text.literal("HUD Editor"));
        for (SaturnMod m : ModManager.MODS) {
            if (m.isEnabled() && m instanceof HudMod) {
                HudMod mod = (HudMod) m;
                hudMods.add(mod);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int m = hudMods.size() - 1; m >= 0; m--) {
                HudMod mod = hudMods.get(m);
                ModDimensions dim = mod.getDimensions();
                if (isInside(mouseX, mouseY, dim)) {
                    draggingMod = mod;
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
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingMod != null) {
            ModDimensions dim = draggingMod.getDimensions();
            dim.x = (int) mouseX - offsetX;
            dim.y = (int) mouseY - offsetY;
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

            mod.renderDummy(context);

            matrices.pop();
        }
    }

    private boolean isInside(double mouseX, double mouseY, ModDimensions dim) {
        return mouseX >= dim.x && mouseX <= dim.x + (dim.width * dim.scale) && mouseY >= dim.y
                && mouseY <= dim.y + (dim.height * dim.scale);
    }
}
