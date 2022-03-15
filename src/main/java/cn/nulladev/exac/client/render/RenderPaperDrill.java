package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityPaperDrill;
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

@RegEntityRender(EntityPaperDrill.class)
public class RenderPaperDrill extends Render<EntityPaperDrill> {

    public RenderPaperDrill(RenderManager renderManager) {
        super(renderManager);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityPaperDrill entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        if (entity == null)
            return;

        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(entity.prevRotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch, 0.0F, 0.0F, 1.0F);

        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        GlStateManager.color(1F, 0.5F, 0.5F, 0.9F);

        double w = 0.4D;
        float angel = 360F * (entity.ticksExisted % 20) / 20;
        GlStateManager.rotate(angel, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(5D, 0, 0);
        for (int i = 0; i < 8; i++) {
            int gradation = 2 * i + 1;
            double r = 0.25D * i;
            for (int j = 0; j < gradation; ++j) {
                GlStateManager.rotate(360.0F / gradation, 1.0F, 0.0F, 0.0F);
                GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
                b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                b.pos(-w, r, w).endVertex();
                b.pos(w, r, w).endVertex();
                b.pos(w, r, -w).endVertex();
                b.pos(-w, r, -w).endVertex();
                t.draw();
            }
            if (i % 2 == 0)
                GlStateManager.rotate(-2 * angel, 1.0F, 0.0F, 0.0F);
            else
                GlStateManager.rotate(2 * angel, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(-0.5D, 0, 0);
        }

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();;
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPaperDrill p_110775_1_) {
        return null;
    }
}
