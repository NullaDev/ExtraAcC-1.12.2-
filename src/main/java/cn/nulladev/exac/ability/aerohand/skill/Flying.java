package cn.nulladev.exac.ability.aerohand.skill;

import java.util.Optional;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientContext;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.academy.ability.context.RegClientContext;
import cn.academy.ability.context.ClientRuntime.ActivateHandlers;
import cn.academy.ability.context.ClientRuntime.IActivateHandler;
import cn.academy.ability.context.Context.Status;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour.ContextOffenseArmour;
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

public class Flying extends Skill {

	public static final Flying INSTANCE = new Flying();

	private Flying() {
		super("flying", 4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
		    public Context apply(EntityPlayer p) {
		        return new ContextFlying(p);
		    }
		};
		ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextFlying.class);
		rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
	}
	
	public static class ContextFlying extends Context {
				
		private boolean prevAllowFlying = false; 

		public ContextFlying(EntityPlayer _player) {
			super(_player, Flying.INSTANCE);
		}
		
		@Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
		private void s_madeAlive() {
			float overload = MathUtils.lerpf(80, 50, ctx.getSkillExp());
			ctx.consume(overload, 0);		
			prevAllowFlying = player.capabilities.allowFlying;
			player.capabilities.allowFlying = true;
			player.capabilities.isFlying = true;
			player.sendPlayerAbilities();
			//关闭龟壳盾
			Optional<ContextOffenseArmour> context = ContextManager.instance.find(ContextOffenseArmour.class);
			if(context.isPresent() && context.get().getStatus() == Status.ALIVE) {
				context.get().terminate();
			}
			//特效暂时没有
			//EntityOffenseArmour armor = new EntityOffenseArmour(player.world, player);
			//player.world.spawnEntity(armor);
		}
		
		@Listener(channel=MSG_TICK, side=Side.SERVER)
		private void s_tick() {
			float cp = MathUtils.lerpf(16, 8, ctx.getSkillExp());
			if(ctx.consume(0.5F, cp)) {
				if (ctx.getSkillExp() >= 0.5F)
					player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("speed"), 39));
				ctx.addSkillExp(0.0001f);
			}
			else
				terminate();
		}
		
		@Listener(channel=MSG_TERMINATED, side=Side.SERVER)
		private void s_terminate() {
			player.capabilities.allowFlying = prevAllowFlying;
			player.capabilities.isFlying = prevAllowFlying;
			player.sendPlayerAbilities();
			ctx.setCooldown(20);
		}
		
		
	}
		
	@SideOnly(Side.CLIENT)
	@RegClientContext(ContextFlying.class)
	public static class ContextFlyingC extends ClientContext {
		
		private IActivateHandler activateHandler;
		  
		public ContextFlyingC(ContextFlying ctx) {
			super(ctx);
		}

		@Listener(channel=MSG_MADEALIVE, side=Side.CLIENT)
		private void l_alive() {
			if (isLocal()) {
				activateHandler = ActivateHandlers.terminatesContext(this.parent);
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
