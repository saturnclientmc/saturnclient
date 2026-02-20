package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.mods.utils.HealthRenderState;
import org.saturnclient.saturnclient.config.Property;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

// todo: rendering modes, player check

public class Nametags extends Module {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Boolean> healthDisplay = Property.bool(false);
    
    public Nametags() {
        super(
            new ModuleDetails("Nametags", "nametags")
                .description("Allows you to modify entity nametags")
                .tags("Camera", "Utility")
                .version("0.1.0"),
            enabled.named("Enabled"),
            healthDisplay.named("Display health")
        );
    }
    
    @Override
    public void tick() {
       
    }

    
    public static String getNametagString(LivingEntityRenderState state) {
        if (state.customName == null) return null;
        if (!healthDisplay.value) return state.customName.getString(); // name only
        if (!(state instanceof HealthRenderState hrs)) return null;
        
        float health = hrs.saturn$getHealth();
        float maxHealth = hrs.saturn$getMaxHealth();

        if (health < 0f || maxHealth < 0f || health > maxHealth) return null;

        return state.customName.getString()
            + " §c" + String.format("%.1f", hrs.saturn$getHealth())
            + " / " + String.format("%.1f", hrs.saturn$getMaxHealth()) + " ❤";
            
    }

    public static boolean shouldReplaceName() { 
        return enabled.value;
    }

    @Override
    public void onEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }
}
