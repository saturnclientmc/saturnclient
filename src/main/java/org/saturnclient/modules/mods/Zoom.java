package org.saturnclient.modules.mods;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.config.manager.Property;

public class Zoom extends Module {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Boolean> toggle = Property.bool(false);
    public static Property<Integer> zoomKey = Property.keybinding(GLFW.GLFW_KEY_C);
    public static Property<Float> zoomLevel = Property.floatProp(3.0f);
    
    public static boolean isZooming = false;
    
    public Zoom() {
        super(
            new ModuleDetails("Zoom", "zoom")
                .description("Allows you to zoom in for a closer view")
                .tags("Camera", "Utility")
                .version("0.1.0"),
            enabled.named("Enabled"),
            toggle.named("Toggle zoom"),
            zoomKey.named("Zoom Keybinding"),
            zoomLevel.named("Zoom Level")
        );
    }
    
    @Override
    public void tick() {
        if (toggle.value) {
            if (zoomKey.wasKeyPressed()) {
                isZooming = !isZooming;
            }
        } else {
            isZooming = zoomKey.isKeyPressed();
        }
    }

    public static float getZoomLevel() {
        return zoomLevel.value;
    }

    public static boolean shouldZoom() { 
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
        if (!e) {
            isZooming = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }
}
