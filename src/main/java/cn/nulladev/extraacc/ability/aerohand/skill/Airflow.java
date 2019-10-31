package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.datapart.AbilityData;
import cn.academy.datapart.CPData;
import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Airflow extends Skill {
	
	public static final Airflow INSTANCE = new Airflow();

	private Airflow() {
		super("airflow", 2);
		this.canControl = false;
	}
	
	@SubscribeEvent
    public void damage(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntity();
		
		if (event.getSource().getDamageType().equals("inWall") || event.getSource().getDamageType().equals("drown")) {
			if (AbilityData.get(player).isSkillLearned(Airflow.INSTANCE)) {
				float exp = AbilityData.get(player).getSkillExp(AscendingAir.INSTANCE);
				float dmg = event.getAmount();
				float cp = MathUtils.lerpf(40, 20, exp);
				if (!CPData.get(player).isOverloaded() &&
						!CPData.get(player).isOverloadRecovering() && 
						CPData.get(player).perform(0, cp * dmg)) {
					float dmg1 = dmg * MathUtils.lerpf(0.5F, 0.1F, exp);
					event.setAmount(dmg1);
					AbilityData.get(player).addSkillExp(Airflow.INSTANCE, dmg * 0.002F);
				}		
			}
		}
    }
	
	@SubscribeEvent
    public void air(PlayerTickEvent event) {
		if (event.player.getAir() < 300 && event.player.world.getTotalWorldTime() % 10 == 0) {
			if (AbilityData.get(event.player).isSkillLearned(Airflow.INSTANCE)) {
				if (AbilityData.get(event.player).getSkillExp(Airflow.INSTANCE) >= 0.5F) {
					if (!CPData.get(event.player).isOverloaded() &&
							!CPData.get(event.player).isOverloadRecovering() && 
							CPData.get(event.player).perform(0, 20)) {
						event.player.setAir(300);
						AbilityData.get(event.player).addSkillExp(Airflow.INSTANCE, 0.001F);
					}
				}
			}
		}
	}

}
