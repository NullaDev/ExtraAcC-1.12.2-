package cn.nulladev.exac.item;

import java.util.List;

import javax.annotation.Nullable;

import cn.academy.AcademyCraft;
import cn.academy.item.ItemEnergyBase;
import cn.nulladev.exac.core.EXACUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

	public static final double ENERGY_COST = 1D;

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

	    if (EXACUtils.isActive(stack)) {
	    	EXACUtils.setActive(stack, false);
	    } else if (player.capabilities.isCreativeMode || itemManager.getEnergy(stack) > ENERGY_COST * 40) {
	    	EXACUtils.setActive(stack, true);
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
		if (EXACUtils.isActive(stack)) {
			if (itemSlot >= 9) {
				EXACUtils.setActive(stack, false);
				return;
			}
			if (entity instanceof EntityPlayer) {
				if(!((EntityPlayer)entity).capabilities.isCreativeMode && itemManager.pull(stack, ENERGY_COST, true) < ENERGY_COST) {
					EXACUtils.setActive(stack, false);
			    	return;
				}
			} else if (itemManager.pull(stack, ENERGY_COST, true) < ENERGY_COST) {
				EXACUtils.setActive(stack, false);
		    	return;
		    }
			EntityLivingBase entityliving = (EntityLivingBase) entity;
			entityliving.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 39));
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (EXACUtils.isActive(stack)) {
			tooltip.add(I18n.translateToLocal("item.imagitem.enabled"));
		} else {
			tooltip.add(I18n.translateToLocal("item.imagitem.disabled"));
		}
		super.addInformation(stack, world, tooltip, flag);
    }

}
