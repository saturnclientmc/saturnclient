package org.saturnclient.modules.mods;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.config.Property;

import net.minecraft.client.option.Perspective;

public class Freelook extends Module {
    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Boolean> toggle = Property.bool(true);
    public static Property<Integer> freelookKey = Property.keybinding(GLFW.GLFW_KEY_H);
    public static boolean isFreeLooking = false;
    private static boolean was1stPerson = false;
    
    public Freelook() {
        super(
            new ModuleDetails("Freelook", "freelook")
                .description("Allows you to look around freely without moving your character")
                .tags("Movement", "Camera")
                .version("0.1.0"),
            enabled.named("Enabled"),
            toggle.named("Toggle freelook"),
            freelookKey.named("Freelook Keybinding")
        );
    }
    
    @Override
    public void tick() {
        if (toggle.value) {
            if (freelookKey.wasKeyPressed()) {
                if (isFreeLooking) {
                    stopFreelook();
                } else {
                    startFreelook();
                }
            }
        } else if (freelookKey.isKeyPressed()) {
            if (!isFreeLooking) {
                startFreelook();
            }
        } else if (isFreeLooking) {
            stopFreelook();
        }
    }

    private void startFreelook() {
        isFreeLooking = true;
        was1stPerson = SaturnClient.client.options.getPerspective() == Perspective.FIRST_PERSON;
        SaturnClient.client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    private void stopFreelook() {
        if (was1stPerson) {
            SaturnClient.client.options.setPerspective(Perspective.FIRST_PERSON);
        }
        isFreeLooking = false;
    }

    @Override
    public void setEnabled(boolean e) {
        enabled.value = e;
    }

    @Override
    public boolean isEnabled() {
        return enabled.value;
    }
}
