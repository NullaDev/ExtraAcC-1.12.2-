package cn.nulladev.exac.core;

import cn.nulladev.exac.item.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class EXACItems {
	
	public static final Item optical_chip = new EXACItemNormal("opticalChip");
	public static final Item lasor_component = new EXACItemNormal("lasorComponent");
	public static final Item etched_cobblestone = new EXACItemNormal("etchedCobblestone");
	public static final Item ray_twister = new ItemRayTwister();
	public static final Item energy_unit_group = new ItemEnergyUnitGroup();
	public static final Item electricalibur = new ItemElectricalibur();
	public static final Item avalon = new ItemAvalon();
	public static final Item cp_potion = new ItemCPPotion();
	public static final Item lasor_gun = new ItemLasorGun();
	public static final Item air_jet = new ItemAirJet();
	public static final Item teleporter = new ItemTeleporter();
	public static final Item paper_plane = new ItemPaperPlane();
	public static final Item drop_item_magnet = new ItemDropItemMagnet();
	
	public static final Item reso_helmet = new ItemResoArmor(EntityEquipmentSlot.HEAD);
	public static final Item reso_chestplate = new ItemResoArmor(EntityEquipmentSlot.CHEST);
	public static final Item reso_leggings = new ItemResoArmor(EntityEquipmentSlot.LEGS);
	public static final Item reso_boots = new ItemResoArmor(EntityEquipmentSlot.FEET);
	
	public static final Item imag_helmet = new ItemImagArmor(EntityEquipmentSlot.HEAD);
	public static final Item imag_chestplate = new ItemImagArmor(EntityEquipmentSlot.CHEST);
	public static final Item imag_leggings = new ItemImagArmor(EntityEquipmentSlot.LEGS);
	public static final Item imag_boots = new ItemImagArmor(EntityEquipmentSlot.FEET);

	public static final Item paper_helmet = new ItemPaperArmor(EntityEquipmentSlot.HEAD);
	public static final Item paper_chestplate = new ItemPaperArmor(EntityEquipmentSlot.CHEST);
	public static final Item paper_leggings = new ItemPaperArmor(EntityEquipmentSlot.LEGS);
	public static final Item paper_boots = new ItemPaperArmor(EntityEquipmentSlot.FEET);

}
