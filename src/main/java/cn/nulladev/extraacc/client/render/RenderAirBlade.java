package cn.nulladev.extraacc.client.render;

import org.lwjgl.opengl.GL11;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.extraacc.entity.EntityAirBlade;
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

@RegEntityRender(EntityAirBlade.class)
public class RenderAirBlade extends Render<EntityAirBlade> {
	
	public RenderAirBlade(RenderManager renderManager) {
		super(renderManager);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void doRender(EntityAirBlade entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		if (entity == null)
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.rotate(entity.prevRotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationPitch, 0.0F, 0.0F, 1.0F);
		
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GlStateManager.color(0.7F, 1F, 1F, 0.5F);
		
		Tessellator t = Tessellator.getInstance();
		BufferBuilder b = t.getBuffer();
		
		double[] r1 = new double[41], r2 = new double[41];
		for (int i = 0; i <= 40; i++) {
			double d = i * 0.1 - 2;
			r1[i] = Math.sqrt(6.25D - d * d) - 1.5;
			r2[i] = Math.sqrt(18.0625D - d * d) - 3.75;
		}
		
		b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
		for (int i = 0; i <= 40; i++) {
			double d = i * 0.1 - 2;
			b.pos(r1[i], -0.01, d).endVertex();
			b.pos(r2[i], -0.01, d).endVertex();
		}
		t.draw();
		
		b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
		for (int i = 0; i <= 40; i++) {
			double d = i * 0.1 - 2;
			b.pos(r1[i], 0.01, d).endVertex();
			b.pos(r2[i], 0.01, d).endVertex();
		}
		t.draw();
		
		b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
		for (int i = 0; i <= 40; i++) {
			double d = i * 0.1 - 2;
			b.pos(r1[i], -0.01, d).endVertex();
			b.pos(r1[i], 0.01, d).endVertex();
		}
		for (int i = 40; i >= 0; i--) {
			double d = i * 0.1 - 2;
			b.pos(r2[i], -0.01, d).endVertex();
			b.pos(r2[i], 0.01, d).endVertex();
		}
		t.draw();
		
	    GlStateManager.disableBlend();
	    GlStateManager.enableCull();
	    GlStateManager.enableLighting();
	    GlStateManager.enableTexture2D();
		
		GlStateManager.popMatrix();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityAirBlade p_110775_1_) {
		return null;
	}
}
