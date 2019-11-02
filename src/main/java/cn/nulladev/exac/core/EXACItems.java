package cn.nulladev.exac.core;

import cn.nulladev.exac.item.ItemConstraintArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class EXACItems {
	
	public static final Item constraint_helmet = new ItemConstraintArmor(EntityEquipmentSlot.HEAD);
	public static final Item constraint_chestplate = new ItemConstraintArmor(EntityEquipmentSlot.CHEST);
	public static final Item constraint_leggings = new ItemConstraintArmor(EntityEquipmentSlot.LEGS);
	public static final Item constraint_boots = new ItemConstraintArmor(EntityEquipmentSlot.FEET);

}
