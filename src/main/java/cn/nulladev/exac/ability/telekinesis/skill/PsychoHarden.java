package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.*;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityShell;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Function1;
import scala.reflect.ClassTag;
import scala.runtime.AbstractFunction1;

import java.util.Optional;

public class PsychoHarden extends Skill {

    public static final PsychoHarden INSTANCE = new PsychoHarden();

    private PsychoHarden() {
        super("psycho_harden", 4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
            public Context apply(EntityPlayer p) {
                return new ContextPsychoHarden(p);
            }
        };
        ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextPsychoHarden.class);
        rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
    }

    public static class ContextPsychoHarden extends Context {

        private double prevSpeed = 0.1D;

        public ContextPsychoHarden(EntityPlayer _player) {
            super(_player, PsychoHarden.INSTANCE);
        }

        @NetworkMessage.Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
        private void s_madeAlive() {
            prevSpeed = ctx.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            ctx.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
            float overload = MathUtils.lerpf(100, 50, ctx.getSkillExp());
            float cp = MathUtils.lerpf(1500, 1000, ctx.getSkillExp());
            ctx.consume(overload, cp);
            EntityShell shell = new EntityShell(player.world, player);
            player.world.spawnEntity(shell);
            MinecraftForge.EVENT_BUS.register(this);
        }

        @NetworkMessage.Listener(channel=MSG_TICK, side=Side.SERVER)
        private void s_tick() {
            if(ctx.consume(0F, 2F)) {
                player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 19, 9));
                ctx.addSkillExp(0.00001f);
            } else {
                terminate();
            }
        }

        @NetworkMessage.Listener(channel=MSG_TERMINATED, side=Side.SERVER)
        private void s_terminate() {
            ctx.setCooldown(160);
            player.removePotionEffect(Potion.getPotionFromResourceLocation("slowness"));
            player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 79, 4));
            MinecraftForge.EVENT_BUS.unregister(this);
        }

        @SubscribeEvent
        public void damage(LivingHurtEvent event) {
            if (!(event.getEntity() instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) event.getEntity();
            Optional<ContextPsychoHarden> context = ContextManager.instance.find(ContextPsychoHarden.class);
            if(context.isPresent() && context.get().player == player) {
                float dmg = event.getAmount();
                float cp_per_dmg = MathUtils.lerpf(60, 30, ctx.getSkillExp());
                if (ctx.consume(dmg * 0.1F, dmg * cp_per_dmg)) {
                    ctx.addSkillExp(dmg * 0.0005f);
                    event.setCanceled(true);
                } else {
                    terminate();
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    @RegClientContext(ContextPsychoHarden.class)
    public static class ContextPsychoHardenC extends ClientContext {

        private ClientRuntime.IActivateHandler activateHandler;

        public ContextPsychoHardenC(ContextPsychoHarden ctx) {
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
