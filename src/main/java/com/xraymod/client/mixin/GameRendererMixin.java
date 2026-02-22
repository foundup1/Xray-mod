package com.xraymod.client.mixin;

import com.xraymod.client.XRayModClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockState.class)
public class GameRendererMixin {

    @Inject(method = "isSideInvisible", at = @At("RETURN"), cancellable = true)
    private void xray_isSideInvisible(BlockState state, net.minecraft.util.math.Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (XRayModClient.xRayManager.isEnabled()) {
            BlockState self = (BlockState) (Object) this;
            Block block = self.getBlock();
            if (XRayModClient.xRayManager.shouldHideBlock(block)) {
                cir.setReturnValue(false);
            }
        }
    }
}
