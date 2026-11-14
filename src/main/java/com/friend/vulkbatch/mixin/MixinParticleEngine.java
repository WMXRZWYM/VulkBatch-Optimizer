package com.friend.vulkbatch.mixin;

import com.friend.vulkbatch.batch.BatchManager;
import com.friend.vulkbatch.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
// 使用 all 压制 IDEA Mixin 插件过于严苛的参数名警告
@SuppressWarnings("all")
public class MixinParticleEngine {

    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true, require = 0)
    private void onCreateParticle(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir) {
        if (!Config.optimizeParticle || Minecraft.getInstance().player == null) return;

        Vec3 cameraPos = Minecraft.getInstance().player.getEyePosition(1.0F);
        double distSqr = cameraPos.distanceToSqr(x, y, z);
        double maxDist = Config.PARTICLE_CULL_DISTANCE;

        if (distSqr > maxDist * maxDist) {
            BatchManager.recordParticleCull();
            cir.setReturnValue(null);
        }
    }
}