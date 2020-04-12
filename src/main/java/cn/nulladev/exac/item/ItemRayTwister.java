package cn.nulladev.exac.item;

import java.util.List;

import javax.annotation.Nullable;

import cn.academy.AcademyCraft;
import cn.academy.energy.api.item.ImagEnergyItem;
import cn.academy.item.ItemEnergyBase;
import cn.nulladev.exac.core.EXACUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRayTwister extends ItemEnergyBase {

	public ItemRayTwister() {
		super(10000, 100);
		this.setUnlocalizedName("rayTwister");
		this.setCreativeTab(AcademyCraft.cct);
		this.bFull3D = true;
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
			return new ActionResult(EnumActionResult.PASS, stack);

		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
	    if (isActive(nbt)) {
	    	setActive(nbt, false);
	    } else if (player.capabilities.isCreativeMode || itemManager.pull(stack, 200, true) >= 200) {
	    	setActive(nbt, true);
	    }
	    
    	return new ActionResult(EnumActionResult.SUCCESS, stack);   
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		if (world.isRemote)
			return;
		if (!(entity instanceof EntityLivingBase))
			return;
		if (isActive(stack)) {
			if (itemSlot >= 9) {
				setActive(stack, false);
				return;
			}
			if (entity instanceof EntityPlayer) {
				if(!((EntityPlayer)entity).capabilities.isCreativeMode && itemManager.pull(stack, 5, true) < 5) {
					setActive(stack, false);
			    	return;
				}
			} else if (itemManager.pull(stack, 5, true) < 5) {
		    	setActive(stack, false);
		    	return;
		    }
			EntityLivingBase entityliving = (EntityLivingBase) entity;
			entityliving.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 39));
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
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (isActive(stack)) {
			tooltip.add(I18n.translateToLocal("item.imagitem.enabled"));
		} else {
			tooltip.add(I18n.translateToLocal("item.imagitem.disabled"));
		}
		super.addInformation(stack, world, tooltip, flag);
    }

}
