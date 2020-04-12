package cn.nulladev.exac.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cn.academy.ACItems;
import cn.academy.AcademyCraft;
import cn.academy.ability.SkillDamageSource;
import cn.academy.client.render.misc.RailgunHandEffect;
import cn.academy.entity.EntityRailgunFX;
import cn.academy.item.ItemEnergyBase;
import cn.lambdalib2.renderhook.DummyRenderData;
import cn.lambdalib2.util.EntitySelectors;
import cn.lambdalib2.util.MathUtils;
import cn.lambdalib2.util.Raytrace;
import cn.lambdalib2.util.VecUtils;
import cn.nulladev.exac.ability.aerohand.skill.AirCannon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.EnumHelper;

public class ItemElectricalibur extends ItemEnergyBase {
	
	public ItemElectricalibur() {
		super(100000, 200);
		this.setUnlocalizedName("electricalibur");
		this.setCreativeTab(AcademyCraft.cct);
		this.bFull3D = true;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return itemManager.pull(stack, 500, true) >= 500;
    }
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
	
	private static ItemStack findCoin(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == ACItems.coin) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ACItems.coin) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (itemstack.getItem() == ACItems.coin) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		boolean flag = !findCoin(player).isEmpty();
		if (!player.capabilities.isCreativeMode && !flag) {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        } else if(!player.capabilities.isCreativeMode && itemManager.getEnergy(itemstack) < 10000) {
        	return new ActionResult(EnumActionResult.FAIL, itemstack);
        } else {
        	if (world.isRemote) {
        		handEffect(player);
    		}
        	player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }	
    }
	
	@SideOnly(Side.CLIENT)
	protected void handEffect(EntityPlayer player) {
		DummyRenderData.get(player).addRenderHook(new RailgunHandEffect());
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		if (this.getMaxItemUseDuration(stack) - timeLeft < 20)
			return;
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            ItemStack itemstack = this.findCoin(player);
            if (!player.capabilities.isCreativeMode && itemstack.isEmpty()) {
            	return;
            }
            if(!player.capabilities.isCreativeMode && itemManager.pull(stack, 10000, true) < 10000) {
            	return;
            }
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    player.inventory.deleteStack(itemstack);
                }
            }
            
            RayTraceResult raytraceresult = world.rayTraceBlocks(player.getPositionVector(), VecUtils.lookingPos(player, 15));
            RayTraceResult raytraceresult1 = Raytrace.traceLiving(player, 15, EntitySelectors.living());
            if (raytraceresult1.entityHit != null)
    			raytraceresult1.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), 30);
            
            if (world.isRemote) {
            	createRailgunFX(raytraceresult, player);
        	}
        }
    }
	
	@SideOnly(Side.CLIENT)
	protected void createRailgunFX(RayTraceResult raytraceresult, EntityPlayer player) {
		EntityRailgunFX eff = new EntityRailgunFX(player, 15);
        if (raytraceresult != null) {
        	Vec3d pos = raytraceresult.hitVec;
    		double d = Math.sqrt(Math.pow(player.posX - pos.x, 2) + Math.pow(player.posY - pos.y, 2) + Math.pow(player.posZ - pos.z, 2));
    		eff.length = d;
        }
        player.world.spawnEntity(eff);
	}
	
	@Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		if (slot != EntityEquipmentSlot.MAINHAND) {
			return super.getAttributeModifiers(slot, stack);
		}
		
		double dmg = itemManager.getEnergy(stack) >= 500? 15:3;
		
	    Multimap ret = HashMultimap.create();
	    ret.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.8D, 0));
	    ret.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", dmg, 0));

	    return ret;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
	    return EnumHelper.addRarity("GOLDEN", TextFormatting.GOLD, "GOLDEN");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.translateToLocal("item.electricalibur.desc"));
		super.addInformation(stack, world, tooltip, flag);
    }

}
