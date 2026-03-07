package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.KeystrokesInterface;
import org.saturnclient.config.manager.Property;
import org.saturnclient.ui.RenderScope;

public class Keystrokes extends Module implements HudMod {

    private static final Property<Boolean> enabled = Property.bool(false);
    private static final Property<Boolean> showMouse = Property.bool(true);
    private static final Property<Boolean> showSpace = Property.bool(false);

    private static final Property<Integer> clickBg = Property.color(0xFFCCCCCC);
    private static final Property<Integer> clickFg = Property.color(0xFFFFFFFF);

    private static final ModDimensions dimensions = new ModDimensions(78, 54);

    private final KeystrokesInterface minecraft;

    // Key states
    private boolean w, a, s, d, lmb, rmb, space;

    // Layout constants
    private static final int KEY_SIZE = 24;
    private static final int KEY_SPACING = 3;
    private static final int MOUSE_WIDTH = 38;
    private static final int MOUSE_HEIGHT = 24;
    private static final int SPACE_HEIGHT = 19;

    public Keystrokes(KeystrokesInterface minecraft) {
        super(new ModuleDetails("Keystrokes", "keystrokes")
                .description("Displays movement keystrokes")
                .version("v0.2.0")
                .tags("Utility"),

                enabled.named("Enabled"),
                showMouse.named("Show mouse clicks"),
                showSpace.named("Show space"),
                dimensions.prop(),
                clickFg.named("Clicked fg"),
                clickBg.named("Clicked bg"));

        this.minecraft = minecraft;

        dimensions.renderBackground = false;
    }

    @Override
    public void tick() {
        w = minecraft.isForwardPressed();
        a = minecraft.isLeftPressed();
        s = minecraft.isBackPressed();
        d = minecraft.isRightPressed();
        lmb = minecraft.isAttackPressed();
        rmb = minecraft.isUsePressed();
        space = minecraft.isJumpPressed();

        updateHeight();
    }

    private void updateHeight() {
        int height = KEY_SIZE * 2 + KEY_SPACING;

        if (showSpace.value)
            height += SPACE_HEIGHT + KEY_SPACING;

        if (showMouse.value)
            height += MOUSE_HEIGHT + KEY_SPACING;

        dimensions.height = height;
    }

    @Override
    public void renderDummy(RenderScope scope) {
        render(scope, false);
    }

    @Override
    public void renderHud(RenderScope scope) {
        render(scope, true);
    }

    private void render(RenderScope scope, boolean live) {

        int yOffset = 0;

        // --- W ---
        renderKey(scope, live && w, 'W', KEY_SIZE + KEY_SPACING, yOffset);

        // --- A S D ---
        yOffset += KEY_SIZE + KEY_SPACING;

        renderKey(scope, live && a, 'A', 0, yOffset);
        renderKey(scope, live && s, 'S', KEY_SIZE + KEY_SPACING, yOffset);
        renderKey(scope, live && d, 'D', (KEY_SIZE + KEY_SPACING) * 2, yOffset);

        yOffset += KEY_SIZE + KEY_SPACING;

        // --- SPACE ---
        if (showSpace.value) {
            renderKeySpace(scope, live && space, 0, yOffset);
            yOffset += SPACE_HEIGHT + KEY_SPACING;
        }

        // --- MOUSE ---
        if (showMouse.value) {
            renderKeyMouse(scope, live && lmb, "LMB", 0, yOffset);
            renderKeyMouse(scope, live && rmb, "RMB", MOUSE_WIDTH + KEY_SPACING, yOffset);
        }
    }

    private void renderKey(RenderScope scope, boolean pressed, char c, int x, int y) {
        int bg = pressed ? clickBg.value : dimensions.bgColor.value;
        int fg = pressed ? clickFg.value : dimensions.fgColor.value;

        scope.drawRoundedRectangle(x, y, KEY_SIZE, KEY_SIZE, dimensions.radius.value, bg);
        scope.drawText(0.6f, String.valueOf(c), x + 9, y + 7, dimensions.font.value, fg);
    }

    private void renderKeyMouse(RenderScope scope, boolean pressed, String text, int x, int y) {
        int bg = pressed ? clickBg.value : dimensions.bgColor.value;
        int fg = pressed ? clickFg.value : dimensions.fgColor.value;

        scope.drawRoundedRectangle(x, y, MOUSE_WIDTH, MOUSE_HEIGHT, dimensions.radius.value, bg);
        scope.drawText(0.6f, text, x + 9, y + 7, dimensions.font.value, fg);
    }

    private void renderKeySpace(RenderScope scope, boolean pressed, int x, int y) {
        int bg = pressed ? clickBg.value : dimensions.bgColor.value;
        int fg = pressed ? clickFg.value : dimensions.fgColor.value;

        scope.drawRoundedRectangle(x, y, dimensions.width, SPACE_HEIGHT, dimensions.radius.value, bg);

        int lineY = y + SPACE_HEIGHT / 2;
        scope.drawRect(x + (dimensions.width - 30) / 2, lineY, 30, 1, fg);
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }
}