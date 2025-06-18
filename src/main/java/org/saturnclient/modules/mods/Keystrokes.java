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
    public static ModDimensions dimensions = new ModDimensions(78, 52);
    private boolean w = false;
    private boolean a = false;
    private boolean s = false;
    private boolean d = false;

    private static Property<Integer> clickBg = Property.color(0xFFCCCCCC);
    private static Property<Integer> clickFg = Property.color(0xFFFFFFFF);

    public Keystrokes() {
        super(new ModuleDetails("Keystrokes", "keystrokes")
            .description("Displays the keystrokes for movement")
            .version("v0.1.0")
            .tags("Utility"),

        enabled.named("Enabled"),
        dimensions.prop());

        dimensions.renderBackground = false;
    }
    
    @Override
    public void tick() {
        w = SaturnClient.client.options.forwardKey.isPressed();
        a = SaturnClient.client.options.leftKey.isPressed();
        s = SaturnClient.client.options.backKey.isPressed();
        d = SaturnClient.client.options.rightKey.isPressed();
    }

    @Override
    public void renderHud(RenderScope scope) {
        renderKey(scope, w, 'W', (dimensions.width - 24) / 2, 0);
        renderKey(scope, a, 'A', 0, dimensions.height - 24);
        renderKey(scope, s, 'S', (dimensions.width - 24) / 2, dimensions.height - 24);
        renderKey(scope, d, 'D', dimensions.width - 24, dimensions.height - 24);
    }

    @Override
    public void renderDummy(RenderScope scope) {
        renderKey(scope, false, 'W', (dimensions.width - 24) / 2, 0);
        renderKey(scope, false, 'A', 0, dimensions.height - 24);
        renderKey(scope, false, 'S', (dimensions.width - 24) / 2, dimensions.height - 24);
        renderKey(scope, false, 'D', dimensions.width - 24, dimensions.height - 24);
    }

    private void renderKey(RenderScope scope, boolean isPressed, char c, int x, int y) {
        scope.drawRoundedRectangle(x, y, 24, 24, dimensions.radius.value, isPressed ? clickBg.value : dimensions.bgColor.value);
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