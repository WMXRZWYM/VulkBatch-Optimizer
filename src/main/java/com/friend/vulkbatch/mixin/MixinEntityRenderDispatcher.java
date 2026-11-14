package com.friend.vulkbatch.mixin;

import com.friend.vulkbatch.batch.BatchManager;
import com.friend.vulkbatch.config.Config;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
@SuppressWarnings({"unused", "Mixin"}) // 抑制签名不匹配相关的 IDE 警告
public class MixinEntityRenderDispatcher {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
    private <E extends Entity> void onShouldRender(E entity, Frustum culler, double camX, double camY, double camZ, float partialTicks, CallbackInfoReturnable<Boolean> cir) {
        cull(entity, camX, camY, camZ, cir);
    }

    @Unique
    private static <E extends Entity> void cull(E entity, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.optimizeEntity || entity instanceof Player) return;

        double distSqr = camX * camX + camY * camY + camZ * camZ;

        if (entity instanceof ItemEntity && distSqr > 4096.0D) {
            BatchManager.recordEntityCull();
            cir.setReturnValue(false);
        } else if (entity instanceof ExperienceOrb && distSqr > 1024.0D) {
            BatchManager.recordEntityCull();
            cir.setReturnValue(false);
        }
    }
}