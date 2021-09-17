package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityThinker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OverloadThinking extends Skill {

    public static final OverloadThinking INSTANCE = new OverloadThinking();

    private OverloadThinking() {
        super("overload_thinking", 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        activateSingleKey2(rt, keyID, ContextOverloadThinking::new);
    }

    public static class ContextOverloadThinking extends Context {

        static final String MSG_PERFORM = "perform";

        public ContextOverloadThinking(EntityPlayer _player) {
            super(_player, OverloadThinking.INSTANCE);
        }

        @NetworkMessage.Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
        public void l_keydown()  {
            sendToServer(MSG_PERFORM);
        }

        @NetworkMessage.Listener(channel=MSG_PERFORM, side=Side.SERVER)
        public void s_perform()  {
            if(ctx.consume(100, 0)) {
                EntityThinker thinker = new EntityThinker(player.world, player);
                player.world.spawnEntity(thinker);
                float cp_recover = MathUtils.lerpf(1000, 2000, ctx.getSkillExp());
                float new_cp = Math.min(ctx.cpData.getMaxCP(), cp_recover + ctx.cpData.getCP());
                ctx.cpData.setCP(new_cp);
                ctx.addSkillExp(getExpIncr());
                ctx.setCooldown((int)MathUtils.lerpf(300, 60, ctx.getSkillExp()));
            }
            terminate();
        }

        private float getExpIncr()  {
            return MathUtils.lerpf(0.01f, 0.005f, ctx.getSkillExp());
        }

    }

}