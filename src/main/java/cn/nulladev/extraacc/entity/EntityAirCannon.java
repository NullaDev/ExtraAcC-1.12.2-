package cn.nulladev.extraacc.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.ability.aerohand.skill.AirCannon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAirCannon extends EntityFlying {
	
	public static final int AGE = 80;
	public static final float BASIC_VELOCITY = 0.3F;
	public static final float MAX_VELOCITY = 0.6F;
	public static final float BASIC_DAMAGE = 8;
	public static final float MAX_DAMAGE = 16;
	
	public static final float DAMAGE_DECREASE_RATE = 0.5F;
	public static final float INITIAL_SIZE = 0.2F;
	
	private float exp;
	private Vec3d direc;

	public EntityAirCannon(World world) {
        super(world, INITIAL_SIZE, INITIAL_SIZE);
        this.setNoGravity();
        this.setDecrease(0.98F);
    }
	
    public EntityAirCannon(World world, EntityPlayer thrower, float _exp, Vec3d _dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, INITIAL_SIZE, INITIAL_SIZE, AGE);
        this.setNoGravity();
        this.setDecrease(0.98F);
        this.exp = _exp;
        this.direc = _dir;
        this.setVelocity(_dir, getVelocity(_exp));
    }
    
    private static float getVelocity(float exp) {
    	return MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, exp);
    }
    
    private float getBasicDamage(float exp) {
    	return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, exp);
    }
    
    private float getDamage(float exp) {
    	return getBasicDamage(exp) * MathUtils.lerpf(1, DAMAGE_DECREASE_RATE, (float)this.ticksExisted / this.age);
    }

	@Override
	protected void onImpact(RayTraceResult pos) {
		if (pos.entityHit != null) {
			pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), AirCannon.INSTANCE).setProjectile(), getDamage(exp));
			double v = MathUtils.lerpf(2, 3, exp) / pos.entityHit.height; 
			pos.entityHit.addVelocity(v * direc.x, v * direc.y, v * direc.z);
			pos.entityHit.setAir(300);
		}
		this.setDead();
	}
	
	@Override
    public void onUpdate() {
		super.onUpdate();
		this.width += 0.02F;
		this.height += 0.02F;
		this.setSize(width, height);
	}

}