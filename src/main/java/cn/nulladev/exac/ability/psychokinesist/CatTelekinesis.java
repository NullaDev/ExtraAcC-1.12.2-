package cn.nulladev.exac.ability.psychokinesist;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.exac.ability.psychokinesist.skill.PsychoThrowing;
import cn.nulladev.exac.ability.psychokinesist.skill.Insulation;

public class CatTelekinesis extends Category {

    public static Skill psycho_throwing = PsychoThrowing.INSTANCE;
    public static Skill insulation = Insulation.INSTANCE;

    public CatTelekinesis() {
        super("telekinesis");
        this.setColorStyle(255, 127, 127, 127);

        addSkill(psycho_throwing);
        addSkill(insulation);

        VanillaCategories.addGenericSkills(this);

        psycho_throwing.setPosition(20, 25);
        insulation.setPosition(30, 65);
    }

}
