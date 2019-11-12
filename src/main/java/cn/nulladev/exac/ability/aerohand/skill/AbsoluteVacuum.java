package cn.nulladev.exac.ability.aerohand.skill;

import java.util.Optional;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.academy.ability.context.Context.Status;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.Flying.ContextFlying;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour.ContextOffenseArmour;
import cn.nulladev.exac.entity.EntityAirCannon;
import cn.nulladev.exac.entity.EntityVacuum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AbsoluteVacuum extends Skill {
	public static final AbsoluteVacuum INSTANCE = new AbsoluteVacuum();

	private AbsoluteVacuum() {
		super("absolute_vacuum", 5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, (EntityPlayer p) -> new ContextAbsoluteVacuum(p));
	}
	
	public static class ContextAbsoluteVacuum extends Context {
		
		static final String MSG_PERFORM = "perform";
		
		private float cp;

		public ContextAbsoluteVacuum(EntityPlayer _player) {
			super(_player, AbsoluteVacuum.INSTANCE);
			
			cp = MathUtils.lerpf(1200, 1800, ctx.getSkillExp());
		}
		
		private boolean consume() {
			float overload = MathUtils.lerpf(480, 320, ctx.getSkillExp());
			return ctx.consume(overload, cp);
		}
		
		@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
		public void l_keydown()  {
			sendToServer(MSG_PERFORM);
		}
		
		@Listener(channel=MSG_PERFORM, side=Side.SERVER)
		public void s_perform()  {
			if(consume()) {
				World world = player.world;
				//关闭飞行
				Optional<ContextFlying> context = ContextManager.instance.find(ContextFlying.class);
				if(context.isPresent() && context.get().getStatus() == Status.ALIVE) {
					context.get().terminate();
				}
				//关闭龟壳盾
				Optional<ContextOffenseArmour> context2 = ContextManager.instance.find(ContextOffenseArmour.class);
				if(context2.isPresent() && context2.get().getStatus() == Status.ALIVE) {
					context2.get().terminate();
				}
				EntityVacuum vacuum = new EntityVacuum(world, player, ctx.getSkillExp());
				world.spawnEntity(vacuum);
				ctx.addSkillExp(getExpIncr());
				ctx.setCooldown((int)MathUtils.lerpf(480, 320, ctx.getSkillExp()));
			}
		    terminate();
		}

		private float getExpIncr()  {
			return MathUtils.lerpf(0.002f, 0.001f, ctx.getSkillExp());
		}
			
	}

}
