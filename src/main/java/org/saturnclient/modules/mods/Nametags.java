package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.mods.utils.HealthRenderState;
import org.saturnclient.saturnclient.config.Property;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

// todo: rendering modes, custom names

public class Nametags extends Module {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Boolean> healthDisplay = Property.bool(false);
    private static Property<Boolean> players = Property.bool(true);
    private static Property<Boolean> hostile = Property.bool(false);
    private static Property<Boolean> passive = Property.bool(false);
    private static Property<Boolean> heartEmoji = Property.bool(true);
    private static Property<Boolean> obfuscate = Property.bool(false);
    private static Property<Integer> unit = Property.select(0, "Health", "Hearts");
    private static Property<Integer> format = Property.select(0, "Value", "Value / Total", "%");
    private static Property<Integer> nameColor = Property.select(15,
        "Black",
        "Dark Blue",
        "Dark Green",
        "Dark Aqua",
        "Dark Red",
        "Dark Purple",
        "Gold",
        "Gray",
        "Dark Gray",
        "Blue",
        "Green",
        "Aqua",
        "Red",
        "Light Purple",
        "Yellow",
        "White"
    );
    private static Property<Integer> healthColor = Property.select(12,
        "Black",
        "Dark Blue",
        "Dark Green",
        "Dark Aqua",
        "Dark Red",
        "Dark Purple",
        "Gold",
        "Gray",
        "Dark Gray",
        "Blue",
        "Green",
        "Aqua",
        "Red",
        "Light Purple",
        "Yellow",
        "White"
    );

    private static final String[] COLOR_CODES = {
        "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7",
        "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"
    };

    public Nametags() {
        super(
            new ModuleDetails("Nametags", "nametags")
                .description("Allows you to modify entity nametags")
                .tags("Camera", "Utility")
                .version("0.1.0"),
            enabled.named("Enabled"),
            healthDisplay.named("Display health"),
            players.named("Players"),
            hostile.named("Hostile"),
            passive.named("Passive"),
            heartEmoji.named("Heart emoji"),
            obfuscate.named("Obfuscate enemy names"),
            unit.named("Unit"),
            format.named("Format"),
            nameColor.named("Name color"),
            healthColor.named("Health color")
        );
    }
    
    @Override
    public void tick() {
       
    }
    
    public static String getHealthString(LivingEntityRenderState state, HealthRenderState hrs, float health, float maxHealth) {
        String ret = "";

        switch (format.value){
            case 0: // value
                ret = String.format("%.1f", health / (unit.value + 1));
                break;
            
            case 1: // value / total
                ret = String.format("%.1f", health / (unit.value + 1)) + " / " + String.format("%.1f", maxHealth / (unit.value + 1));
                break;
            
            case 2: // %
                ret = String.format("%.0f%%", (health / maxHealth) * 100);
                break;
        }
        return ret;
    }

    public static String getNametagString(LivingEntityRenderState state) {
        if (state.customName == null) return null;
        if (!healthDisplay.value) return state.customName.getString(); // name only
        if (!(state instanceof HealthRenderState hrs)) return null;
        
        float health = hrs.saturn$getHealth();
        float maxHealth = hrs.saturn$getMaxHealth();

        HealthRenderState.EntityType type = hrs.saturn$getEntityType();
        if (type == HealthRenderState.EntityType.PASSIVE && !passive.value) return null;
        if (type == HealthRenderState.EntityType.HOSTILE && !hostile.value) return null;
        if (type == HealthRenderState.EntityType.PLAYER && !players.value) return null;
        if (type == HealthRenderState.EntityType.OTHER) return null;

        if (health < 0f || maxHealth < 0f || health > maxHealth) return null;

        String hpColor = COLOR_CODES[healthColor.value];
        String color = COLOR_CODES[nameColor.value];
        String heart = heartEmoji.value ? " ❤" : "";
        String obf = obfuscate.value ? "§k" : "";
        String reset = "§r";

        String ret = color + obf + state.customName.getString() + reset +  " " + hpColor + getHealthString(state, hrs, health, maxHealth) + heart;
        
        return ret;
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
