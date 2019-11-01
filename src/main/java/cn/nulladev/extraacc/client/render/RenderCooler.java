package cn.nulladev.extraacc.client.render;

import org.lwjgl.opengl.GL11;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.extraacc.entity.EntityCooler;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@RegEntityRender(EntityCooler.class)
public class RenderCooler extends Render<EntityCooler> {
	
	public RenderCooler(RenderManager renderManager) {
		super(renderManager);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void doRender(EntityCooler entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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
		
		GlStateManager.color(0.7F, 1F, 1F, 0.5F);
		
		Tessellator t = Tessellator.getInstance();
		BufferBuilder b = t.getBuffer();
		
		b.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		b.pos(0, entity.h, 0);
		for (int i = 0; i <= 30; i++) {
			double theta = 2 * Math.PI * i / 30;
			b.pos(Math.sin(theta), entity.h, Math.cos(theta)).endVertex();
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
	protected ResourceLocation getEntityTexture(EntityCooler p_110775_1_) {
		return null;
	}
}
