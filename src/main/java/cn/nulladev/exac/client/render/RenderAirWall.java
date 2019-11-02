package cn.nulladev.exac.client.render;

import org.lwjgl.opengl.GL11;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityAirWall;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@RegEntityRender(EntityAirWall.class)
public class RenderAirWall extends Render<EntityAirWall> {
	
	public RenderAirWall(RenderManager renderManager) {
		super(renderManager);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void doRender(EntityAirWall entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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
		
		float a = MathUtils.lerpf(0.5F, 0.1F, entity.getCurrentRadius() / entity.getMaxRadius());
		GlStateManager.color(0.7F, 1F, 1F, a);
		
		Tessellator t = Tessellator.getInstance();
		BufferBuilder b = t.getBuffer();
		
		b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
		for (int i = 0; i <= 30; i++) {
			double theta = 2 * Math.PI * i / 30;
			double r = entity.getCurrentRadius();
			b.pos(r * Math.sin(theta), -0.5, r * Math.cos(theta)).endVertex();
			b.pos(r * Math.sin(theta), 2.5, r * Math.cos(theta)).endVertex();
		}
		t.draw();
		
		GlStateManager.enableDepth();
	    GlStateManager.disableBlend();
	    GlStateManager.enableCull();
	    GlStateManager.enableLighting();
	    GlStateManager.enableTexture2D();
		
		GlStateManager.popMatrix();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityAirWall p_110775_1_) {
		return null;
	}
}
