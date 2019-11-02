package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import cn.nulladev.exac.core.ExtraAcademy;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemConstraintArmor extends ItemArmor {
	
	private static final int[] ArmorVars = {2, 6, 5, 2};
	public static final ArmorMaterial CONSTRAINT = EnumHelper.addArmorMaterial("constraint", "constraint", 12, ArmorVars, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

	public ItemConstraintArmor(EntityEquipmentSlot position) {
		super(CONSTRAINT, 0, position);
		this.setCreativeTab(AcademyCraft.cct);
		
		switch (this.armorType) {
		case HEAD:
			this.setUnlocalizedName("helmetConstraint");
			break;
		case CHEST:
			this.setUnlocalizedName("chestplateConstraint");
			break;
		case LEGS:
			this.setUnlocalizedName("leggingsConstraint");
			break;
		case FEET:
			this.setUnlocalizedName("bootsConstraint");
			break;
		default:
		}
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (slot == EntityEquipmentSlot.LEGS)
			return ExtraAcademy.MODID + ":textures/models/armor/constraint_layer_2.png";	
		return ExtraAcademy.MODID + ":textures/models/armor/constraint_layer_1.png";
    }

}
