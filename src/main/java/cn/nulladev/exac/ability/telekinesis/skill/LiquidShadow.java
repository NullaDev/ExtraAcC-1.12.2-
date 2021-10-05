package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientContext;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.RegClientContext;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityWaterman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Function1;
import scala.reflect.ClassTag;
import scala.runtime.AbstractFunction1;

public class LiquidShadow extends Skill {

    public static final LiquidShadow INSTANCE = new LiquidShadow();

    private LiquidShadow() {
        super("liquid_shadow", 5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
            public Context apply(EntityPlayer p) {
                return new ContextLiquidShadow(p);
            }
        };
        ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextLiquidShadow.class);
        rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
    }

    public static class ContextLiquidShadow extends Context {

        private final float cp;
        private final float overload;

        public ContextLiquidShadow(EntityPlayer _player) {
            super(_player, LiquidShadow.INSTANCE);
            cp = MathUtils.lerpf(2000, 1000, ctx.getSkillExp());
            overload = MathUtils.lerpf(300, 200, ctx.getSkillExp());
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

        @NetworkMessage.Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
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
            if(ctx.consume(overload, cp)) {
                World world = player.world;
                EntityWaterman waterman = new EntityWaterman(world);
                waterman.setOwnerId(player.getUniqueID());
                world.spawnEntity(waterman);
                ctx.addSkillExp(0.002F);
            }
        }

        @NetworkMessage.Listener(channel=MSG_TICK, side=Side.SERVER)
        private void s_tick() {
            if(ctx.consume(0F, 1F)) {
                ctx.addSkillExp(0.0001f);
            }
            else
                terminate();
        }

        @NetworkMessage.Listener(channel=MSG_TERMINATED, side=Side.SERVER)
        private void s_terminate() {
            ctx.setCooldown(100);
        }

    }

    @SideOnly(Side.CLIENT)
    @RegClientContext(ContextLiquidShadow.class)
    public static class ContextLiquidShadowC extends ClientContext {

        private ClientRuntime.IActivateHandler activateHandler;

        public ContextLiquidShadowC(ContextLiquidShadow ctx) {
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
