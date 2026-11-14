package com.friend.vulkbatch.mixin;

import com.friend.vulkbatch.batch.BatchManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SectionRenderDispatcher.RenderSection.class)
@SuppressWarnings("unused")
public abstract class MixinLevelRenderer {

    @Shadow
    private volatile long sectionNode;

    @Inject(method = "compileAsync", at = @At("HEAD"), cancellable = true, require = 0)
    private void vulkbatch$skipRedundantRebuild(RenderSectionRegion region, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        SectionPos pos = SectionPos.of(this.sectionNode);
        long currentTick = mc.level.getGameTime();

        if (BatchManager.shouldSkipChunk(pos, currentTick)) {
            ci.cancel();
        }
    }
}