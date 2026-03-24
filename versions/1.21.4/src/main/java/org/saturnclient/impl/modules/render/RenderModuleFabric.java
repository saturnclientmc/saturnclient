package org.saturnclient.impl.modules.render;

import org.saturnclient.common.feature.RenderFeature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

/**
 * Fabric implementation of {@link RenderFeature}.
 *
 * Window dimensions and FPS are read directly from
 * {@link MinecraftClient}. Camera perspective control delegates to
 * {@link net.minecraft.client.option.GameOptions}.
 */
public class RenderModuleFabric implements RenderFeature {

    private final MinecraftClient mc;

    public RenderModuleFabric(MinecraftClient mc) {
        this.mc = mc;
    }

    // ---------------------------------------------------------------
    // Window
    // ---------------------------------------------------------------

    @Override
    public int getScaledWindowWidth() {
        return mc.getWindow().getScaledWidth();
    }

    @Override
    public int getScaledWindowHeight() {
        return mc.getWindow().getScaledHeight();
    }

    // ---------------------------------------------------------------
    // Performance
    // ---------------------------------------------------------------

    @Override
    public int getFps() {
        return mc.getCurrentFps();
    }

    // ---------------------------------------------------------------
    // Camera perspective
    // ---------------------------------------------------------------

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
