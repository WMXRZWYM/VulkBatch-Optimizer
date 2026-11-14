package com.friend.vulkbatch.mixin;

import com.friend.vulkbatch.batch.BatchManager;
import com.friend.vulkbatch.config.Config;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
@SuppressWarnings("unused")
public class MixinBlockEntityRenderDispatcher {

    @Shadow
    private Vec3 cameraPos;

    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true, require = 0)
    private <E extends BlockEntity, S extends BlockEntityRenderState> void onTryExtractRenderState(
            E blockEntity, float partialTicks, ModelFeatureRenderer.CrumblingOverlay breakProgress, boolean isGloballyRendered, CallbackInfoReturnable<S> cir) {

        if (!Config.optimizeBlockEntity || this.cameraPos == null) return;

        // 使用 Vec3.atCenterOf 替代不存在的 getCenter() 方法
        Vec3 blockCenter = Vec3.atCenterOf(blockEntity.getBlockPos());
        double distSqr = this.cameraPos.distanceToSqr(blockCenter);
        double maxDist = Config.BLOCK_ENTITY_CULL_DISTANCE;

        if (distSqr > maxDist * maxDist) {
            BatchManager.recordBlockEntityCull();
            cir.setReturnValue(null);
        }
    }
}