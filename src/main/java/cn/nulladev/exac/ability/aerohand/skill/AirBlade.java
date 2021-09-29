package cn.nulladev.exac.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityAirBlade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AirBlade extends Skill {

	public static final AirBlade INSTANCE = new AirBlade();

	private AirBlade() {
		super("air_blade", 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, ContextAirBlade::new);
	}
	
	public static class ContextAirBlade extends Context {
		
		static final String MSG_PERFORM = "perform";
		
		private final float cp;
		private final float overload;

		public ContextAirBlade(EntityPlayer _player) {
			super(_player, AirBlade.INSTANCE);
			cp = MathUtils.lerpf(100, 150, ctx.getSkillExp());
			overload = MathUtils.lerpf(60, 40, ctx.getSkillExp());
		}
		
		@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
		public void l_keydown()  {
			sendToServer(MSG_PERFORM);
		}
		
		@Listener(channel=MSG_PERFORM, side=Side.SERVER)
		public void s_perform()  {
			if(ctx.consume(overload, cp)) {
				World world = player.world;
				
				EntityAirBlade entity = new EntityAirBlade(world, player, ctx.getSkillExp(), player.getLookVec());
				world.spawnEntity(entity);
				
				ctx.addSkillExp(getExpIncr());
				ctx.setCooldown((int)MathUtils.lerpf(40, 30, ctx.getSkillExp()));
			}
		    terminate();
		}

		private float getExpIncr()  {
			return MathUtils.lerpf(0.002f, 0.004f, ctx.getSkillExp());
		}
			
	}


}