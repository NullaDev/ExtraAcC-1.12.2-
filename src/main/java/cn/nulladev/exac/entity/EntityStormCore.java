package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.EntitySelectors;
import cn.lambdalib2.util.MathUtils;
import cn.lambdalib2.util.Raytrace;
import cn.nulladev.exac.ability.aerohand.skill.StormCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityStormCore extends EntityHasOwner {

    public static final float BASIC_DAMAGE = 80;
    public static final float MAX_DAMAGE = 120;
    public static final float ATTRACT_POWER = 0.5F;
    public static final float EXPLOSION_TIME = 80;
    public static final float AGE = 100;
    private float _exp;

    public EntityStormCore(World world) {
        super(world);
    }

    public EntityStormCore(World _world, EntityPlayer owner, float exp) {
        super(_world);
        this.setOwner(owner);
        this._exp = exp;
        Vec3d pos = Raytrace.traceLiving(owner, 6, EntitySelectors.nothing()).hitVec;
        this.setPosition(pos.x, pos.y, pos.z);
    }

    private float getDamage() {
        return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this._exp);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted >= AGE) {
            this.setDead();
            return;
        } else if (this.ticksExisted <= EXPLOSION_TIME) {
            float r = 8F;
            List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
                    posX - r, posY - r, posZ - r, posX + r, posY + r, posZ + r));
            for (Entity target : list) {
                if (target instanceof EntityLivingBase) {
                    EntityLivingBase entity = (EntityLivingBase)target;
                    if (entity == this.getOwner())
                        continue;
                    float d = (float) Math.sqrt(Math.pow(entity.posX - posX, 2) + Math.pow(entity.posY - posY, 2) + Math.pow(entity.posZ - posZ, 2));
                    float power = Math.max(1 - d / 10F, 0.2F);
                    if (this.ticksExisted < EXPLOSION_TIME) {
                        entity.attackEntityFrom(new SkillDamageSource(this.getOwner(), StormCore.INSTANCE), 1F * power);
                        Vec3d direc = new Vec3d(this.posX - entity.posX, this.posY - entity.posY, this.posZ - entity.posZ).normalize();
                        entity.addVelocity(ATTRACT_POWER * power * direc.x, ATTRACT_POWER * power * direc.y, ATTRACT_POWER * power * direc.z);
                    } else {
                        entity.attackEntityFrom(new SkillDamageSource(this.getOwner(), StormCore.INSTANCE), this.getDamage() * power);
                    }
                } else if (target instanceof IProjectile) {
                    target.setDead();
                }

            }
        }
    }
}
