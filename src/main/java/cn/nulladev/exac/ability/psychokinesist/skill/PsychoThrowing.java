package cn.nulladev.exac.ability.psychokinesist.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityCobblestone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PsychoThrowing extends Skill {

    public static final PsychoThrowing INSTANCE = new PsychoThrowing();

    private PsychoThrowing() {
        super("psycho_throwing", 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        activateSingleKey2(rt, keyID, ContextPsychoThrowing::new);
    }

    public static class ContextPsychoThrowing extends Context {
        static final String MSG_PERFORM = "perform";

        private final float cp;

        public ContextPsychoThrowing(EntityPlayer _player) {
            super(_player, PsychoThrowing.INSTANCE);
            cp = MathUtils.lerpf(300, 600, ctx.getSkillExp());
        }

        private boolean consume() {
            float overload = 20F;
            return ctx.consume(overload, cp);
        }

        @NetworkMessage.Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
        public void l_keydown()  {
            sendToServer(MSG_PERFORM);
        }

        @NetworkMessage.Listener(channel=MSG_PERFORM, side=Side.SERVER)
        public void s_perform()  {
            if(consume()) {
                World world = player.world;
                EntityCobblestone stone = new EntityCobblestone(world, player, ctx.getSkillExp(), player.getLookVec(), false);
                world.spawnEntity(stone);
                ctx.addSkillExp(getExpIncr());
                ctx.setCooldown((int)MathUtils.lerpf(40, 20, ctx.getSkillExp()));
            }
            terminate();
        }

        private float getExpIncr()  {
            return MathUtils.lerpf(0.002f, 0.001f, ctx.getSkillExp());
        }
    }
}
