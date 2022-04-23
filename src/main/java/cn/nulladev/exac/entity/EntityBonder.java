package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.PsychoSlam;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBonder extends EntityHasOwner {

    public static final float BASIC_DAMAGE = 15;
    public static final float MAX_DAMAGE = 20;
    public static final float PUSH_POWER = 0.2F;
    public static final int SLAM_TIME = 30;
    public static final int AGE = 45;

    private EntityLivingBase _target;
    private float _exp;
    private Vec3d _direcXZ;

    public EntityBonder(World world) {
        super(world);
    }

    public EntityBonder(World _world, EntityPlayer owner, EntityLivingBase target, float exp, Vec3d dir) {
        super(_world);
        this.setOwner(owner);
        this._target = target;
        this._exp = exp;
        this._direcXZ = dir;
        this.setPosition(target.posX, target.posY, target.posZ);
    }

    private float getDamage() {
        return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this._exp);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this._target == null) {
            return;
        }
        if (this.ticksExisted >= AGE) {
            this.setDead();
            return;
        } else if (this.ticksExisted < SLAM_TIME) {
            if (this.ticksExisted == 1)
                this._target.attackEntityFrom(new SkillDamageSource(this.getOwner(), PsychoSlam.INSTANCE), getDamage());
            this.setPosition(this._target.posX, this._target.posY + this._target.height / 2, this._target.posZ);
            Vec3d direc = new Vec3d(this._direcXZ.x, Math.sqrt(3), this._direcXZ.z).normalize();
            this._target.addVelocity(PUSH_POWER * direc.x, PUSH_POWER * direc.y, PUSH_POWER * direc.z);
        } else if (this.ticksExisted == SLAM_TIME) {
            this._target.setVelocity(0, -2, 0);
            this._target.attackEntityFrom(new SkillDamageSource(this.getOwner(), PsychoSlam.INSTANCE), 2 * getDamage());
        }
    }
}
