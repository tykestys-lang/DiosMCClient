package com.diosmc.client.mixin;

import com.diosmc.client.DiosMCClient;
import com.diosmc.client.modules.pvp.Velocity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class VelocityMixin {

    @Inject(method = "onEntityVelocityUpdate", at = @At("HEAD"), cancellable = true)
    private void onVelocity(EntityVelocityUpdateS2CPacket packet, CallbackInfo ci) {
        if (DiosMCClient.moduleManager == null) return;

        Velocity vel = (Velocity) DiosMCClient.moduleManager.getModuleByName("Velocity");
        if (vel == null || !vel.isEnabled()) return;

        // Cancelar el paquete de velocidad completo si multiplier = 0
        if (vel.getHorizontalMultiplier() == 0f && vel.getVerticalMultiplier() == 0f) {
            ci.cancel();
        }
        // Si se quiere knockback parcial, se haría via @ModifyArg sobre los valores del packet
    }
}
