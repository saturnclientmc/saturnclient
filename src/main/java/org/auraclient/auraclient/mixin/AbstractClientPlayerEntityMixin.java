package org.auraclient.auraclient.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import org.auraclient.auraclient.cloaks.Cloaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for handling custom cloaks in the AbstractClientPlayerEntity class.
 * Injects custom cape textures for players who have them assigned.
 */
@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public class AbstractClientPlayerEntityMixin {
    
    /**
     * Injects custom cape textures into the player's skin textures.
     * @param cir Callback containing the original skin textures
     */
    @Inject(
        method = "getSkinTextures",
        at = @At("RETURN"),
        cancellable = true
    )
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        
        if (Cloaks.playerCapes.containsKey(player.getName().getString()) && 
            Cloaks.capeCacheIdentifier != null) {
            
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