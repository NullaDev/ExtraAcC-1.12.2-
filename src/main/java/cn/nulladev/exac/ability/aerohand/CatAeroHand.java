package cn.nulladev.exac.ability.aerohand;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.exac.ability.aerohand.skill.*;

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
	public static Skill storm_core = StormCore.INSTANCE;

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
		addSkill(storm_core);
		
		VanillaCategories.addGenericSkills(this);
		
		volcanic_ball.setPosition(20, 25);
		ascending_air.setPosition(30, 70);
		
		air_blade.setPosition(65, 20);
		airflow.setPosition(75, 85);
		air_cooling.setPosition(80, 55);
		
		air_wall.setPosition(110, 35);
		air_jet.setPosition(120, 75);
			
		bomber_lance.setPosition(150, 10);
		offense_armour.setPosition(160, 45);
		flying.setPosition(165, 85);
		
		absolute_vacuum.setPosition(200, 25);
		storm_core.setPosition(205, 65);
		
		air_blade.setParent(volcanic_ball, 0.5F);
		air_wall.setParent(air_blade, 0.5F);
		bomber_lance.setParent(air_blade, 0.5F);
		offense_armour.setParent(air_wall, 1.0F);
		absolute_vacuum.setParent(bomber_lance, 0.5F);
		
		air_cooling.setParent(ascending_air);
		airflow.setParent(ascending_air, 0.5F);
		air_jet.setParent(airflow, 0.1F);
		flying.setParent(air_jet, 0.5F);
		storm_core.setParent(flying, 0.5F);
	}

}