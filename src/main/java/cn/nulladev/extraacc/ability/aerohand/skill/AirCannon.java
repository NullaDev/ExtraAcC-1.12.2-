package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.entity.EntityAirCannon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AirCannon extends Skill {

	public static final AirCannon INSTANCE = new AirCannon();

	private AirCannon() {
		super("air_cannon", 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, (EntityPlayer p) -> new ContextAirCannon(p));
	}
	
	public static class ContextAirCannon extends Context {
		
		static final String MSG_PERFORM = "perform";
		
		private float cp;

		public ContextAirCannon(EntityPlayer _player) {
			super(_player, AirCannon.INSTANCE);
			cp = MathUtils.lerpf(40, 160, ctx.getSkillExp());
		}
		
		private boolean consume() {
			float overload = MathUtils.lerpf(30, 20, ctx.getSkillExp());
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
				EntityAirCannon gun = new EntityAirCannon(world, player, ctx.getSkillExp(), player.getLookVec());
				world.spawnEntity(gun);
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