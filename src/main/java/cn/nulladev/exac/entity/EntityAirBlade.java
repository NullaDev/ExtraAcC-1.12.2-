package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.AirBlade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAirBlade extends EntityFlying {
	
	public static final int BASIC_AGE = 20;
	public static final int MAX_AGE = 30;
	
	public static final float VELOCITY = 1F;
	
	public static final float BASIC_DAMAGE = 12;
	public static final float MAX_DAMAGE = 18;
	
	public static final float DAMAGE_DECREASE_RATE = 0.75F;
	public static final float SIZE = 2F;
	
	private float exp;
	private Vec3d direc;
	
	private double offsetX = 0F;
	private double offsetZ = 0F;

	public EntityAirBlade(World world) {
        super(world, SIZE, 0.02F);
        this.setNoGravity();
        this.setDecrease(0.99F);
    }
	
    public EntityAirBlade(World world, EntityPlayer thrower, float _exp, Vec3d _dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, SIZE, 0.02F, getAge(_exp));
        this.setNoGravity();
        this.setDecrease(0.99F);
        this.exp = _exp;
        this.direc = _dir;
        this.setVelocity(_dir, VELOCITY);
        this.harvestStrength = 0.2F;
    }
     
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
    
    private static int getAge(float exp) {
    	return (int) MathUtils.lerpf(BASIC_AGE, MAX_AGE, exp);
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
			pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), AirBlade.INSTANCE).setProjectile().setDamageBypassesArmor(), getDamage(exp));
		}
		this.setDead();
	}
	
	@Override
    public void onUpdate() {
		super.onUpdate();
		Vec3d xzVec = new Vec3d(this.motionX, 0, this.motionZ).normalize();
		this.posX -= offsetX;
		this.posZ -= offsetZ;
		offsetX = 2F * xzVec.z * Math.sin(this.ticksExisted * Math.PI / 10);
		offsetZ = 2F * - xzVec.x * Math.sin(this.ticksExisted * Math.PI / 10);
		this.posX += offsetX;
		this.posZ += offsetZ;
	}

}