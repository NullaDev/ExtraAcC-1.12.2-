package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.core.EXACItems;
import cn.nulladev.exac.entity.EntityCobblestone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PsychoThrowing extends Skill {

    public static final PsychoThrowing INSTANCE = new PsychoThrowing();

    private PsychoThrowing() {
        super("psycho_throwing", 1);
    }

    private static ItemStack findStone(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) || player.getHeldItem(EnumHand.OFF_HAND).getItem() == EXACItems.etched_cobblestone) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) || player.getHeldItem(EnumHand.MAIN_HAND).getItem() == EXACItems.etched_cobblestone) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) || itemstack.getItem() == EXACItems.etched_cobblestone) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        activateSingleKey2(rt, keyID, ContextPsychoThrowing::new);
    }

    public static class ContextPsychoThrowing extends Context {
        static final String MSG_PERFORM = "perform";

        private final float cp;

        public ContextPsychoThrowing(EntityPlayer _player) {
            super(_player, PsychoThrowing.INSTANCE);
            cp = MathUtils.lerpf(400, 800, ctx.getSkillExp());
        }

        private boolean consume(boolean etched) {
            float overload = 20F;
            float bonus = etched? 1F:1.5F;
            return ctx.consume(overload, cp * bonus);
        }

        @NetworkMessage.Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
        public void l_keydown()  {
            sendToServer(MSG_PERFORM);
        }

        @NetworkMessage.Listener(channel=MSG_PERFORM, side=Side.SERVER)
        public void s_perform()  {
            ItemStack stackStone = PsychoThrowing.findStone(this.player);
            boolean etched = false;
            if (player.capabilities.isCreativeMode) {
                etched = true;
                stackStone = ItemStack.EMPTY;
            } else if (stackStone.isEmpty()) {
                return;
            }
            if (stackStone.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
                etched = false;
            } else if (stackStone.getItem() == EXACItems.etched_cobblestone) {
                etched = true;
            }
            if(consume(etched)) {
                if (!stackStone.isEmpty()) {
                    stackStone.shrink(1);
                }
                World world = player.world;
                EntityCobblestone stone = new EntityCobblestone(world, player, ctx.getSkillExp(), player.getLookVec(), etched);
                world.spawnEntity(stone);
                ctx.addSkillExp(getExpIncr());
                ctx.setCooldown((int)MathUtils.lerpf(40, 20, ctx.getSkillExp()));
            }
            terminate();
        }

        private float getExpIncr()  {
            return MathUtils.lerpf(0.002f, 0.001f, ctx.getSkillExp());
        }
    }
}
