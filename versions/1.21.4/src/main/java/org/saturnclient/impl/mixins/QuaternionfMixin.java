package org.saturnclient.impl.mixins;

import org.joml.Quaternionf;
import org.saturnclient.common.ref.render.QuaternionfRef;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Quaternionf.class)
public abstract class QuaternionfMixin implements QuaternionfRef {
}
