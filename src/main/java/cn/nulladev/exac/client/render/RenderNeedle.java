package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityNeedle;
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

@RegEntityRender(EntityNeedle.class)
public class RenderNeedle extends Render<EntityNeedle> {

    public RenderNeedle(RenderManager renderManager) {
        super(renderManager);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityNeedle entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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

        GlStateManager.color(0.5F, 0.5F, 0.5F, 0.9F);

        GlStateManager.rotate(45F, 1.0F, 0.0F, 0.0F);
        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        b.pos(-0.25D,  0.1D, 0).endVertex();
        b.pos(-0.25D, -0.1D, 0).endVertex();
        b.pos( 0.25D, -0.1D, 0).endVertex();
        b.pos( 0.25D,  0.1D, 0).endVertex();
        t.draw();
        GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        b.pos(-0.25D,  0.2D, 0).endVertex();
        b.pos(-0.25D, -0.2D, 0).endVertex();
        b.pos( 0.25D, -0.2D, 0).endVertex();
        b.pos( 0.25D,  0.2D, 0).endVertex();
        t.draw();

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();;
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNeedle p_110775_1_) {
        return null;
    }
}
