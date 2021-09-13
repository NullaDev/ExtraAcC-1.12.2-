package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityCobblestone;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@RegEntityRender(EntityCobblestone.class)
public class RenderCobblestone extends Render<EntityCobblestone> {

    public RenderCobblestone(RenderManager renderManager) {
        super(renderManager);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityCobblestone entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        if (entity == null)
            return;

        this.bindEntityTexture(entity);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.rotate(entity.prevRotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(entity.ticksExisted * 3, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(entity.ticksExisted * 3, 0.0F, 1.0F, 0.0F);

        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GlStateManager.disableDepth();

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        for (int i = 0; i < 4; ++i) {
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            b.pos(-0.5D, -0.5D, 0.5D).tex(0, 1).endVertex();
            b.pos( 0.5D, -0.5D, 0.5D).tex(1, 1).endVertex();
            b.pos( 0.5D,  0.5D, 0.5D).tex(1, 0).endVertex();
            b.pos(-0.5D,  0.5D, 0.5D).tex(0, 0).endVertex();
            t.draw();
        }

        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        for (int i = 0; i < 2; ++i) {
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            b.pos(-0.5D, -0.5D, 0.5D).tex(0, 1).endVertex();
            b.pos( 0.5D, -0.5D, 0.5D).tex(1, 1).endVertex();
            b.pos( 0.5D,  0.5D, 0.5D).tex(1, 0).endVertex();
            b.pos(-0.5D,  0.5D, 0.5D).tex(0, 0).endVertex();
            t.draw();
        }

        //GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }


    @Override
    protected ResourceLocation getEntityTexture(EntityCobblestone entity) {
        return new ResourceLocation("minecraft", "textures/blocks/cobblestone.png");
    }

}
