package cn.nulladev.extraacc.entity;

import com.google.common.base.Optional;

import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBomberLance extends EntityFlying {
	
	public static final int AGE = 40;
	public static final float BASIC_VELOCITY = 2.0F;
	public static final float MAX_VELOCITY = 3.0F;
	public static final float BASIC_DAMAGE = 16;
	public static final float MAX_DAMAGE = 24;
	
	public static final float INITIAL_SIZE = 0.4F;
	
	private float exp;
	private Vec3d direc;
	
	protected static final DataParameter<Float> StartPosX = EntityDataManager.<Float>createKey(EntityBomberLance.class, DataSerializers.FLOAT);
	protected static final DataParameter<Float> StartPosY = EntityDataManager.<Float>createKey(EntityBomberLance.class, DataSerializers.FLOAT);
	protected static final DataParameter<Float> StartPosZ = EntityDataManager.<Float>createKey(EntityBomberLance.class, DataSerializers.FLOAT);

	public EntityBomberLance(World world) {
        super(world, INITIAL_SIZE, INITIAL_SIZE);
        this.setNoGravity();
        this.setDecrease(1.00F);
        this.setStartPos(this.posX, this.posY, this.posZ);
        this.ignoreFrustumCheck = true;
    }
	
    public EntityBomberLance(World world, EntityPlayer thrower, float _exp, Vec3d _dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, INITIAL_SIZE, INITIAL_SIZE, AGE);
        this.setNoGravity();
        this.setDecrease(1.00F);
        this.exp = _exp;
        this.direc = _dir;
        this.setStartPos(thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ);
        this.setVelocity(_dir, getVelocity(_exp));
    }
    
    private static float getVelocity(float exp) {
    	return MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, exp);
    }
    
    private float getBasicDamage(float exp) {
    	return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, exp);
    }
    
    private float getDamage(float exp) {
    	return getBasicDamage(exp);
    }
    
    @Override
	protected void entityInit() {
		super.entityInit();
        this.dataManager.register(StartPosX, Float.valueOf(0.0F));
        this.dataManager.register(StartPosY, Float.valueOf(0.0F));
        this.dataManager.register(StartPosZ, Float.valueOf(0.0F));
	}
    
    public void setStartPos(double x, double y, double z) {
		this.dataManager.set(StartPosX, Float.valueOf((float)x));
		this.dataManager.set(StartPosY, Float.valueOf((float)y));
		this.dataManager.set(StartPosZ, Float.valueOf((float)z));
	}
	
	public Vec3d getStartPos() {
		return new Vec3d(this.dataManager.get(StartPosX), 
				this.dataManager.get(StartPosY), 
				this.dataManager.get(StartPosZ)
				);
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		if (pos.entityHit != null) {
			float value = this.getVelocity(exp) * ( 2.0F / pos.entityHit.height);
			pos.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(this.getOwner()).setProjectile(), getDamage(exp));
			double v = 16 / pos.entityHit.height; 
			pos.entityHit.addVelocity(v * direc.x, v * direc.y, v * direc.z);
			pos.entityHit.setAir(300);
		}
		this.setDead();
	}

}