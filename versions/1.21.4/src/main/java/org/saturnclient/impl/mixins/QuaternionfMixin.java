package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.render.QuaternionfRef;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class QuaternionfMixin implements QuaternionfRef {
}
