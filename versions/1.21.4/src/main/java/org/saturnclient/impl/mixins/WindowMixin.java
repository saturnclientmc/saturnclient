package org.saturnclient.impl.mixins;

import org.saturnclient.common.render.IWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.util.Window;

@Mixin(Window.class)
public abstract class WindowMixin implements IWindow {
    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract int getHeight();

    @Shadow
    public abstract int getFramebufferWidth();

    @Shadow
    public abstract int getFramebufferHeight();
}
