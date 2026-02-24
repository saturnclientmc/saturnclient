package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.config.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class Ping extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, Fonts.getHeight());
    
    public Ping() {
        super(new ModuleDetails("Ping Display", "ping")
            .description("Displays ping")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            dimensions.prop());
    }

    @Override
    public void renderHud(RenderScope scope) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayNetworkHandler handler = client.getNetworkHandler();
        String text = "0 ms";
        if (handler != null) {
            text = String.valueOf(handler.getPlayerListEntry(client.player.getUuid()).getLatency()) + " ms";
        }
        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public void renderDummy(RenderScope scope) {
        String text = "10 ms";
        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public ModDimensions getDimensions() {
        return dimensions;
    }
}
