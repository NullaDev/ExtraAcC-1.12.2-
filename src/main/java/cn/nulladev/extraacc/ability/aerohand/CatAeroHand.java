package cn.nulladev.extraacc.ability.aerohand;

import cn.academy.ability.Category;
import cn.academy.ability.Skill;
import cn.academy.ability.vanilla.VanillaCategories;
import cn.nulladev.extraacc.ability.aerohand.skill.AirBlade;
import cn.nulladev.extraacc.ability.aerohand.skill.AirCannon;
import cn.nulladev.extraacc.ability.aerohand.skill.AirCooling;
import cn.nulladev.extraacc.ability.aerohand.skill.AirJet;
import cn.nulladev.extraacc.ability.aerohand.skill.BomberLance;

public class CatAeroHand extends Category {
	
	public static Skill basic_air_gun = AirCannon.INSTANCE;
	//public static Skill air_control = SkillAirControl.INSTANCE;
	
	public static Skill air_blade = AirBlade.INSTANCE;
	public static Skill air_cooling = AirCooling.INSTANCE;
	
	public static Skill air_jet = AirJet.INSTANCE;
	public static Skill bomber_lance = BomberLance.INSTANCE;
	//public static Skill updraft = SkillUpdraft.INSTANCE;
	//public static Skill sand_storm = SkillSandStorm.INSTANCE;
	
	//public static Skill turbulent_storm = SkillTurbulentStorm.INSTANCE;

	public CatAeroHand() {
		super("aerohand");
		this.setColorStyle(191, 255, 255, 127);
		
		//Lv1
		addSkill(basic_air_gun);
		//addSkill(air_control);
		
		//Lv2
		addSkill(air_blade);
		addSkill(air_cooling);
		
		//Lv3
		addSkill(air_jet);
		
		//Lv4
		addSkill(bomber_lance);
		//addSkill(updraft);
		//addSkill(sand_storm);
		
		//addSkill(turbulent_storm);
		
		VanillaCategories.addGenericSkills(this);
		
		basic_air_gun.setPosition(20, 25);
		//air_control.setPosition(35, 60);
		
		air_blade.setPosition(70, 15);
		air_cooling.setPosition(90, 45);
		
		air_jet.setPosition(115, 80);
		//explosive_air_gun.setPosition(90, 45);
			
		bomber_lance.setPosition(150, 20);
		//updraft.setPosition(150, 20);
		//sand_storm.setPosition(160, 75);
		
		//turbulent_storm.setPosition(200, 35);
		
		air_blade.setParent(basic_air_gun, 0.5F);

	}

}