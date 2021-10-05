package cn.nulladev.exac.client.render;

import cn.lambdalib2.registry.mc.RegEntityRender;
import cn.nulladev.exac.entity.EntityWaterman;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

@RegEntityRender(EntityWaterman.class)
public class RenderWaterman extends RenderLiving<EntityWaterman> {

    public RenderWaterman(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelZombie(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaterman entity) {
        return new ResourceLocation("exac:textures/entities/waterman.png");
    }
}
