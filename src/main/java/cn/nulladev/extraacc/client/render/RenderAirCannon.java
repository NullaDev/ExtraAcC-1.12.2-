package cn.nulladev.extraacc.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.entity.EntityAirCannon;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@RegEntityRender(EntityAirCannon.class)
public class RenderAirCannon extends Render<EntityAirCannon> {
	
	public RenderAirCannon(RenderManager renderManager) {
		super(renderManager);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void doRender(EntityAirCannon entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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
		
		float a = MathUtils.lerpf(0.5F, 0.1F, (float)entity.ticksExisted / 80);
		GlStateManager.color(0.7F, 1F, 1F, a);
		
		Tessellator t = Tessellator.getInstance();
		BufferBuilder b = t.getBuffer();
		
		float r = entity.width / 2;
		
	    int gradation = 30;
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
	    GlStateManager.enableCull();
	    GlStateManager.enableLighting();
	    GlStateManager.enableTexture2D();
		
		GlStateManager.popMatrix();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityAirCannon p_110775_1_) {
		return null;
	}
}
