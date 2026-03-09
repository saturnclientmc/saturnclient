package org.saturnclient.impl.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

import org.saturnclient.common.module.RenderModule;

/**
 * Fabric implementation of {@link RenderModule}.
 *
 * Window dimensions and FPS are read directly from
 * {@link MinecraftClient}.  Camera perspective control delegates to
 * {@link net.minecraft.client.option.GameOptions}.
 */
public class RenderModuleFabric implements RenderModule {

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
