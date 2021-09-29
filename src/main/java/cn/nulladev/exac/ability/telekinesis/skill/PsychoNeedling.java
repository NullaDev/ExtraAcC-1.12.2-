package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ACItems;
import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityNeedle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PsychoNeedling extends Skill {

    public static final PsychoNeedling INSTANCE = new PsychoNeedling();

    private PsychoNeedling() {
        super("psycho_needling", 2);
    }

    private static ItemStack findNeedle(EntityPlayer player) {
        if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == ACItems.needle) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ACItems.needle) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (itemstack.getItem() == ACItems.needle) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        activateSingleKey2(rt, keyID, ContextPsychoNeedling::new);
    }

    public static class ContextPsychoNeedling extends Context {
        static final String MSG_PERFORM = "perform";

        private final float cp;
        private final float overload;

        public ContextPsychoNeedling(EntityPlayer _player) {
            super(_player, PsychoNeedling.INSTANCE);
            cp = MathUtils.lerpf(800, 400, ctx.getSkillExp());
            overload = MathUtils.lerpf(20, 10, ctx.getSkillExp());
        }

        @NetworkMessage.Listener(channel=MSG_KEYDOWN, side=Side.CLIENT)
        public void l_keydown()  {
            sendToServer(MSG_PERFORM);
        }

        @NetworkMessage.Listener(channel=MSG_PERFORM, side=Side.SERVER)
        public void s_perform()  {
            ItemStack stackNeedle = ItemStack.EMPTY;
            if (!player.capabilities.isCreativeMode) {
                stackNeedle = PsychoNeedling.findNeedle(this.player);
                if (stackNeedle.isEmpty())
                    return;
            }
            if(ctx.consume(overload, cp)) {
                if (!stackNeedle.isEmpty()) {
                    stackNeedle.shrink(1);
                }
                World world = player.world;
                EntityNeedle needle = new EntityNeedle(world, player, ctx.getSkillExp(), player.getLookVec());
                world.spawnEntity(needle);
                ctx.addSkillExp(getExpIncr());
                ctx.setCooldown((int)MathUtils.lerpf(20, 10, ctx.getSkillExp()));
            }
            terminate();
        }

        private float getExpIncr()  {
            return MathUtils.lerpf(0.002f, 0.001f, ctx.getSkillExp());
        }
    }
}