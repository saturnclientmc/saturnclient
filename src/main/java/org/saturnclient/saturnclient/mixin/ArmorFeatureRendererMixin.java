package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;equippedHeadStack:Lnet/minecraft/item/ItemStack;", opcode = org.objectweb.asm.Opcodes.GETFIELD))
    private ItemStack redirectEquippedHeadStack(BipedEntityRenderState state) {
        ItemStack original = state.equippedHeadStack;
        if (original.isEmpty()) {
            return new ItemStack(Items.DIAMOND_HELMET);
        }
        return original;
    }
}