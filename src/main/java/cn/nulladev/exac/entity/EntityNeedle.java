package cn.nulladev.exac.entity;

import cn.academy.ACItems;
import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.PsychoNeedling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityNeedle extends EntityFlying {

    public static final int ACC_TIME = 20;
    public static final float BASIC_VELOCITY = 0.1F;
    public static final float MAX_VELOCITY = 0.15F;
    public static final float BASIC_DAMAGE = 4F;
    public static final float MAX_DAMAGE = 8F;

    private float exp;
    private Vec3d direc;

    public EntityNeedle(World world) {
        super(world, 0.5F, 0.5F);
        this.setNoGravity();
        this.setNoDecrease();
    }

    public EntityNeedle(World world, EntityPlayer thrower, float _exp, Vec3d _dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, 0.5F, 0.5F, Integer.MAX_VALUE);
        this.setNoGravity();
        this.setNoDecrease();
        this.exp = _exp;
        this.direc = _dir;
        this.setVelocity(this.direc, this.getAcc());
        this.harvestStrength = 0F;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    private float getAcc() {
        return MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, this.exp);
    }

    private float getDamage() {
        return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this.exp);
    }

    public void addVelocity(Vec3d vDir, float v) {
        vDir.normalize();
        this.addVelocity(v * vDir.x, v * vDir.y, v * vDir.z);
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        BlockPos genPos;
        if (pos.entityHit != null) {
            pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), PsychoNeedling.INSTANCE).setDamageBypassesArmor().setProjectile(), getDamage());
            genPos = new BlockPos(pos.hitVec);
        } else {
            genPos = pos.getBlockPos();
        }
        EntityItem drop = new EntityItem(this.world, genPos.getX(), genPos.getY(), genPos.getZ(), new ItemStack(ACItems.needle));
        this.world.spawnEntity(drop);
        this.setDead();
    }

    @Override
    public void onUpdate() {
        if (this.ticksExisted <= ACC_TIME && this.direc != null)
            this.addVelocity(this.direc, this.getAcc());
        super.onUpdate();
    }
}
