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

    private float _exp;
    private Entity _target = null;
    private double _longitude = 0;
    private double _latitude = 0;
    private float _cur_velocity = 0;
    private int _ticks_since_has_target = 0;

    public EntityBomb(World world) {
        super(world, 0.1F, 0.1F);
        this.setNoGravity();
        this.setNoDecrease();
    }

    public EntityBomb(World world, EntityPlayer thrower, float exp) {
        super(world, thrower, thrower.posX, thrower.posY + 1, thrower.posZ, 0.1F, 0.1F, Integer.MAX_VALUE);
        this.setNoGravity();
        this.setNoDecrease();
        this._exp = exp;
        this.harvestStrength = 0F;
        this.genRanPos();
    }

    private void genRanPos() {
        Random ran = new Random();
        this._longitude = 2 * Math.PI * ran.nextDouble();
        this._latitude = 2 * Math.PI * ran.nextDouble();
        this.posX = this.getOwner().posX + Math.sin(_latitude);
        this.posY = this.getOwner().posY + 1 + Math.cos(_latitude) * Math.cos(_longitude);
        this.posZ = this.getOwner().posZ + Math.cos(_latitude) * Math.sin(_longitude);
        this.setPosition(posX, posY, posZ);
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    private float getAcc() {
        return MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, this._exp);
    }

    private float getDamage() {
        return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this._exp);
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        if (this._target == null)
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
        if (this._target == null) {
            this._latitude += 2 * Math.PI / 80;
            if (this._latitude > 2 * Math.PI)
                this._latitude -= 2 * Math.PI;
            this.posX = this.getOwner().posX + Math.sin(_latitude);
            this.posY = this.getOwner().posY + 1 + Math.cos(_latitude) * Math.cos(_longitude);
            this.posZ = this.getOwner().posZ + Math.cos(_latitude) * Math.sin(_longitude);
            this.setPosition(posX, posY, posZ);
        } else {
            if (this._ticks_since_has_target <= ACC_TIME) {
                this._cur_velocity += this.getAcc();
                Vec3d direc = new Vec3d(this._target.posX - this.posX, this._target.posY - this.posY, this._target.posZ - this.posZ);
                this.setVelocity(direc, _cur_velocity);
            } else if (this._ticks_since_has_target >= AGE) {
                this.setDead();
            }
            this._ticks_since_has_target++;
        }
    }

    public Entity get_target() {
        return this._target;
    }

    public void lock(Entity entity) {
        this._target = entity;
        this._cur_velocity += this.getAcc();
        Vec3d direc = new Vec3d(this.posX - this._target.posX, this.posY - this._target.posY - this._target.height / 2, this.posZ - this._target.posZ);
        this.setVelocity(direc, _cur_velocity);
    }
}
