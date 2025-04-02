package org.saturnclient.saturnclient.mixin;

import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ColorHelper;

import org.saturnclient.saturnmods.mods.Particles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TotemParticle.class)
public class TotemParticleMixin extends AnimatedParticle {
    protected TotemParticleMixin(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider,
            float upwardsAcceleration) {
        super(world, x, y, z, spriteProvider, upwardsAcceleration);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void colorChanger(ClientWorld world, double x, double y, double z, double velocityX, double velocityY,
            double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        if (Particles.enabled.value && Particles.totem_enabled.value) {
            if (this.random.nextInt(2) == 0) {
                this.setColor(ColorHelper.getRedFloat(Particles.totem_1.value),
                        ColorHelper.getGreenFloat(Particles.totem_1.value),
                        ColorHelper.getBlueFloat(Particles.totem_1.value));
            } else {
                this.setColor(ColorHelper.getRedFloat(Particles.totem_2.value),
                        ColorHelper.getGreenFloat(Particles.totem_2.value),
                        ColorHelper.getBlueFloat(Particles.totem_2.value));
            }
        }
    }
}