package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityBonder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@RegEntityRender(EntityBonder.class)
public class RenderBonder extends Render<EntityBonder> {

    public RenderBonder(RenderManager renderManager) {
        super(renderManager);
    }

    private static float getA(int tick) {
        if (tick <= EntityBonder.SLAM_TIME)
            return 0.5F;
        else
            return MathUtils.lerpf(0.5F, 0F, (float)(tick - EntityBonder.SLAM_TIME) / (EntityBonder.AGE - EntityBonder.SLAM_TIME));
    }

    private static float getH(int tick) {
        if (tick <= EntityBonder.SLAM_TIME)
            return -0.1F;
        else
            return -0.1F - 0.05F * (tick - EntityBonder.SLAM_TIME);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityBonder entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        if (entity == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();

        GlStateManager.color(1F, 0.5F, 0.5F, getA(entity.ticksExisted));

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        float r = 1;
        float h1 = getH(entity.ticksExisted);
        float h2 = 0.1F;
        b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
        for (int i = 0; i <= 30; i++) {
            double theta = 2 * Math.PI * i / 30;
            b.pos(r * Math.sin(theta), h1, r * Math.cos(theta)).endVertex();
            b.pos(r * Math.sin(theta), h2, r * Math.cos(theta)).endVertex();
        }
        t.draw();

        if (entity.ticksExisted >= EntityBonder.SLAM_TIME) {
            b.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
            b.pos(0, h1, 0);
            for (int i = 0; i <= 30; i++) {
                double theta = 2 * Math.PI * i / 30;
                b.pos(Math.sin(theta), h1, Math.cos(theta)).endVertex();
            }
            t.draw();
            b.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
            b.pos(0, h2, 0);
            for (int i = 0; i <= 30; i++) {
                double theta = 2 * Math.PI * i / 30;
                b.pos(Math.sin(theta), h2, Math.cos(theta)).endVertex();
            }
            t.draw();
        }

        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBonder p_110775_1_) {
        return null;
    }
}
