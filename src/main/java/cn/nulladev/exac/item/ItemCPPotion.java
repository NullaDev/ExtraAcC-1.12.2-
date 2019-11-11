package cn.nulladev.exac.item;

import cn.academy.datapart.CPData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCPPotion extends EXACItemNormal {
	
	public static final float CP_RECOVER_AMOUNT = 2000;
	
	public ItemCPPotion() {
		super("CPPotion");
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 40;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (ItemCPPotion.canEat(player)) {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
        	EntityPlayer player = (EntityPlayer) entityLiving;
        	if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
                player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
                
            }
        	//acc的默认数据
        	//init_cp: [1800, 1800, 2800, 4000, 5800, 8000]
        	//add_cp: [0, 900, 1000, 1500, 1700, 12000]
        	float cp = Math.min(CPData.get(player).getCP() + CP_RECOVER_AMOUNT, CPData.get(player).getMaxCP() / 2);
        	CPData.get(player).setCP(cp);
        }
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
        	return stack;
        }
	}
	
	private static boolean canEat(EntityPlayer player) {
		return CPData.get(player).getCP() < CPData.get(player).getMaxCP() / 2;
	}

}
