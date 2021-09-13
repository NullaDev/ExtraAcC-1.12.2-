package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.SkillDamageSource;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.academy.datapart.AbilityData;
import cn.academy.datapart.CPData;
import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Insulation extends Skill {

    public static final Insulation INSTANCE = new Insulation();

    public Insulation() {
        super("insulation", 1);
        this.canControl = false;
    }

    @SubscribeEvent
    public void damage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        if (event.getSource().getDamageType().equals("ac_skill")) {
            if (AbilityData.get(player) == null)
                return;
            if (AbilityData.get(player).isSkillLearned(Insulation.INSTANCE)) {
                float exp = AbilityData.get(player).getSkillExp(Insulation.INSTANCE);
                float protection_rate = MathUtils.lerpf(0.1F, 0.2F, exp);
                try {
                    SkillDamageSource ds = (SkillDamageSource) event.getSource();
                    if (ds.skill.getCategory() == VanillaCategories.electromaster)
                        protection_rate *= 2;
                    else if (ds.skill.getCategory() == VanillaCategories.meltdowner)
                        protection_rate *= 2.5;
                } catch(Exception e) {
                    //Do nothing
                }
                float dmg = event.getAmount();
                float cp_per_dmg = 100F;
                if (!CPData.get(player).isOverloaded() &&
                        !CPData.get(player).isOverloadRecovering() &&
                        CPData.get(player).perform(0, cp_per_dmg * dmg)) {
                    event.setAmount(dmg * (1 - protection_rate));
                    AbilityData.get(player).addSkillExp(Insulation.INSTANCE, dmg * 0.002F);
                }
            }
        } else if (event.getSource().getDamageType().equals("lightningBolt")) {
            if (AbilityData.get(player) == null)
                return;
            if (AbilityData.get(player).isSkillLearned(Insulation.INSTANCE)) {
                float exp = AbilityData.get(player).getSkillExp(Insulation.INSTANCE);
                float protection_rate = MathUtils.lerpf(0.2F, 0.4F, exp);
                float dmg = event.getAmount();
                float cp_per_dmg = 100F;
                if (!CPData.get(player).isOverloaded() &&
                        !CPData.get(player).isOverloadRecovering() &&
                        CPData.get(player).perform(0, cp_per_dmg * dmg)) {
                    event.setAmount(dmg * (1 - protection_rate));
                    AbilityData.get(player).addSkillExp(Insulation.INSTANCE, dmg * 0.002F);
                }
            }
        }
    }
}
