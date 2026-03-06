package org.saturnclient.impl.mixins;

import java.io.File;
import java.io.InputStream;

import org.saturnclient.common.minecraft.IMinecraftClient;
import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.common.minecraft.render.IWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IMinecraftClient {
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
    public InputStream getResource(SaturnIdentifier identifier) {
        try {
            return resourceManager.getResource((Identifier) identifier.inner).get().getInputStream();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IWindow getWindow() {
        return (IWindow) (Object) this.window;
    }
}
