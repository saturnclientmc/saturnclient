package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.game.ItemStackRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackRef {
    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getDamage();
}
