package cn.nulladev.extraacc.ability.aerohand;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.extraacc.ability.aerohand.skill.AirBlade;
import cn.nulladev.extraacc.ability.aerohand.skill.AirCannon;
import cn.nulladev.extraacc.ability.aerohand.skill.AirCooling;
import cn.nulladev.extraacc.ability.aerohand.skill.AirJet;
import cn.nulladev.extraacc.ability.aerohand.skill.AirWall;
import cn.nulladev.extraacc.ability.aerohand.skill.Airflow;
import cn.nulladev.extraacc.ability.aerohand.skill.AscendingAir;
import cn.nulladev.extraacc.ability.aerohand.skill.BomberLance;

public class CatAeroHand extends Category {
	
	public static Skill air_gun = AirCannon.INSTANCE;
	public static Skill ascending_air = AscendingAir.INSTANCE;
	
	public static Skill air_blade = AirBlade.INSTANCE;
	public static Skill airflow = Airflow.INSTANCE;
	public static Skill air_cooling = AirCooling.INSTANCE;
	
	public static Skill air_wall = AirWall.INSTANCE;
	public static Skill air_jet = AirJet.INSTANCE;
	
	public static Skill bomber_lance = BomberLance.INSTANCE;
	//public static Skill sand_storm = SkillSandStorm.INSTANCE;
	
	//public static Skill turbulent_storm = SkillTurbulentStorm.INSTANCE;

	public CatAeroHand() {
		super("aerohand");
		this.setColorStyle(191, 255, 255, 127);
		
		addSkill(air_gun);
		addSkill(ascending_air);
		
		addSkill(air_blade);
		addSkill(airflow);
		addSkill(air_cooling);
		
		addSkill(air_wall);
		addSkill(air_jet);
		
		addSkill(bomber_lance);
		//addSkill(sand_storm);
		
		//addSkill(turbulent_storm);
		
		VanillaCategories.addGenericSkills(this);
		
		air_gun.setPosition(20, 25);
		ascending_air.setPosition(35, 60);
		
		air_blade.setPosition(70, 20);
		airflow.setPosition(75, 75);
		air_cooling.setPosition(85, 45);
		
		air_wall.setPosition(110, 10);
		air_jet.setPosition(115, 75);
			
		bomber_lance.setPosition(140, 35);
		//sand_storm.setPosition(160, 75);
		
		//turbulent_storm.setPosition(200, 35);
		
		air_blade.setParent(air_gun, 0.5F);
		air_wall.setParent(air_blade, 0.5F);
		bomber_lance.setParent(air_blade, 0.5F);
		
		air_cooling.setParent(ascending_air);
		airflow.setParent(ascending_air, 0.5F);
		air_jet.setParent(airflow, 0.5F);

	}

}