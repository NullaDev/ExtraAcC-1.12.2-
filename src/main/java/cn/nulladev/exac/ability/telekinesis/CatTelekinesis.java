package cn.nulladev.exac.ability.telekinesis;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.exac.ability.telekinesis.skill.*;

public class CatTelekinesis extends Category {

    public static Skill psycho_throwing = PsychoThrowing.INSTANCE;
    public static Skill psycho_transmission = PsychoTransmission.INSTANCE;

    public static Skill psycho_needling = PsychoNeedling.INSTANCE;
    public static Skill insulation = Insulation.INSTANCE;

    public static Skill cruise_bomb = CruiseBomb.INSTANCE;
    public static Skill overload_thinking = OverloadThinking.INSTANCE;
    public static Skill perfect_paper = PerfectPaper.INSTANCE;

    public static Skill psycho_slam = PsychoSlam.INSTANCE;
    public static Skill psycho_harden = PsychoHarden.INSTANCE;

    public static Skill liquid_shadow = LiquidShadow.INSTANCE;
    public static Skill paper_drill = PaperDrill.INSTANCE;

    public CatTelekinesis() {
        super("telekinesis");
        this.setColorStyle(255, 127, 127, 127);

        addSkill(psycho_throwing);
        addSkill(psycho_transmission);

        addSkill(psycho_needling);
        addSkill(insulation);

        addSkill(cruise_bomb);
        addSkill(overload_thinking);
        addSkill(perfect_paper);

        addSkill(psycho_slam);
        addSkill(psycho_harden);

        addSkill(liquid_shadow);
        addSkill(paper_drill);

        VanillaCategories.addGenericSkills(this);

        psycho_throwing.setPosition(20, 25);
        psycho_transmission.setPosition(30, 65);

        psycho_needling.setPosition(65, 20);
        insulation.setPosition(70, 70);

        cruise_bomb.setPosition(110, 15);
        overload_thinking.setPosition(115, 55);
        perfect_paper.setPosition(120, 85);

        psycho_slam.setPosition(160, 30);
        psycho_harden.setPosition(165, 60);

        liquid_shadow.setPosition(190, 15);
        paper_drill.setPosition(205, 80);

        psycho_needling.setParent(psycho_throwing, 0.5F);
        insulation.setParent(psycho_transmission);

        cruise_bomb.setParent(psycho_needling, 0.5F);
        overload_thinking.setParent(insulation);
        perfect_paper.setParent(insulation);

        psycho_slam.setParent(cruise_bomb, 0.5F);
        psycho_harden.setParent(overload_thinking, 0.5F);

        liquid_shadow.setParent(cruise_bomb, 0.9F);
        paper_drill.setParent(perfect_paper, 0.5F);
    }

}
