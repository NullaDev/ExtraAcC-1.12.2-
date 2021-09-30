package cn.nulladev.exac.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cn.academy.AcademyCraft;
import cn.academy.energy.api.IFItemManager;
import cn.academy.energy.api.item.ImagEnergyItem;
import cn.nulladev.exac.core.ExtraAcademy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImagArmor extends ItemArmor implements ImagEnergyItem, ISpecialArmor {
	
	protected static IFItemManager itemManager = IFItemManager.instance;
	
	private static final int[] ArmorVars = {0, 0, 0, 0};
	public static final ArmorMaterial IMAG = EnumHelper.addArmorMaterial("imag", "imag", 1024, ArmorVars, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

	public static final double ABSORB_RATE = 0.9D;
	public static final double ABSORB_RATE_SKILL = 0.95D;
	public static final double ENERGY_COST = 500D;

	/** position代表护甲位置，0为头盔，1为胸甲，2为护腿，3为靴子。 */
	public ItemImagArmor(EntityEquipmentSlot position) {
		super(IMAG, 0, position);
		this.setMaxDamage(13);
		this.setCreativeTab(AcademyCraft.cct);
		
		switch (this.armorType) {
		case HEAD:
			this.setUnlocalizedName("helmetImag");
			break;
		case CHEST:
			this.setUnlocalizedName("chestplateImag");
			break;
		case LEGS:
			this.setUnlocalizedName("leggingsImag");
			break;
		case FEET:
			this.setUnlocalizedName("bootsImag");
			break;
		default:
		}
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		if (source.isUnblockable()) {
			if (source.damageType.equals("ac_skill")) {
				double absorptionRatio = getBaseAbsorptionRatio() * 0.5D;
				return new ISpecialArmor.ArmorProperties(0, absorptionRatio, 50);
			} else {
				return new ISpecialArmor.ArmorProperties(0, 0.0D, Integer.MAX_VALUE);
			}
		}
		double totalRatio = source.damageType.equals("ac_skill")? ABSORB_RATE_SKILL : ABSORB_RATE;
		double slotRatio = getBaseAbsorptionRatio() * totalRatio;
		int damageLimit = (int) (itemManager.getEnergy(armor) / slotRatio / ENERGY_COST);
		if (damageLimit >= 1) {
			return new ISpecialArmor.ArmorProperties(0, slotRatio, damageLimit);
		} else {
			double basicRatio = Math.max(1.4D, 7D - damage / 2) / 25;
			totalRatio = source.damageType.equals("ac_skill")? 0.5D + 0.5D * basicRatio : basicRatio;
			slotRatio = getBaseAbsorptionRatio() * totalRatio;
			return new ISpecialArmor.ArmorProperties(0, slotRatio, Integer.MAX_VALUE);
		}
	}

	@Override
	public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
		return true;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		if (itemManager.getEnergy(armor) >= ENERGY_COST) {
			switch (this.armorType) {
			case HEAD:
				return 3;
			case CHEST: 
				return 8;
			case LEGS: 
				return 6;
			case FEET:
				return 3;
			default:
				return 0;
			}
		} else {
			switch (this.armorType) {
			case HEAD: 
				return 1;
			case CHEST: 
				return 3;
			case LEGS: 
				return 2;
			case FEET: 
				return 1;
			default:
				return 0;
			}
		}
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		double cost = damage * ENERGY_COST;
		itemManager.pull(stack, cost, true);
	}

	@Override
	public double getBandwidth() {
		return 200;
	}

	@Override
	public double getMaxEnergy() {
		return 100000;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            ItemStack is = new ItemStack(this);
            items.add(is);
            itemManager.charge(is, 0, true);

            is = new ItemStack(this);
            itemManager.charge(is, Double.MAX_VALUE, true);
            items.add(is);
        }
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
	  
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (itemManager.getEnergy(stack) >= ENERGY_COST) {
			if (this.armorType == EntityEquipmentSlot.LEGS)
				return ExtraAcademy.MODID + ":textures/models/armor/energy_layer_2.png";	
			return ExtraAcademy.MODID + ":textures/models/armor/energy_layer_1.png";	
		} else {
			if (this.armorType == EntityEquipmentSlot.LEGS)
				return ExtraAcademy.MODID + ":textures/models/armor/noenergy_layer_2.png";	
			return ExtraAcademy.MODID + ":textures/models/armor/noenergy_layer_1.png";	
		}
		
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(itemManager.getDescription(stack));
    }

}

