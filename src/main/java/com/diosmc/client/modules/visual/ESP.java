package com.diosmc.client.modules.visual;

import com.diosmc.client.modules.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderLastCallback;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ESP extends Module {

    private float red = 1.0f, green = 0.2f, blue = 0.2f;
    private float lineWidth = 1.5f;
    private boolean registered = false;

    public ESP() {
        super("ESP", "Dibuja cajas alrededor de jugadores a través de paredes", Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            WorldRenderLastCallback.EVENT.register((context, tickDelta) -> {
                if (!isEnabled() || mc.player == null || mc.world == null) return;
                renderESP(context.getMatrixStack(), tickDelta);
            });
            registered = true;
        }
    }

    private void renderESP(MatrixStack matrices, float tickDelta) {
        Vec3d cam = mc.gameRenderer.getCamera().getPos();

        List<PlayerEntity> players = mc.world.getEntitiesByClass(
                PlayerEntity.class,
                mc.player.getBoundingBox().expand(128),
                p -> p != mc.player
        );

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        GL11.glLineWidth(lineWidth);
        RenderSystem.disableTexture();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        for (PlayerEntity player : players) {
            Box box = player.getBoundingBox().expand(0.1);
            drawBox(matrices, buf, box, red, green, blue, 1.0f);
        }

        matrices.pop();

        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private void drawBox(MatrixStack matrices, BufferBuilder buf, Box box,
                         float r, float g, float b, float a) {
        Matrix4f m = matrices.peek().getModel();
        buf.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);

        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;

        // 12 aristas de la caja
        int[][] edges = {
            {0,0,0,1,0,0},{1,0,0,1,0,1},{1,0,1,0,0,1},{0,0,1,0,0,0}, // inferior
            {0,1,0,1,1,0},{1,1,0,1,1,1},{1,1,1,0,1,1},{0,1,1,0,1,0}, // superior
            {0,0,0,0,1,0},{1,0,0,1,1,0},{1,0,1,1,1,1},{0,0,1,0,1,1}  // pilares
        };

        for (int[] e : edges) {
            buf.vertex(m, e[0]==0?x1:x2, e[1]==0?y1:y2, e[2]==0?z1:z2).color(r,g,b,a).next();
            buf.vertex(m, e[3]==0?x1:x2, e[4]==0?y1:y2, e[5]==0?z1:z2).color(r,g,b,a).next();
        }

        Tessellator.getInstance().draw();
    }

    public void setColor(float r, float g, float b) { red = r; green = g; blue = b; }
    public void setLineWidth(float w) { lineWidth = w; }
}
