package cn.nulladev.exac.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityVolcanicBall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VolcanicBall extends Skill {

	public static final VolcanicBall INSTANCE = new VolcanicBall();

	private VolcanicBall() {
		super("volcanic_ball", 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, ContextVolcanicBall::new);
	}
	
	public static class ContextVolcanicBall extends Context {
		
		static final String MSG_PERFORM = "perform";
		
		private final float cp;

		public ContextVolcanicBall(EntityPlayer _player) {
			super(_player, VolcanicBall.INSTANCE);
			cp = MathUtils.lerpf(40, 100, ctx.getSkillExp());
		}
		
		private boolean consume() {
			float overload = MathUtils.lerpf(40, 30, ctx.getSkillExp());
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
				EntityVolcanicBall ball = new EntityVolcanicBall(world, player, ctx.getSkillExp(), player.getLookVec());
				world.spawnEntity(ball);
				ctx.addSkillExp(getExpIncr());
				ctx.setCooldown((int)MathUtils.lerpf(20, 5, ctx.getSkillExp()));
			}
		    terminate();
		}

		private float getExpIncr()  {
			return MathUtils.lerpf(0.002f, 0.001f, ctx.getSkillExp());
		}
			
	}

}