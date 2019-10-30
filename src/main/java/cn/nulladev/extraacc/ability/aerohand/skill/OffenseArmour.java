package cn.nulladev.extraacc.ability.aerohand.skill;

import java.util.Optional;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.academy.datapart.AbilityData;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.entity.EntityOffenseArmour;
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
		ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(Context.class);
		rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
	}
	
	public static class ContextOffenseArmour extends Context {
				
		private float cp;

		public ContextOffenseArmour(EntityPlayer _player) {
			super(_player, OffenseArmour.INSTANCE);
			
			cp = MathUtils.lerpf(500, 400, ctx.getSkillExp());
		}
		
		@Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
		private void s_madeAlive() {
			float overload = MathUtils.lerpf(80, 50, ctx.getSkillExp());
			ctx.consume(overload, 0);
			//player.getEntityData().setBoolean("offense_armour", true);
			EntityOffenseArmour armor = new EntityOffenseArmour(player.world, player);
			player.world.spawnEntity(armor);
			MinecraftForge.EVENT_BUS.register(this);
		}
		
		@Listener(channel=MSG_TICK, side=Side.SERVER)
		private void s_tick() {
			if(ctx.consume(0.5F, 1F)) {
				player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 39, 3));
				ctx.addSkillExp(0.0001f);
			}
			else
				terminate();
		}
		
		@Listener(channel=MSG_TERMINATED, side=Side.SERVER)
		private void s_terminate() {
			MinecraftForge.EVENT_BUS.unregister(this);
			//player.getEntityData().setBoolean("offense_armour", false);
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
						float p = MathUtils.lerpf(0.9F, 0.98F, ctx.getSkillExp());
						event.setAmount(dmg * p);
						ctx.addSkillExp(dmg * 0.0005f);
					} else {
						terminate();
					}
				}
	        }
			
	    }
	
	}

}
