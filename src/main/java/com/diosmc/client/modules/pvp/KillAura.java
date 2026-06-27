package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {

    private double range = 4.5;
    private int attackDelay = 10; // ticks entre ataques
    private int tickCounter = 0;
    private boolean registered = false;

    public KillAura() {
        super("KillAura", "Ataca automáticamente al jugador más cercano", Category.PVP, GLFW.GLFW_KEY_R);
    }

    @Override
    public void onEnable() {
        tickCounter = 0;
        if (!registered) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!isEnabled() || client.player == null || client.world == null) return;

                tickCounter++;
                if (tickCounter < attackDelay) return;
                tickCounter = 0;

                List<PlayerEntity> players = client.world.getEntitiesByClass(
                        PlayerEntity.class,
                        client.player.getBoundingBox().expand(range),
                        p -> p != client.player
                );

                players.stream()
                        .min(Comparator.comparingDouble(p -> client.player.squaredDistanceTo(p)))
                        .ifPresent(target -> {
                            client.player.lookAt(
                                    net.minecraft.command.EntityAnchorArgument.EntityAnchor.EYES,
                                    target.getEyePos(1.0f)
                            );
                            client.interactionManager.attackEntity(client.player, target);
                            client.player.swingHand(Hand.MAIN_HAND);
                        });
            });
            registered = true;
        }
    }

    public double getRange() { return range; }
    public void setRange(double range) { this.range = range; }
    public int getAttackDelay() { return attackDelay; }
    public void setAttackDelay(int d) { this.attackDelay = d; }
}
