package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import cn.academy.datapart.AbilityData;
import cn.academy.datapart.CPData;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.PerfectPaper;
import cn.nulladev.exac.core.ExtraAcademy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPaperArmor extends ItemArmor {
    private static final int[] ArmorVars = {2, 5, 6, 2};
    public static final ArmorMaterial PAPER = EnumHelper.addArmorMaterial("paper", "paper", 1, ArmorVars, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 4);

    public ItemPaperArmor(EntityEquipmentSlot position) {
        super(PAPER, 0, position);
        this.setCreativeTab(AcademyCraft.cct);
        this.setMaxDamage(0);

        switch (this.armorType) {
            case HEAD:
                this.setUnlocalizedName("helmetPaper");
                break;
            case CHEST:
                this.setUnlocalizedName("chestplatePaper");
                break;
            case LEGS:
                this.setUnlocalizedName("leggingsPaper");
                break;
            case FEET:
                this.setUnlocalizedName("bootsPaper");
                break;
            default:
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack){
        if (AbilityData.get(player) == null || !AbilityData.get(player).isSkillLearned(PerfectPaper.INSTANCE)) {
            drop(world, player, itemStack);
        } else {
            float cp = MathUtils.lerpf(0.005F, 0.0025F, AbilityData.get(player).getSkillExp(PerfectPaper.INSTANCE));
            if (!CPData.get(player).isOverloaded() &&
                    !CPData.get(player).isOverloadRecovering() &&
                    CPData.get(player).perform(0, cp)) {
                AbilityData.get(player).addSkillExp(PerfectPaper.INSTANCE, 0.000001F);
            } else {
                drop(world, player, itemStack);
            }
        }
    }

    private void drop(World world, EntityPlayer player, ItemStack itemStack) {
        int index = player.inventory.armorInventory.indexOf(itemStack);
        if (index != -1) {
            player.inventory.armorInventory.set(index, ItemStack.EMPTY);
        }
        if (!world.isRemote) {
            int num = 0;
            switch (this.armorType) {
                case HEAD:
                    num = 5;
                    break;
                case CHEST:
                    num = 8;
                    break;
                case LEGS:
                    num = 7;
                    break;
                case FEET:
                    num = 4;
                    break;
                default:
            }
            EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Items.PAPER, num));
            world.spawnEntity(item);
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (slot == EntityEquipmentSlot.LEGS)
            return ExtraAcademy.MODID + ":textures/models/armor/paper_layer_2.png";
        return ExtraAcademy.MODID + ":textures/models/armor/paper_layer_1.png";
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(I18n.translateToLocal("item.armorPaper.desc"));
    }
}
