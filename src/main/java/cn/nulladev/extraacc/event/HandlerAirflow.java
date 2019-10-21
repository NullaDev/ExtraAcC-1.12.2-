package cn.nulladev.extraacc.event;

import cn.academy.datapart.AbilityData;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.ability.aerohand.skill.Airflow;
import cn.nulladev.extraacc.ability.aerohand.skill.AscendingAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class HandlerAirflow {
	
	@SubscribeEvent
    public void damage(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntity();
		
		if (event.getSource().getDamageType().equals("inWall") || event.getSource().getDamageType().equals("drown")) {
			if (AbilityData.get(player).isSkillLearned(Airflow.INSTANCE)) {
				float exp = AbilityData.get(player).getSkillExp(Airflow.INSTANCE);
				float ratio = MathUtils.lerpf(0.5F, 0.1F, exp);
				event.setAmount(event.getAmount() * ratio);
				AbilityData.get(player).addSkillExp(Airflow.INSTANCE, 0.01F);
			}
		} else if (event.getSource().getDamageType().equals("fall")) {
			if (AbilityData.get(player).isSkillLearned(AscendingAir.INSTANCE)) {
				float exp = AbilityData.get(player).getSkillExp(AscendingAir.INSTANCE);
				float maxDamage = MathUtils.lerpf(10, 5, exp);
				if (event.getAmount() > maxDamage)
					event.setAmount(maxDamage);
				AbilityData.get(player).addSkillExp(AscendingAir.INSTANCE, 0.01F);
			}
		}
    }
	
	@SubscribeEvent
    public void air(PlayerTickEvent event) {
		if (event.player.getAir() < 300 && event.player.world.getTotalWorldTime() % 10 == 0) {
			if (AbilityData.get(event.player).isSkillLearned(Airflow.INSTANCE)) {
				if (AbilityData.get(event.player).getSkillExp(Airflow.INSTANCE) >= 0.5F)
					event.player.setAir(300);
			}
		}
	}

}
