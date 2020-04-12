package cn.nulladev.exac.ability.aerohand.skill;

import java.util.Optional;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientContext;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.ClientRuntime.ActivateHandlers;
import cn.academy.ability.context.ClientRuntime.IActivateHandler;
import cn.academy.ability.context.Context.Status;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.academy.ability.context.RegClientContext;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.academy.datapart.AbilityData;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.Flying.ContextFlying;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour.ContextOffenseArmour;
import cn.nulladev.exac.entity.EntityOffenseArmour;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Function1;
import scala.reflect.ClassTag;
import scala.runtime.AbstractFunction1;

public class OffenseArmour extends Skill {

	public static final OffenseArmour INSTANCE = new OffenseArmour();

	private OffenseArmour() {
		super("offense_armour", 4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
		    public Context apply(EntityPlayer p) {
		        return new ContextOffenseArmour(p);
		    }
		};
		ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextOffenseArmour.class);
		rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
	}
	
	public static class ContextOffenseArmour extends Context {
		
		public static final AttributeModifier AM = new AttributeModifier("Offense Armour", 0.9F, 0);
				
		public ContextOffenseArmour(EntityPlayer _player) {
			super(_player, OffenseArmour.INSTANCE);
		}
		
		@Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
		private void s_madeAlive() {
			float overload = MathUtils.lerpf(80, 50, ctx.getSkillExp());
			float cp = MathUtils.lerpf(600, 400, ctx.getSkillExp());
			ctx.consume(overload, cp);
			//关闭飞行
			Optional<ContextFlying> context = ContextManager.instance.find(ContextFlying.class);
			if(context.isPresent() && context.get().getStatus() == Status.ALIVE) {
				context.get().terminate();
			}
			EntityOffenseArmour armor = new EntityOffenseArmour(player.world, player);
			player.world.spawnEntity(armor);
			MinecraftForge.EVENT_BUS.register(this);
			player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(AM);
		}
		
		@Listener(channel=MSG_TICK, side=Side.SERVER)
		private void s_tick() {
			if(ctx.consume(0.5F, 1F)) {
				int level = ctx.getSkillExp() >= 0.5F? 3:4;
				player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 39, level));
				ctx.addSkillExp(0.0001f);
			}
			else
				terminate();
		}
		
		@Listener(channel=MSG_TERMINATED, side=Side.SERVER)
		private void s_terminate() {
			player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(AM);
			int cd = (int) MathUtils.lerpf(200, 100, ctx.getSkillExp());
			ctx.setCooldown(cd);
			MinecraftForge.EVENT_BUS.unregister(this);
		}
		
		@SubscribeEvent
	    public void damage(LivingHurtEvent event) {
			if (!(event.getEntity() instanceof EntityPlayer)) {
				return;
			}
			String type = event.getSource().getDamageType();
			if (type.equals("fall") || type.equals("inWall") || type.equals("drown")) {
				return;
			}
			EntityPlayer player = (EntityPlayer) event.getEntity();
			Optional<ContextOffenseArmour> context = ContextManager.instance.find(ContextOffenseArmour.class);
			if(context.isPresent()) {
				if (!event.getSource().isUnblockable()) {
					float dmg = event.getAmount();
					if (ctx.consume(0, dmg * 20)) {
						float p = MathUtils.lerpf(0.1F, 0.05F, ctx.getSkillExp());
						event.setAmount(dmg * p);
						ctx.addSkillExp(dmg * 0.0005f);
					} else {
						terminate();
					}
				}
	        }
	    }
	}
		
	@SideOnly(Side.CLIENT)
	@RegClientContext(ContextOffenseArmour.class)
	public static class ContextOffenseArmourC extends ClientContext {
		
		private IActivateHandler activateHandler;
		  
		public ContextOffenseArmourC(ContextOffenseArmour ctx) {
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
