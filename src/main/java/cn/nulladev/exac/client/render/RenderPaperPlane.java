package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityPaperPlane;
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

@RegEntityRender(EntityPaperPlane.class)
public class RenderPaperPlane extends Render<EntityPaperPlane> {

    public RenderPaperPlane(RenderManager renderManager) {
        super(renderManager);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityPaperPlane entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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

        if (entity.isReinforced())
            GlStateManager.color(1F, 0.5F, 0.5F, 0.9F);
        else
            GlStateManager.color(1F, 1F, 1F, 0.9F);

        b.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        b.pos(0.5D, 0, 0).endVertex();
        b.pos(0, -0.05D, -0.25D).endVertex();
        b.pos(0, 0, -0.05D).endVertex();
        b.pos(0, -0.25D, 0).endVertex();
        b.pos(0, 0, 0.05D).endVertex();
        b.pos(0, -0.05D, 0.25D).endVertex();
        t.draw();

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();;
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPaperPlane p_110775_1_) {
        return null;
    }
}
