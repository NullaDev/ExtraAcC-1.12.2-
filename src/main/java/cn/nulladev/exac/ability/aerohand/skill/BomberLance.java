package cn.nulladev.exac.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage.Listener;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityBomberLance;
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
		activateSingleKey2(rt, keyID, ContextBomberLance::new);
	}
	
	public static class ContextBomberLance extends Context {
		
		static final String MSG_PERFORM = "perform";
		
		private final float cp;
		private final float overload;

		public ContextBomberLance(EntityPlayer _player) {
			super(_player, BomberLance.INSTANCE);
			cp = MathUtils.lerpf(600, 900, ctx.getSkillExp());
			overload = MathUtils.lerpf(240, 160, ctx.getSkillExp());
		}
		
		@Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
		public void l_keydown()  {
			sendToServer(MSG_PERFORM);
		}
		
		@Listener(channel=MSG_PERFORM, side=Side.SERVER)
		public void s_perform()  {
			if(ctx.consume(overload, cp)) {
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