package org.saturnclient.saturnclient.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for handling custom cloaks in the AbstractClientPlayerEntity class.
 * Injects custom cloak textures for players who have them assigned.
 */
@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class CapeRenderer {
    /**
     * Injects custom cloak textures into the player's skin textures.
     */
    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void injectCustomTextures(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        String uuid = player.getUuidAsString().replace("-", "");

        Identifier cloakTexture = Cloaks.getCurrentCloakTexture(uuid);
        if (cloakTexture != null) {
            SkinTextures textures = cir.getReturnValue();
            cir.setReturnValue(new SkinTextures(
                    textures.texture(),
                    textures.textureUrl(),
                    cloakTexture,
                    textures.elytraTexture(),
                    textures.model(),
                    textures.secure()));
        }
    }
}