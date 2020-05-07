package cn.nulladev.exac.item;

import java.util.List;

import javax.annotation.Nullable;

import cn.academy.ACItems;
import cn.academy.AcademyCraft;
import cn.academy.item.ItemEnergyBase;
import cn.nulladev.exac.core.EXACUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTeleporter extends ItemEnergyBase {

	public ItemTeleporter() {
		super(10000, 100);
		this.setUnlocalizedName("teleporter");
		this.setCreativeTab(AcademyCraft.cct);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);	
		if(world.provider.isNether()) {
			return new ActionResult(EnumActionResult.FAIL, stack);
		}
		if (player.isSneaking()) {
			if (hasTeleportPos(stack)) {
				setTeleportPos(stack, -1, -1, -1);
				return new ActionResult(EnumActionResult.SUCCESS, stack);
			} else {
				setTeleportPos(stack, player.posX, player.posY, player.posZ);
				return new ActionResult(EnumActionResult.SUCCESS, stack);
			}
		} else {
			if (!hasTeleportPos(stack)) {
				return new ActionResult(EnumActionResult.FAIL, stack);
			}
			boolean flag = !findPearl(player).isEmpty();
			if (player.capabilities.isCreativeMode || (itemManager.getEnergy(stack) >= 5000 && flag)) {
				if(!player.capabilities.isCreativeMode) {
					itemManager.pull(stack, 5000, true);
					findPearl(player).shrink(1);
				}
				Vec3d pos = getTeleportPos(stack);
				player.setPosition(pos.x, pos.y, pos.z);
				return new ActionResult(EnumActionResult.SUCCESS, stack);
			} else {
				return new ActionResult(EnumActionResult.FAIL, stack);
			}
		}
	}
	
	private static ItemStack findPearl(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.ENDER_PEARL) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.ENDER_PEARL) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (itemstack.getItem() == Items.ENDER_PEARL) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }
	
	protected static boolean hasTeleportPos(ItemStack stack) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		if (!nbt.hasKey("telePosX") || !nbt.hasKey("telePosY") || !nbt.hasKey("telePosZ")) {
			return false;
		}
		if (nbt.getDouble("telePosX") == -1 || nbt.getDouble("telePosY") == -1 || nbt.getDouble("telePosZ") == -1) {
			return false;
		}
		return true;
	}
	
	protected static void setTeleportPos(ItemStack stack, double x, double y, double z) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		nbt.setDouble("telePosX", x);
		nbt.setDouble("telePosY", y);
		nbt.setDouble("telePosZ", z);
	}
	
	protected static Vec3d getTeleportPos(ItemStack stack) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		return new Vec3d(nbt.getDouble("telePosX"), nbt.getDouble("telePosY"), nbt.getDouble("telePosZ"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(I18n.translateToLocal("item.teleporter.desc"));
		if (hasTeleportPos(stack)) {
			Vec3d pos = getTeleportPos(stack);
			tooltip.add(I18n.translateToLocal("item.teleporter.desc2") + (int)pos.x + "," + (int)pos.y + "," + (int)pos.z);
		} else {
			tooltip.add(I18n.translateToLocal("item.teleporter.desc3"));
		}
		super.addInformation(stack, world, tooltip, flag);
    }

}