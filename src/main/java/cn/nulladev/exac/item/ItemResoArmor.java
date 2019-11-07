package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import cn.nulladev.exac.core.ExtraAcademy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemResoArmor extends ItemArmor implements ISpecialArmor {
	
	private static final int[] ArmorVars = {2, 5, 6, 2};
	public static final ArmorMaterial RESO = EnumHelper.addArmorMaterial("reso", "reso", 12, ArmorVars, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);

	public ItemResoArmor(EntityEquipmentSlot position) {
		super(RESO, 0, position);
		this.setCreativeTab(AcademyCraft.cct);
		
		switch (this.armorType) {
		case HEAD:
			this.setUnlocalizedName("helmetReso");
			break;
		case CHEST:
			this.setUnlocalizedName("chestplateReso");
			break;
		case LEGS:
			this.setUnlocalizedName("leggingsReso");
			break;
		case FEET:
			this.setUnlocalizedName("bootsReso");
			break;
		default:
		}
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (slot == EntityEquipmentSlot.LEGS)
			return ExtraAcademy.MODID + ":textures/models/armor/reso_layer_2.png";	
		return ExtraAcademy.MODID + ":textures/models/armor/reso_layer_1.png";
    }

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		if (source.damageType.equals("ac_skill")) {
			double absorptionRatio = getBaseAbsorptionRatio() * 0.75D;
			return new ISpecialArmor.ArmorProperties(0, absorptionRatio, 50);
		} else {
			return new ISpecialArmor.ArmorProperties(0, 0.0D, 0);
		}
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		
	}
	
	private double getBaseAbsorptionRatio() {
		switch (this.armorType) {
		case HEAD: 
			return 0.15D;
		case CHEST: 
			return 0.4D;
		case LEGS: 
			return 0.3D;
		case FEET: 
			return 0.15D;
		default:
			return 0.0D;
		}
	}

}
