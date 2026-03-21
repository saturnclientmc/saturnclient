package org.saturnclient.impl.mixins;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.game.MinecraftClientRef;
import org.saturnclient.common.ref.render.WindowRef;
import org.saturnclient.impl.ui.SaturnScreenFabric;
import org.saturnclient.ui.SaturnScreen;
import org.saturnclient.ui.screens.TitleMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.session.Session;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientRef {
    @Shadow
    public File runDirectory;

    @Shadow
    private Window window;

    @Shadow
    private ReloadableResourceManagerImpl resourceManager;

    @Shadow
    public GameOptions options;

    @Shadow
    private Session session;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Shadow
    @Override
    public abstract void scheduleStop();

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
        this.setScreen(new SaturnScreenFabric(screen));
    }

    @Override
    public void setScreen(MinecraftScreen screen) {
        SaturnScreenFabric exitTarget = new SaturnScreenFabric(new TitleMenu());

        switch (screen) {
            case SelectWorld:
                this.setScreen(new SelectWorldScreen(exitTarget));
                break;

            case Multiplayer:
                this.setScreen(new MultiplayerScreen(exitTarget));
                break;

            case Options:
                this.setScreen(new OptionsScreen(exitTarget, this.options));
                break;
        }
    }

    @Override
    public String getAccessToken() {
        if (session == null) {
            return null;
        }

        return this.session.getAccessToken();
    }

    @Override
    public UUID getUuid() {
        if (session == null) {
            return null;
        }
        return this.session.getUuidOrNull();
    }

    @Override
    public String getUsername() {
        if (session == null) {
            return null;
        }
        return this.session.getUsername();
    }

    @Override
    public void onClientStopping(Runnable handler) {
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> handler.run());
    }

    @Override
    public void executeOnThread(Runnable runnable) {
        ((java.util.concurrent.Executor) (Object) this).execute(runnable);
    }
}
