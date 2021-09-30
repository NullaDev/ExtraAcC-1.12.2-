package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;

public class PerfectPaper extends Skill {

    public static final PerfectPaper INSTANCE = new PerfectPaper();

    private PerfectPaper() {
        super("perfect_paper", 3);
        this.canControl = false;
    }
}
