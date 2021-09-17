package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.*;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.EntitySelectors;
import cn.lambdalib2.util.MathUtils;
import cn.lambdalib2.util.Raytrace;
import cn.lambdalib2.util.WorldUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Function1;
import scala.reflect.ClassTag;
import scala.runtime.AbstractFunction1;

import java.util.List;

public class PsychoTransmission extends Skill {

    public static final PsychoTransmission INSTANCE = new PsychoTransmission();

    private PsychoTransmission() {
        super("psycho_transmission", 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
            public Context apply(EntityPlayer p) {
                return new ContextPsychoTransmission(p);
            }
        };
        ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextPsychoTransmission.class);
        rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
    }

    public static class ContextPsychoTransmission extends Context {

        public ContextPsychoTransmission(EntityPlayer _player) {
            super(_player, PsychoTransmission.INSTANCE);
        }

        @NetworkMessage.Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
        private void s_madeAlive() {
            ctx.consume(5F, 0F);
        }

        @NetworkMessage.Listener(channel=MSG_TICK, side=Side.SERVER)
        private void s_tick() {
            float cp_per_dist = MathUtils.lerpf(4F, 2F, ctx.getSkillExp());
            if(ctx.consume(0F, 0.5F)) {
                float range = MathUtils.lerpf(8, 12, ctx.getSkillExp());
                Vec3d start = player.getPositionEyes(1);
                Vec3d end = Raytrace.traceLiving(player, range, EntitySelectors.nothing()).hitVec;
                AxisAlignedBB boundingBox = WorldUtils.getBoundingBox(start, end);
                List<EntityItem> list = player.world.getEntitiesWithinAABB(EntityItem.class, boundingBox.expand(1.0D, 1.0D, 1.0D));
                if (list.size() > 0) {
                    EntityItem entityitem = list.get(0);
                    float dist = (float)(Math.pow(start.x-entityitem.posX,2) + Math.pow(start.y-entityitem.posY,2) + Math.pow(start.z-entityitem.posZ,2));
                    if (ctx.consume(2.5F, cp_per_dist * dist)) {
                        if(player.inventory.addItemStackToInventory(entityitem.getItem())) {
                            entityitem.setDead();
                            ctx.addSkillExp(dist/50000F);
                        }  else {
                            entityitem.setPosition(player.posX, player.posY, player.posZ);
                        }
                    }
                }
            }
            else
                terminate();
        }

        @NetworkMessage.Listener(channel=MSG_TERMINATED, side=Side.SERVER)
        private void s_terminate() {
            ctx.setCooldown(20);
        }

    }

    @SideOnly(Side.CLIENT)
    @RegClientContext(ContextPsychoTransmission.class)
    public static class ContextPsychoTransmissionC extends ClientContext {

        private ClientRuntime.IActivateHandler activateHandler;

        public ContextPsychoTransmissionC(ContextPsychoTransmission ctx) {
            super(ctx);
        }

        @Listener(channel=MSG_MADEALIVE, side=Side.CLIENT)
        private void l_alive() {
            if (isLocal()) {
                activateHandler = ClientRuntime.ActivateHandlers.terminatesContext(this.parent);
                ClientRuntime.instance().addActivateHandler(activateHandler);
            }
        }

        @Listener(channel=MSG_TERMINATED, side=Side.CLIENT)
        private void l_terminate() {
            if (isLocal()) {
                ClientRuntime.instance().removeActiveHandler(activateHandler);
            }
        }
    }

}
