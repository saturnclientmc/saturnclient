package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.util.Window;

@Mixin(Window.class)
public abstract class IdentifierMixin implements IdentifierRef {
}
