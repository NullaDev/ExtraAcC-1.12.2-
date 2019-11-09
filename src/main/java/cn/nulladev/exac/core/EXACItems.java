package cn.nulladev.exac.core;

import cn.nulladev.exac.item.EXACItemNormal;
import cn.nulladev.exac.item.ItemElectricalibur;
import cn.nulladev.exac.item.ItemEnergyUnitGroup;
import cn.nulladev.exac.item.ItemImagArmor;
import cn.nulladev.exac.item.ItemResoArmor;
import cn.nulladev.exac.item.ItemRayTwister;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class EXACItems {
	
	public static final Item optical_chip = new EXACItemNormal("opticalChip");
	public static final Item ray_twister = new ItemRayTwister();
	public static final Item energy_unit_group = new ItemEnergyUnitGroup();
	public static final Item electricalibur = new ItemElectricalibur();
	
	public static final Item reso_helmet = new ItemResoArmor(EntityEquipmentSlot.HEAD);
	public static final Item reso_chestplate = new ItemResoArmor(EntityEquipmentSlot.CHEST);
	public static final Item reso_leggings = new ItemResoArmor(EntityEquipmentSlot.LEGS);
	public static final Item reso_boots = new ItemResoArmor(EntityEquipmentSlot.FEET);
	
	public static final Item imag_helmet = new ItemImagArmor(EntityEquipmentSlot.HEAD);
	public static final Item imag_chestplate = new ItemImagArmor(EntityEquipmentSlot.CHEST);
	public static final Item imag_leggings = new ItemImagArmor(EntityEquipmentSlot.LEGS);
	public static final Item imag_boots = new ItemImagArmor(EntityEquipmentSlot.FEET);

}
