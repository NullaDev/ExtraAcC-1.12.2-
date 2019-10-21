package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;
import cn.academy.datapart.AbilityData;
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
			if (AbilityData.get(player).isSkillLearned(AscendingAir.INSTANCE)) {
				float exp = AbilityData.get(player).getSkillExp(AscendingAir.INSTANCE);
				float maxDamage = MathUtils.lerpf(10, 5, exp);
				if (event.getAmount() > maxDamage)
					event.setAmount(maxDamage);
				AbilityData.get(player).addSkillExp(AscendingAir.INSTANCE, 0.01F);
			}
		}
    }

}
