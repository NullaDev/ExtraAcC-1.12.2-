package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.BomberLance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBomberLance extends EntityFlying {
	
	public static final int AGE = 20;
	public static final float BASIC_VELOCITY = 1.0F;
	public static final float MAX_VELOCITY = 1.5F;
	public static final float BASIC_DAMAGE = 48;
	public static final float MAX_DAMAGE = 72;
	
	public static final float INITIAL_SIZE = 0.4F;
	
	private float _exp;
	private Vec3d _direc;
	
	protected static final DataParameter<Float> StartPosX = EntityDataManager.<Float>createKey(EntityBomberLance.class, DataSerializers.FLOAT);
	protected static final DataParameter<Float> StartPosY = EntityDataManager.<Float>createKey(EntityBomberLance.class, DataSerializers.FLOAT);
	protected static final DataParameter<Float> StartPosZ = EntityDataManager.<Float>createKey(EntityBomberLance.class, DataSerializers.FLOAT);

	public EntityBomberLance(World world) {
        super(world, INITIAL_SIZE, INITIAL_SIZE);
        this.setNoGravity();
		this.setNoDecrease();
        this.setStartPos(this.posX, this.posY, this.posZ);
        this.ignoreFrustumCheck = true;
    }
	
    public EntityBomberLance(World world, EntityPlayer thrower, float exp, Vec3d dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, INITIAL_SIZE, INITIAL_SIZE, AGE);
        this.setNoGravity();
        this.setNoDecrease();
        this._exp = exp;
        this._direc = dir;
        this.setStartPos(thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ);
        this.setVelocity(dir, getVelocity());
        this.ignoreFrustumCheck = true;
		this.harvestStrength = 0.4F;
    }
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
    
    private float getVelocity() {
    	return MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, this._exp);
    }
    
    private float getDamage() {
    	return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this._exp);
    }
    
    @Override
	protected void entityInit() {
		super.entityInit();
        this.dataManager.register(StartPosX, 0.0F);
        this.dataManager.register(StartPosY, 0.0F);
        this.dataManager.register(StartPosZ, 0.0F);
	}
    
    public void setStartPos(double x, double y, double z) {
		this.dataManager.set(StartPosX, (float) x);
		this.dataManager.set(StartPosY, (float) y);
		this.dataManager.set(StartPosZ, (float) z);
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
			pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), BomberLance.INSTANCE).setProjectile(), this.getDamage());
			double v = MathUtils.lerpf(10F, 15F, _exp) / pos.entityHit.height;
			if (_direc != null)
				pos.entityHit.addVelocity(v * _direc.x, v * _direc.y, v * _direc.z);
			pos.entityHit.setAir(300);
		}
		this.setDead();
	}

}