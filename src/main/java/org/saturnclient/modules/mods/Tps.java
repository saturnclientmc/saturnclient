package org.saturnclient.modules.mods;

import org.saturnclient.modules.HudMod;
import org.saturnclient.modules.ModDimensions;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.config.manager.Property;
import org.saturnclient.ui2.RenderScope;
import org.saturnclient.ui2.resources.Fonts;

public class Tps extends Module implements HudMod {
    private static Property<Boolean> enabled = Property.bool(false);
    private static ModDimensions dimensions = new ModDimensions(60, Fonts.getHeight());

    private static long lastWorldAge = -1;
    private static long lastRealTime = -1;
    private static double currentTps = 20.0;

    private static final int SAMPLE_COUNT = 2;
    private static final double[] samples = new double[SAMPLE_COUNT];
    static {
        java.util.Arrays.fill(samples, 20.0);
    }
    private static int sampleIndex = 0;
    private static int samplesFilled = 0;

    public Tps() {
        super(new ModuleDetails("TPS Display", "tps")
            .description("Displays the TPS")
            .version("v0.1.0")
            .tags("Utility"),
            enabled.named("Enabled"),
            dimensions.prop()
        );
    }

    public static void onTimePacket(long worldAge) {
        long now = System.currentTimeMillis();

        if (lastWorldAge != -1 && lastRealTime != -1) {
            long tickDelta = worldAge - lastWorldAge;
            long timeDelta = now - lastRealTime;

            if (timeDelta > 0 && tickDelta > 0) {
                double sample = (tickDelta / (double) timeDelta) * 1000.0;
                sample = Math.min(sample, 20.0);

                samples[sampleIndex % SAMPLE_COUNT] = sample;
                sampleIndex++;
                samplesFilled = Math.min(samplesFilled + 1, SAMPLE_COUNT);

                double sum = 0;
                for (int i = 0; i < samplesFilled; i++) sum += samples[i];
                currentTps = sum / samplesFilled;
            }
        }

        lastWorldAge = worldAge;
        lastRealTime = now;
    }

    public static void reset() {
        lastWorldAge = -1;
        lastRealTime = -1;
        currentTps = 20.0;
        sampleIndex = 0;
        samplesFilled = SAMPLE_COUNT; 
        for (int i = 0; i < SAMPLE_COUNT; i++) samples[i] = 20.0;
    }

    @Override
    public void renderHud(RenderScope scope) {
        String text = String.format("%.1f TPS", currentTps);
        scope.drawText(text, 0, 0, dimensions.font.value, dimensions.fgColor.value);
        dimensions.width = Fonts.getWidth(text, dimensions.font.value);
    }

    @Override
    public void renderDummy(RenderScope scope) {
        String text = "20.0 TPS";
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
