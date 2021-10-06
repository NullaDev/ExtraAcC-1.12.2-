package cn.nulladev.exac.item;

import cn.academy.datapart.AbilityData;
import cn.academy.datapart.CPData;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.PerfectPaper;
import cn.nulladev.exac.entity.EntityPaperPlane;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPaperPlane extends EXACItemNormal {
    public ItemPaperPlane() {
        super("paperPlane");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        boolean reinforced = false;
        if (AbilityData.get(player) == null || !AbilityData.get(player).isSkillLearned(PerfectPaper.INSTANCE)) {
            ;
        } else {
            float cp = MathUtils.lerpf(300F, 200F, AbilityData.get(player).getSkillExp(PerfectPaper.INSTANCE));
            if (!CPData.get(player).isOverloaded() &&
                    !CPData.get(player).isOverloadRecovering() &&
                    CPData.get(player).perform(0, cp)) {
                reinforced = true;
                AbilityData.get(player).addSkillExp(PerfectPaper.INSTANCE, 0.001F);
            }
        }
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        EntityPaperPlane plane = new EntityPaperPlane(world, player, player.getLookVec());
        if (reinforced)
            plane.setReinforced();
        world.spawnEntity(plane);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
