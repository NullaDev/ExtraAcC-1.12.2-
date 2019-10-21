package cn.nulladev.extraacc.ability.aerohand.skill;

import cn.academy.ability.Skill;

public class Airflow extends Skill {
	
	public static final Airflow INSTANCE = new Airflow();

	private Airflow() {
		super("airflow", 2);
		this.canControl = false;
	}

}
