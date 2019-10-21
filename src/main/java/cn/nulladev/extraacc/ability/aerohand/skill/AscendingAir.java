package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;

public class AscendingAir extends Skill {
	
	public static final AscendingAir INSTANCE = new AscendingAir();

	private AscendingAir() {
		super("ascending_air", 1);
		this.canControl = false;
	}

}
