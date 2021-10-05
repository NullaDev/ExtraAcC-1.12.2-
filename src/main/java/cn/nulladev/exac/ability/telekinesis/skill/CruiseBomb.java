package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.*;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityBombController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Function1;
import scala.reflect.ClassTag;
import scala.runtime.AbstractFunction1;

public class CruiseBomb extends Skill {

    public static final CruiseBomb INSTANCE = new CruiseBomb();

    private CruiseBomb() {
        super("cruise_bomb", 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
            public Context apply(EntityPlayer p) {
                return new ContextCruiseBomb(p);
            }
        };
        ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextCruiseBomb.class);
        rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
    }

    public static class ContextCruiseBomb extends Context {

        public ContextCruiseBomb(EntityPlayer _player) {
            super(_player, CruiseBomb.INSTANCE);
        }

        private static ItemStack findBucket(EntityPlayer player) {
            if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.WATER_BUCKET) {
                return player.getHeldItem(EnumHand.OFF_HAND);
            } else if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.WATER_BUCKET) {
                return player.getHeldItem(EnumHand.MAIN_HAND);
            } else {
                for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                    ItemStack itemstack = player.inventory.getStackInSlot(i);
                    if (itemstack.getItem() == Items.WATER_BUCKET) {
                        return itemstack;
                    }
                }
                return ItemStack.EMPTY;
            }
        }

        @NetworkMessage.Listener(channel = MSG_MADEALIVE, side = Side.SERVER)
        private void s_madeAlive() {
            ItemStack bucket = findBucket(this.player);
            if (!this.player.capabilities.isCreativeMode && bucket == ItemStack.EMPTY) {
                terminate();
            } else {
                if (!this.player.capabilities.isCreativeMode) {
                    bucket.shrink(1);
                    this.player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
                }
            }
            ctx.consume(20, 200);
            EntityBombController controller = new EntityBombController(player.world, player);
            player.world.spawnEntity(controller);
        }

        @NetworkMessage.Listener(channel = MSG_TICK, side = Side.SERVER)
        private void s_tick() {
            float cp = MathUtils.lerpf(2, 1, ctx.getSkillExp());
            if (ctx.consume(0.05F, cp)) {
                ctx.addSkillExp(0.00002f);
            } else
                terminate();
        }

        @NetworkMessage.Listener(channel = MSG_TERMINATED, side = Side.SERVER)
        private void s_terminate() {
            ctx.setCooldown(40);
        }

    }

    @SideOnly(Side.CLIENT)
    @RegClientContext(ContextCruiseBomb.class)
    public static class ContextCruiseBombC extends ClientContext {

        private ClientRuntime.IActivateHandler activateHandler;

        public ContextCruiseBombC(ContextCruiseBomb ctx) {
            super(ctx);
        }

        @NetworkMessage.Listener(channel=MSG_MADEALIVE, side=Side.CLIENT)
        private void l_alive() {
            if (isLocal()) {
                activateHandler = ClientRuntime.ActivateHandlers.terminatesContext(this.parent);
                ClientRuntime.instance().addActivateHandler(activateHandler);
            }
        }

        @NetworkMessage.Listener(channel=MSG_TERMINATED, side=Side.CLIENT)
        private void l_terminate() {
            if (isLocal()) {
                ClientRuntime.instance().removeActiveHandler(activateHandler);
            }
        }
    }

}