package org.saturnclient.impl.mixins;

import java.io.File;
import java.io.InputStream;

import org.saturnclient.common.IMinecraftClient;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.render.WindowRef;
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
    public InputStream getResource(IdentifierRef identifier) {
        try {
            return resourceManager.getResource((Identifier) identifier.inner).get().getInputStream();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public WindowRef getWindow() {
        return (WindowRef) (Object) this.window;
    }
}
