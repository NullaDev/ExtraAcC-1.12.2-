package cn.nulladev.exac.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityCooler;
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
		activateSingleKey2(rt, keyID, ContextAirCooling::new);
	}
	
	public static class ContextAirCooling extends Context {
		
		static final String MSG_PERFORM = "perform";
			
			private final float cp;

			public ContextAirCooling(EntityPlayer _player) {
				super(_player, AirCooling.INSTANCE);
				cp = MathUtils.lerpf(400, 1600, ctx.getSkillExp());
			}
			
			@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
			public void l_keydown()  {
				sendToServer(MSG_PERFORM);
			}
			
			@Listener(channel=MSG_PERFORM, side=Side.SERVER)
			public void s_perform()  {
				if(ctx.consume(0, cp)) {
					EntityCooler cooler = new EntityCooler(player.world, player);
					player.world.spawnEntity(cooler);
					float capacity = MathUtils.lerpf(200, 800, ctx.getSkillExp());
					if (player.world.provider.isNether()) {
						capacity /= 4;
					} else {
						player.extinguish();
					}
					float new_overload = Math.max(ctx.cpData.getOverload() - capacity, 0);
					ctx.cpData.setOverload(new_overload);
					ctx.addSkillExp(getExpIncr());
					ctx.setCooldown((int)MathUtils.lerpf(300, 60, ctx.getSkillExp()));
				}
			    terminate();
			}

			private float getExpIncr()  {
				return MathUtils.lerpf(0.002f, 0.001f, ctx.getSkillExp());
			}

		}

}
