package cn.nulladev.exac.ability.aerohand.skill;

import java.util.Optional;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour.ContextOffenseArmour;
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
		activateSingleKey2(rt, keyID, ContextAirJet::new);
	}
	
	public static class ContextAirJet extends Context {
		
		static final String MSG_PERFORM = "perform";
		static final String MSG_PERFORM2 = "perform2";
			
			private final float cp;
			private final float spd;

			public ContextAirJet(EntityPlayer _player) {
				super(_player, AirJet.INSTANCE);
				
				cp = MathUtils.lerpf(200, 400, ctx.getSkillExp());
				spd = MathUtils.lerpf(2, 4, ctx.getSkillExp());
			}
			
			private boolean consume() {
				float overload = MathUtils.lerpf(80, 40, ctx.getSkillExp());
				return ctx.consume(overload, cp);
			}
			
			@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
			public void l_keydown()  {
				sendToServer(MSG_PERFORM);
			}
			
			@Listener(channel=MSG_PERFORM, side=Side.SERVER)
			public void s_perform() {
				Optional<ContextOffenseArmour> context = ContextManager.instance.find(ContextOffenseArmour.class);
				if(context.isPresent() && context.get().player == this.player && context.get().getStatus() == Status.ALIVE) {
					return;
				}
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
