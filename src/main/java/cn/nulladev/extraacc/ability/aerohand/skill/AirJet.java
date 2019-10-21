package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AirJet extends Skill {

	public static final AirJet INSTANCE = new AirJet();

	private AirJet() {
		super("air_jet", 3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, (EntityPlayer p) -> new ContextAirJet(p));
	}
	
	public static class ContextAirJet extends Context {
		
		static final String MSG_PERFORM = "perform";
		static final String MSG_PERFORM2 = "perform2";
			
			private float cp;
			private float spd;

			public ContextAirJet(EntityPlayer _player) {
				super(_player, AirJet.INSTANCE);
				
				cp = MathUtils.lerpf(600, 900, ctx.getSkillExp());
				spd = MathUtils.lerpf(2, 4, ctx.getSkillExp());
			}
			
			private boolean consume() {
				float overload = MathUtils.lerpf(60, 40, ctx.getSkillExp());
				return ctx.consume(overload, cp);
			}
			
			@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
			public void l_keydown()  {
				sendToServer(MSG_PERFORM);
			}
			
			@Listener(channel=MSG_PERFORM, side=Side.SERVER)
			public void s_perform()  {
				if(consume()) {
					sendToClient(MSG_PERFORM2);
					player.fallDistance = 0;
					ctx.addSkillExp(getExpIncr());
					ctx.setCooldown((int)MathUtils.lerpf(40, 10, ctx.getSkillExp()));
				}
			    terminate();
			}
			
			@Listener(channel=MSG_PERFORM2, side=Side.CLIENT)
			public void s_perform2()  {
				Vec3d vec = player.getLookVec().normalize();
				double vx = spd * vec.x;
				double vy = spd * vec.y;
				double vz = spd * vec.z;
				player.addVelocity(vx, vy, vz);	
			    terminate();
			}

			private float getExpIncr()  {
				return MathUtils.lerpf(0.002f, 0.003f, ctx.getSkillExp());
			}

		}

}
