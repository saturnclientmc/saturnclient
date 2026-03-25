package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public abstract class DrawContextAccessor {
    @Shadow
    public GuiRenderState state;

    public GuiRenderState getState() {
        return state;
    }
}
