package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.ability.aerohand.skill.AirBlade.ContextAirBlade;
import cn.nulladev.extraacc.entity.EntityBomberLance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BomberLance extends Skill {

	public static final BomberLance INSTANCE = new BomberLance();

	private BomberLance() {
		super("bomber_lance", 4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void activate(ClientRuntime rt, int keyID) {
		activateSingleKey2(rt, keyID, (EntityPlayer p) -> new ContextBomberLance(p));
	}
	
	public static class ContextBomberLance extends Context {
		
		static final String MSG_PERFORM = "perform";
		
		private float cp;

		public ContextBomberLance(EntityPlayer _player) {
			super(_player, BomberLance.INSTANCE);
			
			cp = MathUtils.lerpf(600, 900, ctx.getSkillExp());
		}
		
		private boolean consume() {
			float overload = MathUtils.lerpf(240, 160, ctx.getSkillExp());
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
				
				EntityBomberLance entity = new EntityBomberLance(world, player, ctx.getSkillExp(), player.getLookVec());
				world.spawnEntity(entity);
				
				ctx.addSkillExp(getExpIncr());
				ctx.setCooldown((int)MathUtils.lerpf(80, 60, ctx.getSkillExp()));
			}
		    terminate();
		}

		private float getExpIncr()  {
			return MathUtils.lerpf(0.005f, 0.01f, ctx.getSkillExp());
		}
			
	}


}