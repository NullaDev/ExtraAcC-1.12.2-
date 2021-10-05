package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityShell;
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

@RegEntityRender(EntityShell.class)
public class RenderShell extends Render<EntityShell> {

    public RenderShell(RenderManager renderManager) {
        super(renderManager);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityShell entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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

        GlStateManager.color(1F, 0.5F, 0.5F, 0.5F);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
        b.pos(-0.3, 0, -0.3).endVertex();
        b.pos(-0.3, 2, -0.3).endVertex();
        b.pos( 0.3, 0, -0.3).endVertex();
        b.pos( 0.3, 2, -0.3).endVertex();
        b.pos( 0.3, 0,  0.3).endVertex();
        b.pos( 0.3, 2,  0.3).endVertex();
        b.pos(-0.3, 0,  0.3).endVertex();
        b.pos(-0.3, 2,  0.3).endVertex();
        b.pos(-0.3, 0, -0.3).endVertex();
        b.pos(-0.3, 2, -0.3).endVertex();
        t.draw();

        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        b.pos(-0.3, 0, -0.3).endVertex();
        b.pos( 0.3, 0, -0.3).endVertex();
        b.pos( 0.3, 0,  0.3).endVertex();
        b.pos(-0.3, 0,  0.3).endVertex();
        t.draw();

        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        b.pos(-0.3, 2, -0.3).endVertex();
        b.pos( 0.3, 2, -0.3).endVertex();
        b.pos( 0.3, 2,  0.3).endVertex();
        b.pos(-0.3, 2,  0.3).endVertex();
        t.draw();

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityShell p_110775_1_) {
        return null;
    }

}
