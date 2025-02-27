package org.auraclient.auraclient.mixin;

import org.auraclient.auraclient.cloaks.Cloaks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public class AbstractClientPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "getSkinTextures", cancellable = true)
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        if (((AbstractClientPlayerEntity) (Object) this) instanceof ClientPlayerEntity && Cloaks.capeCacheIdentifier != null) {
            SkinTextures textures = cir.getReturnValue();
            cir.setReturnValue(new SkinTextures(
                    textures.texture(),
                    textures.textureUrl(),
                    Cloaks.capeCacheIdentifier,
                    textures.elytraTexture(),
                    textures.model(),
                    textures.secure()
            ));
        }
    }
}