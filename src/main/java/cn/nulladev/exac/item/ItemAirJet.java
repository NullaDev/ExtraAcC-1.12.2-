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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAirJet extends ItemEnergyBase {

	public ItemAirJet() {
		super(10000, 100);
		this.setUnlocalizedName("airJet");
		this.setCreativeTab(AcademyCraft.cct);
		this.bFull3D = true;
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		if (world.getTotalWorldTime() - nbt.getLong("last_use") < 200) {
			return new ActionResult(EnumActionResult.FAIL, stack);
		}
		if (player.capabilities.isCreativeMode || itemManager.getEnergy(stack) >= 500) {
			if(!player.capabilities.isCreativeMode)
				itemManager.pull(stack, 500, true);
			Vec3d vec = player.getLookVec().normalize();
			double vx = 2 * vec.x;
			double vy = 2 * vec.y;
			double vz = 2 * vec.z;
			player.addVelocity(vx, vy, vz);
			nbt.setLong("last_use", world.getTotalWorldTime());
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}	    
		return new ActionResult(EnumActionResult.FAIL, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.translateToLocal("item.airJet.desc"));
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		if (world != null && world.getTotalWorldTime() - nbt.getLong("last_use") < 200) {
			tooltip.add(I18n.translateToLocal("item.airJet.desc2") + (200 - world.getTotalWorldTime() + nbt.getLong("last_use")));
		}
		super.addInformation(stack, world, tooltip, flag);
    }

}
