package cn.nulladev.exac.ability.psychokinesist;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.exac.ability.psychokinesist.skill.SideArm;

public class CatTelekinesis extends Category {

    public static Skill side_arm = SideArm.INSTANCE;

    public CatTelekinesis() {
        super("telekinesis");
        this.setColorStyle(127, 0, 255, 127);

        addSkill(side_arm);

        VanillaCategories.addGenericSkills(this);

        side_arm.setPosition(30, 65);
    }

}
