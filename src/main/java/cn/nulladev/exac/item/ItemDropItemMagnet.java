package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import cn.academy.item.ItemEnergyBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


import java.util.List;

public class ItemDropItemMagnet extends ItemEnergyBase {

    public static final double ENERGY_COST = 2D;

    public ItemDropItemMagnet() {
        super(10000, 100);
        this.setUnlocalizedName("dropItemMagnet");
        this.setCreativeTab(AcademyCraft.cct);
        this.bFull3D = true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.capabilities.isCreativeMode || itemManager.getEnergy(stack) > ENERGY_COST) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int count) {
        if (!(entity instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) entity;
        int time = getMaxItemUseDuration(stack) - count;
        if (player.capabilities.isCreativeMode || itemManager.getEnergy(stack) > ENERGY_COST * time) {
            List<EntityItem> list = player.world.getEntitiesWithinAABB(EntityItem.class,
                    new AxisAlignedBB(player.posX - 8, player.posY - 8, player.posZ - 8, player.posX + 8, player.posY + 8, player.posZ + 8));
            for (EntityItem item:list) {
                float dist2 = (float)(Math.pow(player.posX-item.posX,2) + Math.pow(player.posY-item.posY,2) + Math.pow(player.posZ-item.posZ,2));
                if (dist2 < 1)
                    continue;
                Vec3d dir = new Vec3d(player.posX-item.posX, player.posY-item.posY, player.posZ-item.posZ);
                item.addVelocity(0.02 * dir.x, 0.02 * dir.y, 0.02 * dir.z);
            }
        } else {
            player.stopActiveHand();
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        int time = getMaxItemUseDuration(stack) - timeLeft;
        if (!(entity instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) entity;
        if(!player.capabilities.isCreativeMode)
            itemManager.pull(stack, ENERGY_COST * time, true);
    }

}
