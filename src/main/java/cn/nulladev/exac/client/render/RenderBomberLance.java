package cn.nulladev.exac.client.render;

import org.lwjgl.opengl.GL11;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityBomberLance;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@RegEntityRender(EntityBomberLance.class)
public class RenderBomberLance extends Render<EntityBomberLance> {
	
	public RenderBomberLance(RenderManager renderManager) {
		super(renderManager);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void doRender(EntityBomberLance entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		if (entity == null)
			return;
		
		GlStateManager.pushMatrix();
		
        Vec3d startPos = entity.getStartPos();
        GlStateManager.translate(x - entity.prevPosX + startPos.x, y - entity.prevPosY + startPos.y, z - entity.prevPosZ + startPos.z);
        GlStateManager.rotate(entity.prevRotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.5, 0, 0);
                
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        
        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();
        
        GlStateManager.color(0.7F, 1F, 1F, 0.5F);
		
        double length = Math.sqrt((entity.prevPosX - startPos.x) * (entity.prevPosX - startPos.x) 
        		+ (entity.prevPosY - startPos.y) * (entity.prevPosY - startPos.y) 
        		+ (entity.prevPosZ - startPos.z) * (entity.prevPosZ - startPos.z));
        length = Math.max(0, length - 0.5);
        
        int gradation = 30;
        for (int i = 0; i < gradation; ++i) {
        	GlStateManager.rotate(360.0F / gradation, 1.0F, 0.0F, 0.0F);
        	GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        	b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
        	double w = 0.2D * Math.tan(Math.PI / gradation);
            b.pos(0.0D, 0.2D, w).endVertex();
            b.pos(length, 0.2D, w).endVertex();
            b.pos(length, 0.2D, -w).endVertex();
            b.pos(0.0D, 0.2D, -w).endVertex();
            t.draw();
        }

		GlStateManager.enableDepth();
        GlStateManager.disableBlend();;
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();

		GlStateManager.popMatrix();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityBomberLance p_110775_1_) {
		return null;
	}
}
