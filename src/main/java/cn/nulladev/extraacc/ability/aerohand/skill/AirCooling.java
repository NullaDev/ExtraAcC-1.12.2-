package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AirCooling extends Skill {

	public static final AirCooling INSTANCE = new AirCooling();

	private AirCooling() {
		super("air_cooling", 3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, (EntityPlayer p) -> new ContextAirCooling(p));
	}
	
	public static class ContextAirCooling extends Context {
		
		static final String MSG_PERFORM = "perform";
			
			private float cp;

			public ContextAirCooling(EntityPlayer _player) {
				super(_player, AirCooling.INSTANCE);
				
				cp = MathUtils.lerpf(100, 2000, ctx.getSkillExp());
			}
			
			private boolean consume() {
				return ctx.consume(0, cp);
			}
			
			@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
			public void l_keydown()  {
				sendToServer(MSG_PERFORM);
			}
			
			@Listener(channel=MSG_PERFORM, side=Side.SERVER)
			public void s_perform()  {
				if(consume()) {
					float new_overload;
					if (player.world.provider.isNether()) {
						new_overload = Math.min(ctx.cpData.getOverload(), ctx.cpData.getMaxOverload() / 2);
					} else {
						float f = MathUtils.lerpf(50, 500, ctx.getSkillExp());
						new_overload = Math.max(ctx.cpData.getOverload() - f, 0);
						player.extinguish();
					}
					ctx.cpData.setOverload(new_overload);
					ctx.addSkillExp(getExpIncr());
					ctx.setCooldown((int)MathUtils.lerpf(800, 100, ctx.getSkillExp()));
				}
			    terminate();
			}

			private float getExpIncr()  {
				return MathUtils.lerpf(0.001f, 0.002f, ctx.getSkillExp());
			}

		}


}
