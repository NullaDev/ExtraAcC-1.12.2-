package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.*;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityBombController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Function1;
import scala.reflect.ClassTag;
import scala.runtime.AbstractFunction1;

public class CruiseBomb extends Skill {

    public static final CruiseBomb INSTANCE = new CruiseBomb();

    private CruiseBomb() {
        super("cruise_bomb", 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
            public Context apply(EntityPlayer p) {
                return new ContextCruiseBomb(p);
            }
        };
        ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextCruiseBomb.class);
        rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
    }

    public static class ContextCruiseBomb extends Context {

        public ContextCruiseBomb(EntityPlayer _player) {
            super(_player, CruiseBomb.INSTANCE);
        }

        @NetworkMessage.Listener(channel = MSG_MADEALIVE, side = Side.SERVER)
        private void s_madeAlive() {
            ctx.consume(20, 200);
            EntityBombController controller = new EntityBombController(player.world, player);
            player.world.spawnEntity(controller);
        }

        @NetworkMessage.Listener(channel = MSG_TICK, side = Side.SERVER)
        private void s_tick() {
            float cp = MathUtils.lerpf(2, 1, ctx.getSkillExp());
            if (ctx.consume(0.05F, cp)) {
                ctx.addSkillExp(0.00002f);
            } else
                terminate();
        }

        @NetworkMessage.Listener(channel = MSG_TERMINATED, side = Side.SERVER)
        private void s_terminate() {
            ctx.setCooldown(40);
        }

    }

    @SideOnly(Side.CLIENT)
    @RegClientContext(ContextCruiseBomb.class)
    public static class ContextCruiseBombC extends ClientContext {

        private ClientRuntime.IActivateHandler activateHandler;

        public ContextCruiseBombC(ContextCruiseBomb ctx) {
            super(ctx);
        }

        @NetworkMessage.Listener(channel=MSG_MADEALIVE, side=Side.CLIENT)
        private void l_alive() {
            if (isLocal()) {
                activateHandler = ClientRuntime.ActivateHandlers.terminatesContext(this.parent);
                ClientRuntime.instance().addActivateHandler(activateHandler);
            }
        }

        @NetworkMessage.Listener(channel=MSG_TERMINATED, side=Side.CLIENT)
        private void l_terminate() {
            if (isLocal()) {
                ClientRuntime.instance().removeActiveHandler(activateHandler);
            }
        }
    }

}