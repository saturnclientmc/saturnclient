package org.saturnclient.impl.modules;

import org.saturnclient.feature.interfaces.FreelookInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

public class FreelookFabric implements FreelookInterface {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public boolean isFirstPerson() {
        return mc.options.getPerspective() == Perspective.FIRST_PERSON;
    }

    @Override
    public void setFirstPerson() {
        mc.options.setPerspective(Perspective.FIRST_PERSON);
    }

    @Override
    public void setThirdPersonBack() {
        mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

}