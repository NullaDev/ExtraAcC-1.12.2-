package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.EntitySelectors;
import cn.lambdalib2.util.MathUtils;
import cn.lambdalib2.util.Raytrace;
import cn.nulladev.exac.entity.EntityBonder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PsychoSlam extends Skill {
    public static final PsychoSlam INSTANCE = new PsychoSlam();

    private PsychoSlam() {
        super("psycho_slam", 4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        activateSingleKey2(rt, keyID, ContextPsychoSlam::new);
    }

    public static class ContextPsychoSlam extends Context {

        static final String MSG_PERFORM = "perform";

        private final float cp;
        private final float overload;

        public ContextPsychoSlam(EntityPlayer _player) {
            super(_player, PsychoSlam.INSTANCE);
            cp = MathUtils.lerpf(3000, 2000, ctx.getSkillExp());
            overload = MathUtils.lerpf(180, 120, ctx.getSkillExp());
        }

        @NetworkMessage.Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
        public void l_keydown()  {
            sendToServer(MSG_PERFORM);
        }

        @NetworkMessage.Listener(channel=MSG_PERFORM, side=Side.SERVER)
        public void s_perform()  {
            float range = MathUtils.lerpf(4, 8, ctx.getSkillExp());
            Entity target = Raytrace.traceLiving(player, range, EntitySelectors.living()).entityHit;
            if (!(target instanceof EntityLivingBase)) {
                return;
            }
            if(ctx.consume(overload, cp)) {
                World world = player.world;
                Vec3d direc = new Vec3d(player.getLookVec().x, 0, player.getLookVec().z).normalize();
                EntityBonder bonder = new EntityBonder(world, player, (EntityLivingBase)target, ctx.getSkillExp(), direc);
                world.spawnEntity(bonder);
                ctx.addSkillExp(0.001F);
                ctx.setCooldown((int)MathUtils.lerpf(200, 100, ctx.getSkillExp()));
            }
            terminate();
        }

    }

}
