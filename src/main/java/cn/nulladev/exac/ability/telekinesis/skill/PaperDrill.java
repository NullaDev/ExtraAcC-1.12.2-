package cn.nulladev.exac.ability.telekinesis.skill;

import cn.academy.ability.Skill;
import cn.academy.ability.context.ClientContext;
import cn.academy.ability.context.ClientRuntime;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.RegClientContext;
import cn.academy.ability.ctrl.KeyDelegates;
import cn.lambdalib2.s11n.network.NetworkMessage;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.entity.EntityPaperDrill;
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

public class PaperDrill extends Skill {

    public static final PaperDrill INSTANCE = new PaperDrill();

    private PaperDrill() {
        super("paper_drill", 5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void activate(ClientRuntime rt, int keyID) {
        Function1<EntityPlayer, Context> f = new AbstractFunction1<EntityPlayer, Context>() {
            public Context apply(EntityPlayer p) {
                return new ContextPaperDrill(p);
            }
        };
        ClassTag<Context> tag = scala.reflect.ClassTag$.MODULE$.apply(ContextPaperDrill.class);
        rt.addKey(keyID, KeyDelegates.contextActivate(this, f, tag));
    }

    public static class ContextPaperDrill extends Context {

        private final float cp;
        private final float overload;

        public ContextPaperDrill(EntityPlayer _player) {
            super(_player, PaperDrill.INSTANCE);
            cp = MathUtils.lerpf(200, 100, ctx.getSkillExp());
            overload = MathUtils.lerpf(6, 4, ctx.getSkillExp());
        }

        private static boolean removePaper(EntityPlayer player) {
            if (player.capabilities.isCreativeMode)
                return true;
            int paper = 0;
            if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.PAPER) {
                int val = Math.max(64 - paper, 0);
                paper += Math.min(player.getHeldItem(EnumHand.OFF_HAND).getCount(), val);
                player.getHeldItem(EnumHand.OFF_HAND).shrink(val);
            }
            if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.PAPER) {
                int val = Math.max(64 - paper, 0);
                paper += Math.min(player.getHeldItem(EnumHand.MAIN_HAND).getCount(), val);
                player.getHeldItem(EnumHand.MAIN_HAND).shrink(val);
            }

            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);
                if (itemstack.getItem() == Items.PAPER) {
                    int val = Math.max(64 - paper, 0);
                    paper += Math.min(itemstack.getCount(), val);
                    itemstack.shrink(val);
                }
            }

            if (paper >= 64) {
                return true;
            } else {
                player.inventory.addItemStackToInventory(new ItemStack(Items.PAPER, paper));
                return false;
            }
        }

        @NetworkMessage.Listener(channel=MSG_MADEALIVE, side=Side.SERVER)
        private void s_madeAlive() {
            if (!removePaper(this.player)) {
                terminate();
            }
            if(ctx.consume(20 * overload, 20 * cp)) {
                World world = player.world;
                EntityPaperDrill drill = new EntityPaperDrill(world, player);
                world.spawnEntity(drill);
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
    @RegClientContext(ContextPaperDrill.class)
    public static class ContextPaperDrillC extends ClientContext {

        private ClientRuntime.IActivateHandler activateHandler;

        public ContextPaperDrillC(ContextPaperDrill ctx) {
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
