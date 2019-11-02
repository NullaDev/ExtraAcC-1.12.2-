package cn.nulladev.exac.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.datapart.AbilityData;
import cn.academy.datapart.CPData;
import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AscendingAir extends Skill {
	
	public static final AscendingAir INSTANCE = new AscendingAir();

	private AscendingAir() {
		super("ascending_air", 1);
		this.canControl = false;
	}
	
	@SubscribeEvent
    public void damage(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntity();		
		if (event.getSource().getDamageType().equals("fall")) {
			if (AbilityData.get(player) == null) 
				return;
			if (AbilityData.get(player).isSkillLearned(AscendingAir.INSTANCE)) {
				float exp = AbilityData.get(player).getSkillExp(AscendingAir.INSTANCE);
				float dmg = event.getAmount();
				float dmg1 = MathUtils.lerpf(10, 5, exp);
				if (dmg <= dmg1)
					return;
				float cp = MathUtils.lerpf(40, 20, exp);
				if (!CPData.get(player).isOverloaded() &&
						!CPData.get(player).isOverloadRecovering() &&
						CPData.get(player).perform(0, cp * (dmg - dmg1))) {
					event.setAmount(dmg1);
					AbilityData.get(player).addSkillExp(AscendingAir.INSTANCE, (dmg - dmg1) * 0.002F);
				}
			}
		}
    }

}
