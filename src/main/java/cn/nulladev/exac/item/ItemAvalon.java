package cn.nulladev.exac.item;

import java.util.List;

import javax.annotation.Nullable;

import cn.academy.AcademyCraft;
import cn.academy.item.ItemEnergyBase;
import cn.nulladev.exac.core.EXACUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAvalon extends ItemEnergyBase {
	
	public ItemAvalon() {
		super(100000, 200);
		this.setUnlocalizedName("avalon");
		this.setCreativeTab(AcademyCraft.cct);
		this.bFull3D = true;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		if (world.isRemote)
			return;
		if (world.getTotalWorldTime() % 10 != 0)
			return;
		if (itemManager.getEnergy(stack) <= 2000) {
			return;
		}
		if (!(entity instanceof EntityLivingBase))
			return;
		
		EntityLivingBase entityliving = (EntityLivingBase) entity;
		if (entityliving.getHealth() <= entityliving.getMaxHealth() / 2) {
			setActive(stack, true);
		} else if(entityliving.getHealth() >= entityliving.getMaxHealth()) {
			setActive(stack, false);
		}
		
		if (isActive(stack) && itemManager.pull(stack, 5000, true) >= 2000) {
			entityliving.heal(1);
		}
    }
	
	public static boolean isActive(ItemStack stack) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);	
		return isActive(nbt);
	}

	public static boolean isActive(NBTTagCompound nbt) {
		if (!nbt.hasKey("active")) {
			nbt.setBoolean("active", false);
			return false;
		}
		return nbt.getBoolean("active");
	}
	
	public static void setActive(ItemStack stack, boolean active) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);	
		setActive(nbt, active);
	}

	public static void setActive(NBTTagCompound nbt, boolean active) {
		nbt.setBoolean("active", active);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
	    return EnumHelper.addRarity("GOLDEN", TextFormatting.GOLD, "GOLDEN");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.translateToLocal("item.avalon.desc"));
		super.addInformation(stack, world, tooltip, flag);
    }

}
