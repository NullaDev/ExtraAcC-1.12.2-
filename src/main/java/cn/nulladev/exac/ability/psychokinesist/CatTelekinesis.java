package cn.nulladev.exac.ability.psychokinesist;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.exac.ability.psychokinesist.skill.PsychoThrowing;
import cn.nulladev.exac.ability.psychokinesist.skill.SideArm;

public class CatTelekinesis extends Category {

    public static Skill psycho_throwing = PsychoThrowing.INSTANCE;
    public static Skill side_arm = SideArm.INSTANCE;

    public CatTelekinesis() {
        super("telekinesis");
        this.setColorStyle(127, 0, 255, 127);

        addSkill(psycho_throwing);
        addSkill(side_arm);

        VanillaCategories.addGenericSkills(this);

        psycho_throwing.setPosition(20, 25);
        side_arm.setPosition(30, 65);
    }

}
