package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.entity.EntityOffenseArmour;
import net.minecraft.entity.player.EntityPlayer;
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
			player.getEntityData().setBoolean("offense_armour", true);
			EntityOffenseArmour armor = new EntityOffenseArmour(player.world, player);
			player.world.spawnEntity(armor);
		}
		
		@Listener(channel=MSG_TICK, side=Side.SERVER)
		private void s_tick() {
			float tick_cp = MathUtils.lerpf(20, 10, ctx.getSkillExp());
			if(!ctx.consume(1, tick_cp))
				terminate();
		}
		
		@Listener(channel=MSG_TERMINATED, side=Side.SERVER)
		private void s_terminate() {
			player.getEntityData().setBoolean("offense_armour", false);
		}
	
	}
}
