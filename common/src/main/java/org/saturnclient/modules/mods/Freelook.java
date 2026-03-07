package org.saturnclient.modules.mods;

import org.saturnclient.modules.Module;
import org.saturnclient.modules.ModuleDetails;
import org.saturnclient.modules.interfaces.FreelookInterface;
import org.saturnclient.config.manager.Key;
import org.saturnclient.config.manager.Property;

public class Freelook extends Module {

    private static Property<Boolean> enabled = Property.bool(false);
    private static Property<Boolean> toggle = Property.bool(true);

    public static Property<Integer> freelookKey = Property.keybinding(Key.GLFW_KEY_H);

    public static boolean isFreeLooking = false;
    private static boolean was1stPerson = false;

    private final FreelookInterface minecraft;

    public Freelook(FreelookInterface minecraft) {
        super(
                new ModuleDetails("Freelook", "freelook")
                        .description("Allows you to look around freely without moving your character")
                        .tags("Movement", "Camera")
                        .version("0.1.0"),
                enabled.named("Enabled"),
                toggle.named("Toggle freelook"),
                freelookKey.named("Freelook Keybinding"));

        this.minecraft = minecraft;
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

        was1stPerson = minecraft.isFirstPerson();

        minecraft.setThirdPersonBack();
    }

    private void stopFreelook() {

        if (was1stPerson) {
            minecraft.setFirstPerson();
        }

        isFreeLooking = false;
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