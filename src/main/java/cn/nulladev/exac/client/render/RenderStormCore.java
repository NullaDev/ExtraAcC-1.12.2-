package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityStormCore;
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

@RegEntityRender(EntityStormCore.class)
public class RenderStormCore extends Render<EntityStormCore> {

    public RenderStormCore(RenderManager renderManager) {
        super(renderManager);
    }

    private static float getR1(int tick) {
        if (tick < EntityStormCore.EXPLOSION_TIME) {
            return MathUtils.lerpf(0.2F, 1, tick/EntityStormCore.EXPLOSION_TIME);
        } else {
            tick -= EntityStormCore.EXPLOSION_TIME;
            return MathUtils.lerpf(1, 8, tick/(EntityStormCore.AGE - EntityStormCore.EXPLOSION_TIME));
        }
    }

    private static float getA1(int tick) {
        if (tick < EntityStormCore.EXPLOSION_TIME) {
            return MathUtils.lerpf(0.1F, 0.5F, tick/EntityStormCore.EXPLOSION_TIME);
        } else {
            tick -= EntityStormCore.EXPLOSION_TIME;
            return MathUtils.lerpf(0.5F, 0F, tick/(EntityStormCore.AGE - EntityStormCore.EXPLOSION_TIME));
        }
    }

    private static float getR2(int tick) {
        float p = 0;
        if (tick < 24) {
            p = tick/24F;
        } else if (tick < 44) {
            p = (tick-24)/20F;
        } else if (tick < 60) {
            p = (tick-44)/16F;
        } else if (tick < 72) {
            p = (tick-60)/12F;
        } else if (tick < 80) {
            p = (tick-72)/8F;
        } else {
            return 0;
        }
        return MathUtils.lerpf(8F, 0, p);
    }

    private static float getA2(int tick) {
        float p = 0;
        if (tick < 24) {
            p = tick/24F;
        } else if (tick < 44) {
            p = (tick-24)/20F;
        } else if (tick < 60) {
            p = (tick-44)/16F;
        } else if (tick < 72) {
            p = (tick-60)/12F;
        } else if (tick < 80) {
            p = (tick-72)/8F;
        } else {
            return 0;
        }
        return MathUtils.lerpf(0.1F, 0.2F, p);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void doRender(EntityStormCore entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
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

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        float r = getR1(entity.ticksExisted);
        GlStateManager.color(0.7F, 1F, 1F, getA1(entity.ticksExisted));

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

        r = getR2(entity.ticksExisted);
        GlStateManager.color(0.7F, 1F, 1F, getA2(entity.ticksExisted));

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
    protected ResourceLocation getEntityTexture(EntityStormCore p_110775_1_) {
        return null;
    }
}
