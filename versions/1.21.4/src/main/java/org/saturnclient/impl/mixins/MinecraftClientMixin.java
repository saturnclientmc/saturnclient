package org.saturnclient.impl.mixins;

import java.io.File;
import java.io.InputStream;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.game.MinecraftClientRef;
import org.saturnclient.common.ref.render.WindowRef;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.SaturnScreenFabric;
import org.saturnclient.ui.screens.TitleMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientRef {
    @Shadow
    public File runDirectory;

    @Shadow
    private Window window;

    @Shadow
    public ReloadableResourceManagerImpl resourceManager;

    @Override
    public File getRunDirectory() {
        return runDirectory;
    }

    @Override
    public InputStream getResource(IdentifierRef identifier) {
        try {
            return resourceManager.getResource((Identifier) (Object) identifier).get().getInputStream();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public WindowRef getWindow() {
        return (WindowRef) (Object) this.window;
    }

    @Override
    public void setScreen(SaturnScreen screen) {
        SaturnClient.client.setScreen(new SaturnScreenFabric(screen));
    }

    @Override
    public void setScreen(MinecraftScreen screen) {
        SaturnScreenFabric exitTarget = new SaturnScreenFabric(new TitleMenu());

        switch (screen) {
            case SelectWorld:
                SaturnClient.client.setScreen(new SelectWorldScreen(exitTarget));
                break;

            case Multiplayer:
                SaturnClient.client.setScreen(new MultiplayerScreen(exitTarget));
                break;

            case Options:
                SaturnClient.client.setScreen(new OptionsScreen(exitTarget, SaturnClient.client.options));
                break;
        }
    }
}
