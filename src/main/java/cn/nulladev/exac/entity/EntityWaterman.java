package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.academy.datapart.AbilityData;
import cn.academy.datapart.CPData;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.LiquidShadow;
import cn.nulladev.exac.ability.telekinesis.skill.LiquidShadow.ContextLiquidShadow;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class EntityWaterman extends EntityTameable {

    public EntityWaterman(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.setTamed(true);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20D);
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.BLOCK_WATER_AMBIENT, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.BLOCK_WATER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.BLOCK_WATER_AMBIENT;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            return;
        }
        if (this.inWater) {
            this.setDead();
            this.disableContext();
            return;
        }
        EntityLivingBase owner = this.getOwner();
        if (!(owner instanceof EntityPlayer)) {
            this.setDead();
            this.disableContext();
            return;
        }
        Optional<ContextLiquidShadow> context = ContextManager.instance.find(ContextLiquidShadow.class);
        if(!context.isPresent() || (context.get().getStatus() != Context.Status.ALIVE && context.get().player == owner)) {
            this.setDead();
            this.disableContext();
            return;
        }
        if (this.isDead) {
            this.disableContext();
            return;
        }
    }

    private void disableContext() {
        EntityLivingBase owner = this.getOwner();
        if (owner instanceof EntityPlayer) {
            Optional<ContextLiquidShadow> context = ContextManager.instance.find(ContextLiquidShadow.class);
            if (context.isPresent() && context.get().getStatus() == Context.Status.ALIVE && context.get().player == owner) {
                context.get().terminate();
            }
        }
    }

    private static float getAttack(float exp) {
        return MathUtils.lerpf(20, 30, exp);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        EntityLivingBase owner = this.getOwner();
        if (!(owner instanceof EntityPlayer))
            return false;
        EntityPlayer player = (EntityPlayer)owner;
        Optional<ContextLiquidShadow> context = ContextManager.instance.find(ContextLiquidShadow.class);
        if(!context.isPresent() || (context.get().getStatus() != Context.Status.ALIVE && context.get().player == owner)) {
            return false;
        }
        DamageSource source = new SkillDamageSource(player, LiquidShadow.INSTANCE);
        float value = getAttack(context.get().ctx.getSkillExp());
        boolean flag = entityIn.attackEntityFrom(source, value);
        if (flag) {
            this.applyEnchantments(this, entityIn);
            CPData.get(player).perform(0, 5 * value);
            AbilityData.get(player).addSkillExp(LiquidShadow.INSTANCE, value * 0.0001F);
        }
        return flag;
    }

}
