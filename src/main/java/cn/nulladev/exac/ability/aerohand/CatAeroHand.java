package cn.nulladev.exac.ability.aerohand;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.exac.ability.aerohand.skill.AeroSeparator;
import cn.nulladev.exac.ability.aerohand.skill.AirBlade;
import cn.nulladev.exac.ability.aerohand.skill.VolcanicBall;
import cn.nulladev.exac.ability.aerohand.skill.AirCooling;
import cn.nulladev.exac.ability.aerohand.skill.AirJet;
import cn.nulladev.exac.ability.aerohand.skill.AirWall;
import cn.nulladev.exac.ability.aerohand.skill.Airflow;
import cn.nulladev.exac.ability.aerohand.skill.AscendingAir;
import cn.nulladev.exac.ability.aerohand.skill.BomberLance;
import cn.nulladev.exac.ability.aerohand.skill.Flying;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour;

public class CatAeroHand extends Category {
	
	public static Skill volcanic_ball = VolcanicBall.INSTANCE;
	public static Skill ascending_air = AscendingAir.INSTANCE;
	
	public static Skill air_blade = AirBlade.INSTANCE;
	public static Skill airflow = Airflow.INSTANCE;
	public static Skill air_cooling = AirCooling.INSTANCE;
	
	public static Skill air_wall = AirWall.INSTANCE;
	public static Skill air_jet = AirJet.INSTANCE;
	
	public static Skill bomber_lance = BomberLance.INSTANCE;
	public static Skill offense_armour = OffenseArmour.INSTANCE;
	public static Skill flying = Flying.INSTANCE;
	
	public static Skill absolute_vacuum = AeroSeparator.INSTANCE;

	public CatAeroHand() {
		super("aerohand");
		this.setColorStyle(191, 255, 255, 127);
		
		addSkill(volcanic_ball);
		addSkill(ascending_air);
		
		addSkill(air_blade);
		addSkill(airflow);
		addSkill(air_cooling);
		
		addSkill(air_wall);
		addSkill(air_jet);
		
		addSkill(bomber_lance);
		addSkill(offense_armour);
		addSkill(flying);
		
		addSkill(absolute_vacuum);
		
		VanillaCategories.addGenericSkills(this);
		
		volcanic_ball.setPosition(20, 25);
		ascending_air.setPosition(30, 65);
		
		air_blade.setPosition(65, 20);
		airflow.setPosition(75, 80);
		air_cooling.setPosition(80, 50);
		
		air_wall.setPosition(110, 35);
		air_jet.setPosition(120, 75);
			
		bomber_lance.setPosition(145, 10);
		offense_armour.setPosition(160, 45);
		flying.setPosition(170, 80);
		
		absolute_vacuum.setPosition(200, 25);
		
		air_blade.setParent(volcanic_ball, 0.5F);
		air_wall.setParent(air_blade, 0.5F);
		bomber_lance.setParent(air_blade, 0.5F);
		offense_armour.setParent(air_wall, 1.0F);
		absolute_vacuum.setParent(bomber_lance, 0.5F);
		
		air_cooling.setParent(ascending_air);
		airflow.setParent(ascending_air, 0.5F);
		air_jet.setParent(airflow, 0.1F);
		flying.setParent(air_jet, 0.5F);
	}

}