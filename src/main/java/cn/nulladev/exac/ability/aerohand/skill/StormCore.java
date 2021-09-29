package cn.nulladev.exac.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityStormCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StormCore extends Skill {

    public static final StormCore INSTANCE = new StormCore();

    private StormCore() {
        super("storm_core", 5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        activateSingleKey2(rt, keyID, ContextStormCore::new);
    }

    public static class ContextStormCore extends Context {

        static final String MSG_PERFORM = "perform";

        private final float cp;
        private final float overload;

        public ContextStormCore(EntityPlayer _player) {
            super(_player, StormCore.INSTANCE);
            cp = MathUtils.lerpf(3000, 2000, ctx.getSkillExp());
            overload = MathUtils.lerpf(300, 200, ctx.getSkillExp());
        }

        @NetworkMessage.Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
        public void l_keydown()  {
            sendToServer(MSG_PERFORM);
        }

        @NetworkMessage.Listener(channel=MSG_PERFORM, side=Side.SERVER)
        public void s_perform()  {
            if(ctx.consume(overload, cp)) {
                World world = player.world;
                EntityStormCore core = new EntityStormCore(world, player, ctx.getSkillExp());
                world.spawnEntity(core);
                ctx.addSkillExp(getExpIncr());
                ctx.setCooldown((int)MathUtils.lerpf(450, 300, ctx.getSkillExp()));
            }
            terminate();
        }

        private float getExpIncr()  {
            return MathUtils.lerpf(0.01f, 0.005f, ctx.getSkillExp());
        }

    }


}
