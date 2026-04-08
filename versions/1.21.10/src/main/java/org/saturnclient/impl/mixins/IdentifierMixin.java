package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.Identifier;

@Mixin(Identifier.class)
public abstract class IdentifierMixin implements IdentifierRef {
    @Shadow
    public abstract String toString();
}
