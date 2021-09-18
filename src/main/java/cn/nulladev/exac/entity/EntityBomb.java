package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.CruiseBomb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class EntityBomb extends EntityFlying {

    public static final int ACC_TIME = 20;
    public static final int AGE = 40;
    public static final float BASIC_VELOCITY = 0.1F;
    public static final float MAX_VELOCITY = 0.2F;
    public static final float BASIC_DAMAGE = 4F;
    public static final float MAX_DAMAGE = 8F;

    private float exp;
    private Entity target = null;
    private double longitude = 0;
    private double latitude = 0;
    private float cur_velocity = 0;
    private int time_since_lock = 0;

    public EntityBomb(World world) {
        super(world, 0.1F, 0.1F);
        this.setNoGravity();
        this.setNoDecrease();
    }

    public EntityBomb(World world, EntityPlayer thrower, float _exp) {
        super(world, thrower, thrower.posX, thrower.posY + 1, thrower.posZ, 0.1F, 0.1F, Integer.MAX_VALUE);
        this.setNoGravity();
        this.setNoDecrease();
        this.exp = _exp;
        this.harvestStrength = 0F;
        this.genRanPos();
    }

    private void genRanPos() {
        Random ran = new Random();
        this.longitude = 2 * Math.PI * ran.nextDouble();
        this.latitude = 2 * Math.PI * ran.nextDouble();
        this.posX = this.getOwner().posX + Math.sin(latitude);
        this.posY = this.getOwner().posY + 1 + Math.cos(latitude) * Math.cos(longitude);
        this.posZ = this.getOwner().posZ + Math.cos(latitude) * Math.sin(longitude);
        this.setPosition(posX, posY, posZ);
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

    @Override
    protected void onImpact(RayTraceResult pos) {
        if (this.target == null)
            return;
        if (pos.entityHit != null) {
            if (pos.entityHit instanceof IProjectile)
                pos.entityHit.setDead();
            else
                pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), CruiseBomb.INSTANCE).setDamageBypassesArmor().setProjectile(), getDamage());
        }
        this.setDead();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            return;
        }
        if (this.getOwner() == null) {
            this.setDead();
            return;
        }
        if (this.target == null) {
            this.latitude += 2 * Math.PI / 80;
            if (this.latitude > 2 * Math.PI)
                this.latitude -= 2 * Math.PI;
            this.posX = this.getOwner().posX + Math.sin(latitude);
            this.posY = this.getOwner().posY + 1 + Math.cos(latitude) * Math.cos(longitude);
            this.posZ = this.getOwner().posZ + Math.cos(latitude) * Math.sin(longitude);
            this.setPosition(posX, posY, posZ);
        } else {
            if (this.time_since_lock <= ACC_TIME) {
                this.cur_velocity += this.getAcc();
                Vec3d direc = new Vec3d(this.target.posX - this.posX, this.target.posY - this.posY, this.target.posZ - this.posZ);
                this.setVelocity(direc, cur_velocity);
            } else if (this.time_since_lock >= AGE) {
                this.setDead();
            }
            this.time_since_lock++;
        }
    }

    public Entity getTarget() {
        return this.target;
    }

    public void lock(Entity entity) {
        this.target = entity;
        this.cur_velocity += this.getAcc();
        Vec3d direc = new Vec3d(this.posX - this.target.posX, this.posY - this.target.posY, this.posZ - this.target.posZ);
        this.setVelocity(direc, cur_velocity);
    }
}
