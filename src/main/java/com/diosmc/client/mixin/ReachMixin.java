package com.diosmc.client.mixin;

import com.diosmc.client.DiosMCClient;
import com.diosmc.client.modules.pvp.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ReachMixin {

    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        if (DiosMCClient.moduleManager == null) return;

        Reach reach = (Reach) DiosMCClient.moduleManager.getModuleByName("Reach");
        if (reach != null && reach.isEnabled()) {
            cir.setReturnValue((float) reach.getReachDistance());
        }
    }
}
