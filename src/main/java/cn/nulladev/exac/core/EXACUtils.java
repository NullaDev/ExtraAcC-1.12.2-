package cn.nulladev.exac.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EXACUtils {
	
	public static NBTTagCompound get_or_create_nbt(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound();
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
			return nbt;
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

}
