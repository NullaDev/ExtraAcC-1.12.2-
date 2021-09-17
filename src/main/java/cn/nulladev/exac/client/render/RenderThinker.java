package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityThinker;
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

@RegEntityRender(EntityThinker.class)
public class RenderThinker extends Render<EntityThinker> {

    public RenderThinker(RenderManager renderManager) {
        super(renderManager);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityThinker entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        if (entity == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + entity.getEyeHeight(), z);

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();

        float a = getAlpha(entity.ticksExisted);
        GlStateManager.color(1F, 0.5F, 0.5F, a);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        int gradation = 30;
        double r = getR(entity.ticksExisted);
        for (int i = 0; i < gradation; i++) {
            float alpha = (float) (i * Math.PI / gradation);
            b.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
            for (int j = 0; j <= gradation; j++) {
                double beta = j * 2 * Math.PI / gradation;
                double x1 = r * Math.cos(beta) * Math.sin(alpha);
                double y1 = r * Math.sin(beta) * Math.sin(alpha);
                double z1 = r * Math.cos(alpha);
                b.pos(x1, y1, z1).endVertex();
                x1 = r * Math.cos(beta) * Math.sin(alpha + Math.PI / gradation);
                y1 = r * Math.sin(beta) * Math.sin(alpha + Math.PI / gradation);
                z1 = r * Math.cos(alpha + Math.PI / gradation);
                b.pos(x1, y1, z1).endVertex();
            }
            t.draw();
        }

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityThinker p_110775_1_) {
        return null;
    }

    private static float getAlpha(int tick) {
        if (tick <= 20)
            return 0.5F;
        else
            return 0.5F * (25 - tick) / 5;
    }

    private static float getR(int tick) {
        if (tick <= 20)
            return 0F + 0.5F * tick / 20;
        else
            return 0.5F - 0.1F * (tick - 20);
        //return 3F;
    }
}
