package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.modules.ModuleDetails;

public class Keystrokes extends Module implements HudMod {
    public static Property<Boolean> enabled = Property.bool(false);
    public static Property<Boolean> showMouse = Property.bool(true);
    public static Property<Boolean> showSpace = Property.bool(false);
    public static ModDimensions dimensions = new ModDimensions(78, 46);
    private boolean w = false;
    private boolean a = false;
    private boolean s = false;
    private boolean d = false;
    private boolean lmb = false;
    private boolean rmb = false;
    private boolean space = false;

    private static Property<Integer> clickBg = Property.color(0xFFCCCCCC);
    private static Property<Integer> clickFg = Property.color(0xFFFFFFFF);

    public Keystrokes() {
        super(new ModuleDetails("Keystrokes", "keystrokes")
            .description("Displays the keystrokes for movement")
            .version("v0.1.0")
            .tags("Utility"),

        enabled.named("Enabled"),
        showMouse.named("Show mouse clicks"),
        showSpace.named("Show space clicks"),
        dimensions.prop(),
        clickFg.named("Clicked fg"),
        clickBg.named("Clicked bg"));

        dimensions.renderBackground = false;
    }
    
    @Override
    public void tick() {
        w = SaturnClient.client.options.forwardKey.isPressed();
        a = SaturnClient.client.options.leftKey.isPressed();
        s = SaturnClient.client.options.backKey.isPressed();
        d = SaturnClient.client.options.rightKey.isPressed();
        lmb = SaturnClient.client.options.attackKey.isPressed();
        rmb = SaturnClient.client.options.useKey.isPressed();
        space = SaturnClient.client.options.jumpKey.isPressed();
    }

    @Override
    public void renderDummy(RenderScope scope) {
        dimensions.height = 54; // base height with padding considered

        // WASD keys (3px padding between each key)
        renderKey(scope, true, 'W', 27, 0);   // Top center
        renderKey(scope, false, 'A', 0, 27);   // Bottom left
        renderKey(scope, false, 'S', 27, 27);  // Bottom center
        renderKey(scope, false, 'D', 54, 27);  // Bottom right

        if (showMouse.value) {
            renderKeyM(scope, true, "LMB", 0, 54);   // Below A/S/D
            renderKeyM(scope, false, "RMB", 40, 54);  // With 3px between
            dimensions.height += 27;
        }

        if (showSpace.value) {
            renderKeySpace(scope, false, 0, dimensions.height);
            dimensions.height += 22;
        }
    }

    @Override
    public void renderHud(RenderScope scope) {
        dimensions.height = 54; // base height with padding considered

        // WASD keys (3px padding between each key)
        renderKey(scope, w, 'W', 27, 0);   // Top center
        renderKey(scope, a, 'A', 0, 27);   // Bottom left
        renderKey(scope, s, 'S', 27, 27);  // Bottom center
        renderKey(scope, d, 'D', 54, 27);  // Bottom right

        if (showMouse.value) {
            renderKeyM(scope, lmb, "LMB", 0, 54);   // Below A/S/D
            renderKeyM(scope, rmb, "RMB", 40, 54);  // With 3px between
            dimensions.height += 27;
        }

        if (showSpace.value) {
            renderKeySpace(scope, space, 0, dimensions.height);
            dimensions.height += 22;
        }
    }

    // Consistent 3px padding applied below:

    private void renderKeySpace(RenderScope scope, boolean isPressed, int x, int y) {
        int height = 19;
        scope.drawRoundedRectangle(x, y, dimensions.width, height, dimensions.radius.value, isPressed ? clickBg.value : dimensions.bgColor.value);

        // A 30-pixel line centered vertically with 3px top padding
        int lineY = y + (height - 1) / 2;
        scope.drawRect(x + (dimensions.width - 30) / 2, lineY, 30, 1, isPressed ? clickFg.value : dimensions.fgColor.value);
    }

    private void renderKeyM(RenderScope scope, boolean isPressed, String c, int x, int y) {
        int width = 38, height = 24;
        scope.drawRoundedRectangle(x, y, width, height, dimensions.radius.value, isPressed ? clickBg.value : dimensions.bgColor.value);

        // Apply padding: 3px top and left -> add slight offset to x/y
        scope.drawText(0.6f, c, x + 9, y + 7, dimensions.font.value, isPressed ? clickFg.value : dimensions.fgColor.value);
    }

    private void renderKey(RenderScope scope, boolean isPressed, char c, int x, int y) {
        int size = 24;
        scope.drawRoundedRectangle(x, y, size, size, dimensions.radius.value, isPressed ? clickBg.value : dimensions.bgColor.value);

        // Center the character with 3px top padding
        scope.drawText(0.6f, String.valueOf(c), x + 9, y + 7, dimensions.font.value, isPressed ? clickFg.value : dimensions.fgColor.value);
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
    public void setEnabled(boolean e) {
        enabled.value = e;
    }
}