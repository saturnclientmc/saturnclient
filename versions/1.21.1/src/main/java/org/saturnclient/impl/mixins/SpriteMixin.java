package org.saturnclient.impl.mixins;

import net.minecraft.client.texture.Sprite;
import org.saturnclient.common.ref.asset.SpriteRef;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sprite.class)
public abstract class SpriteMixin implements SpriteRef {
}