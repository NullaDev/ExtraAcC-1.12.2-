package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.VolcanicBall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityVolcanicBall extends EntityFlying {
	
	public static final int AGE = 80;
	public static final float BASIC_VELOCITY = 0.3F;
	public static final float MAX_VELOCITY = 0.6F;
	public static final float BASIC_DAMAGE = 10;
	public static final float MAX_DAMAGE = 20;
	
	public static final float DAMAGE_DECREASE_RATE = 0.5F;
	public static final float INITIAL_SIZE = 0.2F;
	
	private float _exp;
	private Vec3d _direc;

	public EntityVolcanicBall(World world) {
        super(world, INITIAL_SIZE, INITIAL_SIZE);
        this.setNoGravity();
        this.setDecrease(0.98F);
    }
	
    public EntityVolcanicBall(World world, EntityPlayer thrower, float exp, Vec3d dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, INITIAL_SIZE, INITIAL_SIZE, AGE);
        this.setNoGravity();
        this.setDecrease(0.98F);
        this._exp = exp;
        this._direc = dir;
        this.setVelocity(dir, getVelocity());
		this.harvestStrength = 0.2F;
	}
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
    
    private float getVelocity() {
    	return MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, this._exp);
    }
    
    private float getBasicDamage() {
    	return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this._exp);
    }
    
    private float getDamage() {
    	return getBasicDamage() * MathUtils.lerpf(1, DAMAGE_DECREASE_RATE, (float)this.ticksExisted / this.age);
    }

	@Override
	protected void onImpact(RayTraceResult pos) {
		if (pos.entityHit != null) {
			pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), VolcanicBall.INSTANCE).setProjectile(), this.getDamage());
			double v = MathUtils.lerpf(3, 6, _exp) / pos.entityHit.height;
			if (_direc != null)
				pos.entityHit.addVelocity(v * _direc.x, v * _direc.y, v * _direc.z);
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